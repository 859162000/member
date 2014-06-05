package com.wanda.member.upgrade.data;

import com.wanda.member.upgrade.data.TMemberPoint;
import com.wanda.member.upgrade.data.TMemberPointExample;
import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TMemberPointMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int countByExample(TMemberPointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int deleteByExample(TMemberPointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int deleteByPrimaryKey(BigDecimal memberPointId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int insert(TMemberPoint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int insertSelective(TMemberPoint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    List<TMemberPoint> selectByExample(TMemberPointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    TMemberPoint selectByPrimaryKey(BigDecimal memberPointId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int updateByExampleSelective(@Param("record") TMemberPoint record, @Param("example") TMemberPointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int updateByExample(@Param("record") TMemberPoint record, @Param("example") TMemberPointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int updateByPrimaryKeySelective(TMemberPoint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_MEMBER_POINT
     *
     * @mbggenerated Wed Oct 30 11:47:54 CST 2013
     */
    int updateByMemberId(TMemberPoint record);
}