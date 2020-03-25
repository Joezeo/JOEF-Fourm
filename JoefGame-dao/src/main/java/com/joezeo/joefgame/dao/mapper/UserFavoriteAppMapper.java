package com.joezeo.joefgame.dao.mapper;

import com.joezeo.joefgame.dao.pojo.UserFavoriteApp;
import com.joezeo.joefgame.dao.pojo.UserFavoriteAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserFavoriteAppMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    long countByExample(UserFavoriteAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int deleteByExample(UserFavoriteAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int insert(UserFavoriteApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int insertSelective(UserFavoriteApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    List<UserFavoriteApp> selectByExampleWithRowbounds(UserFavoriteAppExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    List<UserFavoriteApp> selectByExample(UserFavoriteAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    UserFavoriteApp selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int updateByExampleSelective(@Param("record") UserFavoriteApp record, @Param("example") UserFavoriteAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int updateByExample(@Param("record") UserFavoriteApp record, @Param("example") UserFavoriteAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int updateByPrimaryKeySelective(UserFavoriteApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_favorite_app
     *
     * @mbg.generated Fri Mar 20 22:14:24 CST 2020
     */
    int updateByPrimaryKey(UserFavoriteApp record);
}