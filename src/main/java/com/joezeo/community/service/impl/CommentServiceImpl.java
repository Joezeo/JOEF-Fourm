package com.joezeo.community.service.impl;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.enums.NotificationTypeEnum;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.*;
import com.joezeo.community.pojo.*;
import com.joezeo.community.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TopicExtMapper topicExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public void addComment(CommentDTO commentDTO, User notifier) {
        if (commentDTO == null) {
            log.error("函数addComment：参数commentDTO异常，=null");
            throw new ServiceException("参数commentDTO异常，=null");
        }
        if (notifier.getId() == null || notifier.getId() <= 0) {
            log.error("函数addComment：参数notifier异常");
            throw new ServiceException("参数userid异常");
        }

        if(commentDTO.getTopicid() == null || commentDTO.getTopicid() <= 0){
            log.error("函数addComment：参数topicId未传递");
            throw new CustomizeException(CustomizeErrorCode.QUESTION_ID_NOT_TRANSFER);
        }

        if (commentDTO.getParentId() == null || commentDTO.getParentId() <= 0) {
            log.error("函数addComment：参数parentId未传递");
            throw new CustomizeException(CustomizeErrorCode.PARENT_ID_NOT_TRANSFER);
        }

        if (commentDTO.getParentType() == null || !CommentTypeEnum.isExist(commentDTO.getParentType())) {
            log.error("函数addComment：参数parentType未传递");
            throw new CustomizeException(CustomizeErrorCode.PARENT_TYPE_ILEEAGUE);
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setUserid(notifier.getId());
        comment.setLikeCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(comment.getGmtCreate());
        comment.setCommentCount(0);

        // 判断当前评论是评论帖子，还是回复评论的
        if(comment.getParentType() == CommentTypeEnum.QUESTION.getType()){ // 回复帖子
            Topic topic = topicMapper.selectByPrimaryKey(commentDTO.getParentId());
            if(topic == null){
                log.error("函数addComment：要回复的帖子没有找到，可能已经被删除了");
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            // 累加评论数
            topic.setCommentCount(1);
            int count = topicExtMapper.incComment(topic);
            if(count != 1){
                log.error("函数addComment：累加评论数失败");
                throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
            }

            // 创建新通知
            createNotify(notifier.getId(), topic.getUserid(), topic.getId(), NotificationTypeEnum.QUESTION, notifier.getName(), topic.getTitle(), commentDTO.getTopicid());
        } else { // 回复评论
            Comment memComment = commentMapper.selectByPrimaryKey(commentDTO.getParentId());
            if(memComment == null){
                log.error("函数addComment：要回复的评论未找到，可能已被删除了");
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 累加二级评论数
            memComment.setCommentCount(1);
            int count = commentExtMapper.incComment(memComment);
            if(count != 1){
                log.error("函数addComment：累加二级评论数失败");
                throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
            }

            // 创建新通知
            createNotify(notifier.getId(), memComment.getUserid(), memComment.getId(), NotificationTypeEnum.COMMENT, notifier.getName(), memComment.getContent(), commentDTO.getTopicid());
        }

        int count = commentMapper.insertSelective(comment);
        if (count != 1) {
            log.error("函数addCommet：新评论在插入数据库时失败");
            throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
        }
    }

    private void createNotify(Long notifier, Long receiver, Long id, NotificationTypeEnum typeEnum, String notifierName, String relatedName, Long topicid) {
        if(notifier == receiver){ // 自己回复自己不创建通知
            return ;
        }
        Notification notification = new Notification();
        notification.setNotifier(notifier);
        notification.setReceiver(receiver);
        notification.setRelatedid(id);
        notification.setRelatedtype(typeEnum.getType());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setGmtModify(notification.getGmtCreate());
        notification.setNotifiername(notifierName);
        notification.setRelatedname(relatedName);
        notification.setTopicid(topicid);
        notificationMapper.insertSelective(notification);
    }

    @Override
    public List<CommentDTO> listByParentId(Long parentId, CommentTypeEnum typeEnum) {
        // 查询评论
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(parentId)
                .andParentTypeEqualTo(typeEnum.getType()); // 根据父类型以及父类型id 来查询所有评论
        example.setOrderByClause("gmt_create");
        List<Comment> comments = commentMapper.selectByExample(example);

        // 如果没有评论直接返回一个空的集合
        if(comments == null || comments.size() == 0){
            return new ArrayList<>();
        }

        // 获取所有评论的评论者id，并且去重
        List<Long> userids = comments.stream().map(c -> c.getUserid()).distinct().collect(Collectors.toList());

        // 根据评论者id所有评论者数据，并放入map中
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userids);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 将 Comment 转化为 CommentDto 并且放入user
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getUserid()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
