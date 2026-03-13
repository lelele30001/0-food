package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;


/**
 * 食谱
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2026-03-13 11:22:53
 */
@TableName("recipe")
public class RecipeEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 食谱标题
     */

    private String title;
    /**
     * 食谱描述
     */

    private String description;
    /**
     * 封面图片
     */

    private String coverImage;
    /**
     * 健身目标
     */

    private String fitnessGoal;
    /**
     * 饮食禁忌
     */

    private String dietaryRestrictions;
    /**
     * 用餐场景
     */

    private String mealScene;
    /**
     * 总热量
     */

    private Double totalCalories;
    /**
     * 状态
     */

    private String status;
    /**
     * 用户ID
     */

    private Long userId;
    /**
     * 用户名
     */

    private String username;
    /**
     * 头像
     */

    private String avatarurl;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;
    @TableField(exist = false)
    private List<RecipeIngredientEntity> ingredients;


    public RecipeEntity() {

    }

    public RecipeEntity(T t) {
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

    public List<RecipeIngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * 获取：食谱标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置：食谱标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取：食谱描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置：食谱描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：封面图片
     */
    public String getCoverImage() {
        return coverImage;
    }

    /**
     * 设置：封面图片
     */
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    /**
     * 获取：健身目标
     */
    public String getFitnessGoal() {
        return fitnessGoal;
    }

    /**
     * 设置：健身目标
     */
    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    /**
     * 获取：饮食禁忌
     */
    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    /**
     * 设置：饮食禁忌
     */
    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    /**
     * 获取：用餐场景
     */
    public String getMealScene() {
        return mealScene;
    }

    /**
     * 设置：用餐场景
     */
    public void setMealScene(String mealScene) {
        this.mealScene = mealScene;
    }

    /**
     * 获取：总热量
     */
    public Double getTotalCalories() {
        return totalCalories;
    }

    /**
     * 设置：总热量
     */
    public void setTotalCalories(Double totalCalories) {
        this.totalCalories = totalCalories;
    }

    /**
     * 获取：状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置：状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取：用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置：用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置：用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取：头像
     */
    public String getAvatarurl() {
        return avatarurl;
    }

    /**
     * 设置：头像
     */
    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

}