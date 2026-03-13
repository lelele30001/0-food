-- 食谱主表
CREATE TABLE `recipe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) NOT NULL COMMENT '食谱标题',
  `description` text COMMENT '食谱描述',
  `cover_image` varchar(255) COMMENT '封面图片',
  `fitness_goal` varchar(50) NOT NULL COMMENT '健身目标',
  `dietary_restrictions` varchar(255) COMMENT '饮食禁忌',
  `meal_scene` varchar(50) NOT NULL COMMENT '用餐场景',
  `total_calories` decimal(10,2) NOT NULL COMMENT '总热量',
  `status` varchar(50) DEFAULT '已发布' COMMENT '状态',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `avatarurl` varchar(255) COMMENT '用户头像',
  `addtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱主表';

-- 食谱食材明细表
CREATE TABLE `recipe_ingredient` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `recipe_id` bigint(20) NOT NULL COMMENT '食谱ID',
  `ingredient_code` varchar(50) NOT NULL COMMENT '食材编码',
  `ingredient_name` varchar(255) NOT NULL COMMENT '食材名称',
  `weight` decimal(10,2) NOT NULL COMMENT '食材重量',
  `calories` decimal(10,2) NOT NULL COMMENT '食材热量',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  PRIMARY KEY (`id`),
  KEY `idx_recipe_id` (`recipe_id`),
  CONSTRAINT `fk_recipe_ingredient_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱食材明细表';