package com.cl.controller;

import com.cl.annotation.IgnoreAuth;
import com.cl.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食物模拟接口
 * 模拟薄荷健康API
 */
@RestController
@RequestMapping("/food")
public class FoodController {

    // 预设食材数据
    private static final List<FoodItem> FOOD_ITEMS = new ArrayList<>();

    static {
        FOOD_ITEMS.add(new FoodItem("001", "鸡胸肉（水煮）", 165, "100g", 31.0, 0.0, 3.6));
        FOOD_ITEMS.add(new FoodItem("002", "鸡蛋（水煮）", 70, "1个", 6.3, 0.6, 4.8));
        FOOD_ITEMS.add(new FoodItem("003", "西兰花（水煮）", 34, "100g", 2.8, 7.0, 0.4));
        FOOD_ITEMS.add(new FoodItem("004", "全麦面包", 80, "1片", 3.5, 13.0, 1.5));
        FOOD_ITEMS.add(new FoodItem("005", "瘦牛肉（煎）", 125, "100g", 25.0, 0.0, 3.0));
        FOOD_ITEMS.add(new FoodItem("006", "红薯（蒸）", 86, "100g", 1.6, 20.0, 0.1));
        FOOD_ITEMS.add(new FoodItem("007", "燕麦片", 389, "100g", 17.0, 66.0, 7.0));
        FOOD_ITEMS.add(new FoodItem("008", "虾仁（水煮）", 80, "100g", 18.0, 0.0, 1.0));
        FOOD_ITEMS.add(new FoodItem("009", "黄瓜", 16, "100g", 0.8, 3.6, 0.2));
        FOOD_ITEMS.add(new FoodItem("010", "番茄", 18, "100g", 0.9, 4.0, 0.2));
        FOOD_ITEMS.add(new FoodItem("011", "豆腐（嫩）", 85, "100g", 8.0, 2.8, 4.8));
        FOOD_ITEMS.add(new FoodItem("012", "糙米（熟）", 116, "100g", 2.6, 23.0, 1.1));
        FOOD_ITEMS.add(new FoodItem("013", "牛油果", 160, "100g", 2.0, 8.5, 14.7));
        FOOD_ITEMS.add(new FoodItem("014", "希腊酸奶（无糖）", 59, "100g", 10.0, 3.0, 0.5));
        FOOD_ITEMS.add(new FoodItem("015", "菠菜", 28, "100g", 2.9, 4.5, 0.4));
    }

    /**
     * 食物搜索接口
     */
    @IgnoreAuth
    @GetMapping("/search")
    public R searchFood(@RequestParam String keyword) {
        List<FoodSearchResult> results = FOOD_ITEMS.stream()
                .filter(item -> item.getName().contains(keyword))
                .map(item -> new FoodSearchResult(item.getFoodId(), item.getName(), item.getCalories(), item.getUnit()))
                .collect(Collectors.toList());
        return R.ok().put("data", results);
    }

    /**
     * 食物详情接口
     */
    @IgnoreAuth
    @GetMapping("/detail")
    public R getFoodDetail(@RequestParam String foodId) {
        FoodItem item = FOOD_ITEMS.stream()
                .filter(food -> food.getFoodId().equals(foodId))
                .findFirst()
                .orElse(null);
        if (item == null) {
            return R.error("食物不存在");
        }
        FoodDetail detail = new FoodDetail(
                item.getFoodId(),
                item.getName(),
                item.getCalories(),
                item.getUnit(),
                item.getProtein(),
                item.getCarbs(),
                item.getFat()
        );
        return R.ok().put("data", detail);
    }

    /**
     * 食物分类列表接口
     */
    @IgnoreAuth
    @GetMapping("/list")
    public R getFoodList(@RequestParam String category) {
        // 简化实现，返回所有食材
        List<FoodSearchResult> results = FOOD_ITEMS.stream()
                .map(item -> new FoodSearchResult(item.getFoodId(), item.getName(), item.getCalories(), item.getUnit()))
                .collect(Collectors.toList());
        return R.ok().put("data", results);
    }

    // 内部类
    private static class FoodItem {
        private String foodId;
        private String name;
        private int calories;
        private String unit;
        private double protein;
        private double carbs;
        private double fat;

        public FoodItem(String foodId, String name, int calories, String unit, double protein, double carbs, double fat) {
            this.foodId = foodId;
            this.name = name;
            this.calories = calories;
            this.unit = unit;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
        }

        public String getFoodId() { return foodId; }
        public String getName() { return name; }
        public int getCalories() { return calories; }
        public String getUnit() { return unit; }
        public double getProtein() { return protein; }
        public double getCarbs() { return carbs; }
        public double getFat() { return fat; }
    }

    private static class FoodSearchResult {
        private String foodId;
        private String name;
        private int calories;
        private String unit;

        public FoodSearchResult(String foodId, String name, int calories, String unit) {
            this.foodId = foodId;
            this.name = name;
            this.calories = calories;
            this.unit = unit;
        }

        public String getFoodId() { return foodId; }
        public String getName() { return name; }
        public int getCalories() { return calories; }
        public String getUnit() { return unit; }
    }

    private static class FoodDetail {
        private String foodId;
        private String name;
        private int calories;
        private String unit;
        private double protein;
        private double carbs;
        private double fat;

        public FoodDetail(String foodId, String name, int calories, String unit, double protein, double carbs, double fat) {
            this.foodId = foodId;
            this.name = name;
            this.calories = calories;
            this.unit = unit;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
        }

        public String getFoodId() { return foodId; }
        public String getName() { return name; }
        public int getCalories() { return calories; }
        public String getUnit() { return unit; }
        public double getProtein() { return protein; }
        public double getCarbs() { return carbs; }
        public double getFat() { return fat; }
    }
}
