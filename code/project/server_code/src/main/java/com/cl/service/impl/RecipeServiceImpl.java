package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.RecipeDao;
import com.cl.dao.RecipeIngredientDao;
import com.cl.entity.RecipeEntity;
import com.cl.entity.RecipeIngredientEntity;
import com.cl.service.RecipeService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 食谱
 *
 * @author
 * @email
 * @date 2026-03-13 11:22:53
 */
@Service("recipeService")
public class RecipeServiceImpl extends ServiceImpl<RecipeDao, RecipeEntity> implements RecipeService {

    @Autowired
    private RecipeIngredientDao recipeIngredientDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RecipeEntity> page = this.selectPage(
                new Page<>(MPUtil.getPage(params), MPUtil.getLimit(params)),
                new EntityWrapper<RecipeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void publishRecipe(RecipeEntity recipe) {
        // 保存食谱主表
        this.insert(recipe);
        
        // 保存食材明细
        List<RecipeIngredientEntity> ingredients = recipe.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {
            for (RecipeIngredientEntity ingredient : ingredients) {
                ingredient.setRecipeId(recipe.getId());
                recipeIngredientDao.insert(ingredient);
            }
        }
    }

    @Override
    public PageUtils getUserRecipes(Map<String, Object> params, Long userId) {
        Page<RecipeEntity> page = this.selectPage(
                new Page<>(MPUtil.getPage(params), MPUtil.getLimit(params)),
                new EntityWrapper<RecipeEntity>().eq("user_id", userId)
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getAllRecipes(Map<String, Object> params) {
        Page<RecipeEntity> page = this.selectPage(
                new Page<>(MPUtil.getPage(params), MPUtil.getLimit(params)),
                new EntityWrapper<RecipeEntity>()
        );

        return new PageUtils(page);
    }

}
