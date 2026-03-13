package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 食谱食材明细
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2026-03-13 11:22:53
 */
@TableName("recipe_ingredient")
public class RecipeIngredientEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 食谱ID
     */

    private Long recipeId;
    /**
     * 食材编码
     */

    private String ingredientCode;
    /**
     * 食材名称
     */

    private String ingredientName;
    /**
     * 食材重量
     */

    private Double weight;
    /**
     * 食材热量
     */

    private Double calories;
    /**
     * 单位
     */

    private String unit;


    public RecipeIngredientEntity() {

    }

    public RecipeIngredientEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：食谱ID
     */
    public Long getRecipeId() {
        return recipeId;
    }

    /**
     * 设置：食谱ID
     */
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    /**
     * 获取：食材编码
     */
    public String getIngredientCode() {
        return ingredientCode;
    }

    /**
     * 设置：食材编码
     */
    public void setIngredientCode(String ingredientCode) {
        this.ingredientCode = ingredientCode;
    }

    /**
     * 获取：食材名称
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * 设置：食材名称
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * 获取：食材重量
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * 设置：食材重量
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 获取：食材热量
     */
    public Double getCalories() {
        return calories;
    }

    /**
     * 设置：食材热量
     */
    public void setCalories(Double calories) {
        this.calories = calories;
    }

    /**
     * 获取：单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置：单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

}