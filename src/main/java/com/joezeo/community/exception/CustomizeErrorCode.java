package com.joezeo.community.exception;

public enum CustomizeErrorCode implements IExceptionErrorCode{
    QUESTION_NOT_FOUND(2000,"该问题不见了，请换一个试试"),
    COMMENT_NOT_FOUND(2001, "该评论不见了，请换一个试试"),
    PARENT_ID_NOT_TRANSFER(2002, "请选定一个问题或评论进行回复！"),
    PARENT_TYPR_ILEEAGUE(2003, "非法的评论"),
    USER_NOT_LOGIN(2004, "当前操作需要登录后进行，请登录"),
    COMMENT_FAILD(2005, "评论失败，请稍后重试！"),
    SERVER_ERROR(5001, "服务器出现异常了，请稍后重试")
    ;

    private Integer code;
    private String message;

    CustomizeErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}