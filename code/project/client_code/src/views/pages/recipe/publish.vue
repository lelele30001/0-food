<template>
  <div class="edit_view" :style="{}">
    <div class="breadcrumb-wrapper" style="width: 100%">
      <div class="bread_view">
        <el-breadcrumb separator="Ξ" class="breadcrumb">
          <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }"
            >首页</el-breadcrumb-item
          >
          <el-breadcrumb-item class="second_breadcrumb"
            >食谱管理</el-breadcrumb-item
          >
          <el-breadcrumb-item class="second_breadcrumb"
            >发布食谱</el-breadcrumb-item
          >
        </el-breadcrumb>
      </div>
    </div>
    <el-form
      ref="formRef"
      :model="form"
      class="add_form"
      label-width="120px"
      :rules="rules"
    >
      <!-- 基础信息区 -->
      <el-row>
        <el-col :span="24">
          <h3 class="section-title">基础信息</h3>
        </el-col>
        <el-col :span="12">
          <el-form-item label="食谱标题" prop="title">
            <el-input
              class="list_inp"
              v-model="form.title"
              placeholder="请输入食谱标题"
              type="text"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="封面图" prop="coverImage">
            <uploads
              action="file/upload"
              tip="请上传封面图"
              style="width: 100%; text-align: left"
              :fileUrls="form.coverImage ? form.coverImage : ''"
              @change="coverImageUploadSuccess"
            >
            </uploads>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="食谱描述" prop="description">
            <el-input
              v-model="form.description"
              placeholder="请输入食谱描述"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 核心匹配区 -->
      <el-row>
        <el-col :span="24">
          <h3 class="section-title">核心匹配信息</h3>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适配健身目标" prop="fitnessGoal">
            <el-select
              class="list_sel"
              v-model="form.fitnessGoal"
              placeholder="请选择健身目标"
              style="width: 100%"
            >
              <el-option label="增肌" value="增肌"></el-option>
              <el-option label="减脂" value="减脂"></el-option>
              <el-option label="维持" value="维持"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="饮食禁忌" prop="dietaryRestrictions">
            <el-select
              class="list_sel"
              v-model="form.dietaryRestrictions"
              placeholder="请选择饮食禁忌"
              style="width: 100%"
              multiple
            >
              <el-option label="无禁忌" value="无禁忌"></el-option>
              <el-option label="海鲜" value="海鲜"></el-option>
              <el-option label="牛奶" value="牛奶"></el-option>
              <el-option label="辛辣" value="辛辣"></el-option>
              <el-option label="素食" value="素食"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用餐场景" prop="mealScene">
            <el-select
              class="list_sel"
              v-model="form.mealScene"
              placeholder="请选择用餐场景"
              style="width: 100%"
            >
              <el-option label="早餐" value="早餐"></el-option>
              <el-option label="午餐" value="午餐"></el-option>
              <el-option label="晚餐" value="晚餐"></el-option>
              <el-option label="加餐" value="加餐"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 食材明细区 -->
      <el-row>
        <el-col :span="24">
          <div class="section-header">
            <h3 class="section-title">食材明细</h3>
            <el-button type="primary" @click="addIngredient"
              >添加食材</el-button
            >
          </div>
        </el-col>
        <el-col
          :span="24"
          v-for="(ingredient, index) in form.ingredients"
          :key="index"
          class="ingredient-item"
        >
          <el-form-item
            :prop="`ingredients.${index}.ingredientName`"
            :rules="[
              { required: true, message: '请选择食材', trigger: 'blur' },
            ]"
          >
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input
                  v-model="ingredient.ingredientName"
                  placeholder="输入食材名称"
                  @input="
                    handleIngredientInput(index, ingredient.ingredientName)
                  "
                >
                  <template #append>
                    <el-button
                      @click="showIngredientSuggestions(index)"
                      :disabled="!ingredient.ingredientName"
                      >搜索</el-button
                    >
                  </template>
                </el-input>
                <!-- 食材建议下拉框 -->
                <div
                  v-if="suggestions[index] && suggestions[index].length > 0"
                  class="ingredient-suggestions"
                >
                  <div
                    v-for="(item, i) in suggestions[index]"
                    :key="i"
                    class="suggestion-item"
                    @click="selectIngredient(index, item)"
                  >
                    {{ item.name }} ({{ item.calories }}kcal/100g)
                  </div>
                </div>
              </el-col>
              <el-col :span="3">
                <el-input
                  v-model="ingredient.ingredientCode"
                  placeholder="编码"
                  readonly
                />
              </el-col>
              <el-col :span="3">
                <el-input
                  v-model.number="ingredient.weight"
                  placeholder="重量(g)"
                  type="number"
                  @input="calculateCalories(index)"
                />
              </el-col>
              <el-col :span="3">
                <el-input
                  v-model="ingredient.unit"
                  placeholder="单位"
                  readonly
                />
              </el-col>
              <el-col :span="3">
                <el-input
                  v-model="ingredient.baseCalories"
                  placeholder="基础热量"
                  readonly
                />
              </el-col>
              <el-col :span="3">
                <el-input
                  v-model="ingredient.calories"
                  placeholder="热量"
                  readonly
                />
              </el-col>
              <el-col :span="3">
                <el-button type="danger" @click="removeIngredient(index)"
                  >删除</el-button
                >
              </el-col>
            </el-row>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 总热量区 -->
      <el-row>
        <el-col :span="24">
          <h3 class="section-title">总热量</h3>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单份总热量(kcal)" prop="totalCalories">
            <el-input
              class="list_inp"
              v-model.number="form.totalCalories"
              placeholder="总热量"
              type="number"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="formModel_btn_box">
        <el-button class="formModel_cancel" @click="backClick">取消</el-button>
        <el-button class="formModel_confirm" @click="save" type="success"
          >保存</el-button
        >
      </div>
    </el-form>
  </div>
</template>
<script setup>
import { ref, computed, onMounted, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";
const context = getCurrentInstance()?.appContext.config.globalProperties;
const router = useRouter();

// 表单数据
const form = ref({
  title: "",
  description: "",
  coverImage: "",
  fitnessGoal: "",
  dietaryRestrictions: [],
  mealScene: "",
  totalCalories: 0,
  ingredients: [],
});
// 食材建议
const suggestions = ref({});
// 表单引用
const formRef = ref(null);
// 提交
const save = () => {
  console.log("开始保存");
  if (!formRef.value) {
    console.log("formRef不存在");
    context?.$toolUtil.message("表单未初始化", "error");
    return;
  }

  console.log("开始表单验证");
  formRef.value.validate((valid) => {
    console.log("验证结果:", valid);
    if (valid) {
      // 验证至少添加一种食材
      if (form.value.ingredients.length === 0) {
        console.log("食材列表为空");
        context?.$toolUtil.message("至少添加一种食材", "error");
        return;
      }

      // 准备提交数据
      const recipeData = {
        recipe: {
          title: form.value.title,
          description: form.value.description,
          coverImage: form.value.coverImage,
          fitnessGoal: form.value.fitnessGoal,
          dietaryRestrictions: form.value.dietaryRestrictions.join(","),
          mealScene: form.value.mealScene,
          totalCalories: form.value.totalCalories,
          userId: context?.$toolUtil.storageGet("userid"),
          status: "已发布",
        },
        ingredients: form.value.ingredients,
      };

      console.log("准备提交的数据:", recipeData);
      console.log("context.$http是否存在:", !!context?.$http);

      // 提交到后端
      context
        ?.$http({
          url: "recipe/publish",
          method: "post",
          data: recipeData,
        })
        .then((res) => {
          console.log("后端响应:", res);
          if (res.data.code === 0) {
            context?.$toolUtil.message("发布成功", "success");
            // 跳转到食谱列表页
            router.push("/pages/recipe/list");
          } else {
            context?.$toolUtil.message(res.data.msg || "发布失败", "error");
          }
        })
        .catch((error) => {
          console.error("请求失败:", error);
          context?.$toolUtil.message("请求失败: " + error.message, "error");
        });
    } else {
      console.log("表单验证失败");
    }
  });
};

// 表单验证规则
const rules = ref({
  title: [{ required: true, message: "请输入食谱标题", trigger: "blur" }],
  fitnessGoal: [{ required: true, message: "请选择健身目标", trigger: "blur" }],
  dietaryRestrictions: [
    { required: true, message: "请选择饮食禁忌", trigger: "blur" },
  ],
  mealScene: [{ required: true, message: "请选择用餐场景", trigger: "blur" }],
  totalCalories: [{ required: true, message: "请输入总热量", trigger: "blur" }],
});

// 封面图上传回调
const coverImageUploadSuccess = (e) => {
  form.value.coverImage = e;
};

// 添加食材
const addIngredient = () => {
  form.value.ingredients.push({
    ingredientCode: "",
    ingredientName: "",
    weight: 0,
    unit: "g",
    baseCalories: 0,
    calories: 0,
  });
};

// 移除食材
const removeIngredient = (index) => {
  form.value.ingredients.splice(index, 1);
  calculateTotalCalories();
};

// 处理食材输入
const handleIngredientInput = (index, value) => {
  if (value.length > 1) {
    // 模拟食材搜索
    searchIngredients(index, value);
  } else {
    suggestions.value[index] = [];
  }
};

// 显示食材建议
const showIngredientSuggestions = (index) => {
  const ingredient = form.value.ingredients[index];
  if (ingredient.ingredientName) {
    searchIngredients(index, ingredient.ingredientName);
  }
};

// 搜索食材（本地模拟）
const searchIngredients = (index, keyword) => {
  // 模拟食材数据库 - 健身常见食材
  const mockIngredients = [
    { code: "001", name: "鸡胸肉（水煮）", calories: 165, unit: "100g" },
    { code: "002", name: "鸡蛋（水煮）", calories: 70, unit: "1个" },
    { code: "003", name: "西兰花（水煮）", calories: 34, unit: "100g" },
    { code: "004", name: "全麦面包", calories: 80, unit: "1片" },
    { code: "005", name: "瘦牛肉（煎）", calories: 125, unit: "100g" },
    { code: "006", name: "红薯（蒸）", calories: 86, unit: "100g" },
    { code: "007", name: "燕麦片", calories: 389, unit: "100g" },
    { code: "008", name: "虾仁（水煮）", calories: 80, unit: "100g" },
    { code: "009", name: "黄瓜", calories: 16, unit: "100g" },
    { code: "010", name: "番茄", calories: 18, unit: "100g" },
    { code: "011", name: "豆腐（嫩）", calories: 85, unit: "100g" },
    { code: "012", name: "糙米（熟）", calories: 116, unit: "100g" },
    { code: "013", name: "牛油果", calories: 160, unit: "100g" },
    { code: "014", name: "希腊酸奶（无糖）", calories: 59, unit: "100g" },
    { code: "015", name: "菠菜", calories: 28, unit: "100g" },
  ];

  // 过滤匹配的食材
  const filtered = mockIngredients.filter((item) =>
    item.name.includes(keyword)
  );

  suggestions.value[index] = filtered;
};

// 选择食材
const selectIngredient = (index, item) => {
  const ingredient = form.value.ingredients[index];
  ingredient.ingredientCode = item.code;
  ingredient.ingredientName = item.name;
  ingredient.baseCalories = item.calories;
  ingredient.unit = item.unit;
  // 计算热量
  calculateCalories(index);
  // 清空建议
  suggestions.value[index] = [];
};

// 计算单个食材的热量
const calculateCalories = (index) => {
  const ingredient = form.value.ingredients[index];
  if (ingredient.weight && ingredient.baseCalories) {
    ingredient.calories = (ingredient.weight / 100) * ingredient.baseCalories;
    // 更新总热量
    calculateTotalCalories();
  }
};

// 计算总热量
const calculateTotalCalories = () => {
  let total = 0;
  form.value.ingredients.forEach((item) => {
    total += item.calories || 0;
  });
  form.value.totalCalories = total;
};

// 取消
const backClick = () => {
  history.back();
};

// 初始化
onMounted(() => {
  // 添加一个默认食材行
  addIngredient();
});
</script>
<style lang="scss" scoped>
// 面包屑盒子
.bread_view {
  :deep(.breadcrumb) {
    .el-breadcrumb__separator {
    }
    .first_breadcrumb {
      .el-breadcrumb__inner {
      }
    }
    .second_breadcrumb {
      .el-breadcrumb__inner {
      }
    }
  }
}

// 章节标题
.section-title {
  font-size: 18px;
  font-weight: bold;
  margin: 20px 0 10px 0;
  color: #333;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px 0 10px 0;
}

// 食材项
.ingredient-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 4px;
}

// 食材建议
.ingredient-suggestions {
  position: absolute;
  z-index: 1000;
  width: calc(100% - 80px);
  max-height: 200px;
  overflow-y: auto;
  background: #fff;
  border: 1px solid #ddd;
  border-top: none;
}

.suggestion-item {
  padding: 8px 12px;
  cursor: pointer;
  &:hover {
    background: #f5f7fa;
  }
}

// 表单
.add_form {
  // form item
  :deep(.el-form-item) {
    //label
    .el-form-item__label {
    }
    // 内容盒子
    .el-form-item__content {
      // 输入框
      .list_inp {
      }
      // 下拉框
      .list_sel {
        //去掉默认样式
        .select-trigger {
          height: 100%;
          .el-input {
            height: 100%;
          }
        }
      }
      // 长文本
      .el-textarea__inner {
      }
      //图片上传样式
      .el-upload-list {
        //提示语
        .el-upload__tip {
        }
        //外部盒子
        .el-upload--picture-card {
          //图标
          .el-icon {
          }
        }
        .el-upload-list__item {
        }
      }
    }
  }
}

// 按钮盒子
.formModel_btn_box {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  .formModel_cancel {
    margin-right: 20px;
  }
  .formModel_cancel:hover {
  }

  .formModel_confirm {
  }
  .formModel_confirm:hover {
  }
}
</style>
<style>
.edit_view {
  width: 1200px;
  margin: 20px auto;
  padding: 20px 20px 20px;
  background: #fff;
  overflow: hidden;
  border: 0px solid #eee;
  font-size: 16px;
  color: #666;
}

.edit_view .add_form {
  width: 100%;
  padding: 30px;
  border: 0px solid #eee;
}

.edit_view .add_form .el-form-item {
  margin: 0px 0px 20px;
  display: flex;
}

.edit_view .add_form .el-form-item .el-form-item__label {
  width: 150px;
  background: none;
  text-align: right;
  display: block;
  font-size: 16px;
  color: rgb(51, 51, 51);
  font-weight: 500;
}

.edit_view .add_form .el-form-item .el-form-item__content {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  width: calc(100% - 120px);
}

.edit_view .add_form .el-form-item .el-form-item__content .list_inp {
  height: 36px;
  line-height: 36px;
  border: 1px solid rgb(221, 221, 221);
  padding: 0px 10px;
  width: 100%;
  box-sizing: border-box;
  background: rgb(255, 255, 255);
  font-size: 16px;
}

.edit_view .add_form .el-form-item .el-form-item__content .list_sel {
  line-height: 36px;
  border: 1px solid rgb(221, 221, 221);
  box-sizing: border-box;
  width: calc(100% - 0px);
  padding: 0px 10px;
  border-radius: 0px;
  background: rgb(255, 255, 255);
  font-size: 16px;
}

.edit_view .add_form .el-form-item .el-form-item__content .el-textarea {
}

.edit_view
  .add_form
  .el-form-item
  .el-form-item__content
  .el-textarea
  .el-textarea__inner {
  width: 100%;
  min-height: 100px;
  padding: 12px;
  border: 1px solid rgb(221, 221, 221);
  border-radius: 0px;
  color: #666;
  font-size: 16px;
}

.edit_view
  .add_form
  .el-form-item
  .el-form-item__content
  .el-upload--picture-card {
  background-color: rgb(255, 255, 255);
  width: 100px;
  height: 90px;
  line-height: 100px;
  text-align: center;
  border: 1px solid rgb(221, 221, 221);
  border-radius: 0px;
  cursor: pointer;
}

.edit_view
  .add_form
  .el-form-item
  .el-form-item__content
  .el-upload--picture-card
  .el-icon {
  font-size: 32px;
  color: #999;
}

.edit_view
  .add_form
  .el-form-item
  .el-form-item__content
  .el-upload--picture-card
  .el-icon
  svg {
  color: #999;
}

.edit_view
  .add_form
  .el-form-item
  .el-form-item__content
  .img-uploader
  .el-upload__tip {
  font-size: 15px;
  color: #666;
  margin: 0;
}
</style>
