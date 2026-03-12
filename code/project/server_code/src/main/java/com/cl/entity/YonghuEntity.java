package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 用户
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-26 11:22:53
 */
@TableName("yonghu")
public class YonghuEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 用户账号
     */

    private String yonghuzhanghao;
    /**
     * 用户密码
     */

    private String yonghumima;
    /**
     * 用户姓名
     */

    private String yonghuxingming;
    /**
     * 头像
     */

    private String touxiang;
    /**
     * 性别
     */

    private String xingbie;
    /**
     * 手机号码
     */

    private String shoujihaoma;
    /**
     * 身份证
     */

    private String shenfenzheng;
    /**
     * 邮箱
     */

    private String youxiang;
    /**
     * 身高(cm)
     */
    @TableField("shengao")
    private Double shengao;
    /**
     * 体重(kg)
     */
    @TableField("tizhong")
    private Double tizhong;
    /**
     * 年龄
     */
    @TableField("nianling")
    private Integer nianling;
    /**
     * 健身目标
     */
    @TableField("jianshenmubiao")
    private String jianshenmubiao;
    /**
     * 饮食禁忌
     */
    @TableField("yinsjijin")
    private String yinsjijin;
    /**
     * 每日热量需求(kcal)
     */
    @TableField("meirireliang")
    private Double meirireliang;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public YonghuEntity() {

    }


    public YonghuEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：用户账号
     */
    public String getYonghuzhanghao() {
        return yonghuzhanghao;
    }

    /**
     * 设置：用户账号
     */
    public void setYonghuzhanghao(String yonghuzhanghao) {
        this.yonghuzhanghao = yonghuzhanghao;
    }

    /**
     * 获取：用户密码
     */
    public String getYonghumima() {
        return yonghumima;
    }

    /**
     * 设置：用户密码
     */
    public void setYonghumima(String yonghumima) {
        this.yonghumima = yonghumima;
    }

    /**
     * 获取：用户姓名
     */
    public String getYonghuxingming() {
        return yonghuxingming;
    }

    /**
     * 设置：用户姓名
     */
    public void setYonghuxingming(String yonghuxingming) {
        this.yonghuxingming = yonghuxingming;
    }

    /**
     * 获取：头像
     */
    public String getTouxiang() {
        return touxiang;
    }

    /**
     * 设置：头像
     */
    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    /**
     * 获取：性别
     */
    public String getXingbie() {
        return xingbie;
    }

    /**
     * 设置：性别
     */
    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }

    /**
     * 获取：手机号码
     */
    public String getShoujihaoma() {
        return shoujihaoma;
    }

    /**
     * 设置：手机号码
     */
    public void setShoujihaoma(String shoujihaoma) {
        this.shoujihaoma = shoujihaoma;
    }

    /**
     * 获取：身份证
     */
    public String getShenfenzheng() {
        return shenfenzheng;
    }

    /**
     * 设置：身份证
     */
    public void setShenfenzheng(String shenfenzheng) {
        this.shenfenzheng = shenfenzheng;
    }

    /**
     * 获取：邮箱
     */
    public String getYouxiang() {
        return youxiang;
    }

    /**
     * 设置：邮箱
     */
    public void setYouxiang(String youxiang) {
        this.youxiang = youxiang;
    }

    /**
     * 获取：身高(cm)
     */
    public Double getShengao() {
        return shengao;
    }

    /**
     * 设置：身高(cm)
     */
    public void setShengao(Double shengao) {
        this.shengao = shengao;
    }

    /**
     * 获取：体重(kg)
     */
    public Double getTizhong() {
        return tizhong;
    }

    /**
     * 设置：体重(kg)
     */
    public void setTizhong(Double tizhong) {
        this.tizhong = tizhong;
    }

    /**
     * 获取：年龄
     */
    public Integer getNianling() {
        return nianling;
    }

    /**
     * 设置：年龄
     */
    public void setNianling(Integer nianling) {
        this.nianling = nianling;
    }

    /**
     * 获取：健身目标
     */
    public String getJianshenmubiao() {
        return jianshenmubiao;
    }

    /**
     * 设置：健身目标
     */
    public void setJianshenmubiao(String jianshenmubiao) {
        this.jianshenmubiao = jianshenmubiao;
    }

    /**
     * 获取：饮食禁忌
     */
    public String getYinsjijin() {
        return yinsjijin;
    }

    /**
     * 设置：饮食禁忌
     */
    public void setYinsjijin(String yinsjijin) {
        this.yinsjijin = yinsjijin;
    }

    /**
     * 获取：每日热量需求(kcal)
     */
    public Double getMeirireliang() {
        return meirireliang;
    }

    /**
     * 设置：每日热量需求(kcal)
     */
    public void setMeirireliang(Double meirireliang) {
        this.meirireliang = meirireliang;
    }

}
