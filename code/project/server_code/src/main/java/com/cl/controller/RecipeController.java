package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.RecipeEntity;
import com.cl.service.RecipeService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 食谱
 * 后端接口
 *
 * @author
 * @email
 * @date 2026-03-13 11:22:53
 */
@RestController
@RequestMapping("/forum")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RecipeEntity recipe,
                  HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            recipe.setUserId((Long) request.getSession().getAttribute("userId"));
        }
        EntityWrapper<RecipeEntity> ew = new EntityWrapper<RecipeEntity>();


        PageUtils page = recipeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, recipe), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, RecipeEntity recipe,
                  HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            recipe.setUserId((Long) request.getSession().getAttribute("userId"));
        }
        EntityWrapper<RecipeEntity> ew = new EntityWrapper<RecipeEntity>();

        PageUtils page = recipeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, recipe), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @IgnoreAuth
    @RequestMapping("/flist")
    public R flist(@RequestParam Map<String, Object> params, RecipeEntity recipe, HttpServletRequest request) {
        EntityWrapper<RecipeEntity> ew = new EntityWrapper<RecipeEntity>();
        PageUtils page = recipeService.getAllRecipes(params);
        return R.ok().put("data", page);
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(RecipeEntity recipe) {
        EntityWrapper<RecipeEntity> ew = new EntityWrapper<RecipeEntity>();
        ew.allEq(MPUtil.allEQMapPre(recipe, "recipe"));
        RecipeEntity recipeEntity = recipeService.selectOne(ew);
        return R.ok("查询食谱成功").put("data", recipeEntity);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        RecipeEntity recipe = recipeService.selectById(id);
        return R.ok().put("data", recipe);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        RecipeEntity recipe = recipeService.selectById(id);
        return R.ok().put("data", recipe);
    }

    /**
     * 食谱详情
     */
    @IgnoreAuth
    @RequestMapping("/list/{id}")
    public R list(@PathVariable("id") String id) {
        RecipeEntity recipe = recipeService.selectById(id);
        return R.ok().put("data", recipe);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RecipeEntity recipe, HttpServletRequest request) {
        recipe.setUserId((Long) request.getSession().getAttribute("userId"));
        recipeService.publishRecipe(recipe);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody RecipeEntity recipe, HttpServletRequest request) {
        recipe.setUserId((Long) request.getSession().getAttribute("userId"));
        recipeService.publishRecipe(recipe);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RecipeEntity recipe, HttpServletRequest request) {
        recipeService.updateById(recipe);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        recipeService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
