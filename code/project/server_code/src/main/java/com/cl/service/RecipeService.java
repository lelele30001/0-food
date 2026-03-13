package com.cl.service;

import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.RecipeEntity;
import com.cl.utils.PageUtils;

import java.util.Map;

/**
 * 食谱
 *
 * @author
 * @email
 * @date 2026-03-13 11:22:53
 */
public interface RecipeService extends IService<RecipeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void publishRecipe(RecipeEntity recipe);

    PageUtils getUserRecipes(Map<String, Object> params, Long userId);

    PageUtils getAllRecipes(Map<String, Object> params);

}
