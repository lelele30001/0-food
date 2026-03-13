<template>
  <div class="forum_view" :style="{}">
    <div class="back_view" v-if="centerType">
      <el-button class="back_btn" @click="backClick" type="primary"
        >返回</el-button
      >
    </div>
    <div class="section_title">
      <span>{{ myType ? "我的发布" : formName }}</span>
    </div>
    <el-form :model="searchQuery" class="list_search">
      <div class="search_view">
        <div class="search_label">标题：</div>
        <div class="search_box">
          <el-input
            class="search_inp"
            v-model="searchQuery.title"
            placeholder="标题"
            size="small"
            clearable
          >
          </el-input>
        </div>
      </div>
      <div class="search_btn_view">
        <el-button class="search_btn" type="primary" @click="searchClick"
          >搜索</el-button
        >
        <el-button
          class="add_btn"
          @click="addClick"
          v-if="!myType && btnAuth('forum', '新增')"
          type="success"
          >发布新帖</el-button
        >
      </div>
    </el-form>
    <div class="forum_list">
      <div
        class="forum_item"
        v-for="(item, index) in list"
        :key="index"
        @mouseenter="forumEnter(index)"
        @mouseleave="forumLeave"
        @click.stop="detailClick(item.id)"
      >
        <div class="forum_item_left">
          <span class="forum_item_title">{{ item.title }}</span>
          <span class="forum_item_name"> (发布人：{{ item.username }}) </span>
        </div>
        <div
          class="forum_item_btn_box"
          v-if="
            item.userid == userid &&
            forumShowIndex == index &&
            (btnAuth('forum', '修改') || btnAuth('forum', '删除'))
          "
        >
          <el-button
            class="forum_edit_btn"
            v-if="btnAuth('forum', '修改')"
            type="primary"
            @click.stop="editClick(item.id)"
            >修改</el-button
          >
          <el-button
            class="forum_del_btn"
            v-if="btnAuth('forum', '删除')"
            type="danger"
            @click.stop="delClick(item.id)"
            >删除</el-button
          >
        </div>
        <div class="forum_time" v-else>{{ item.addtime }}</div>
      </div>
      <el-pagination
        background
        :layout="layouts.join(',')"
        :total="total"
        :page-size="listQuery.limit"
        v-model:current-page="listQuery.page"
        prev-text="上一页"
        next-text="下一页"
        :hide-on-single-page="false"
        :style="{}"
        @size-change="sizeChange"
        @current-change="currentChange"
      />
    </div>

    <el-dialog
      v-model="formVisible"
      :title="formTitle"
      width="70%"
      custom-class="edit_view"
      destroy-on-close
    >
      <el-form
        class="add_form"
        :model="form"
        label-width="120px"
        :rules="rules"
        ref="formRef"
      >
        <!-- 基础信息区 -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="食谱标题" prop="title">
              <el-input
                class="list_inp"
                v-model="form.title"
                placeholder="请输入食谱标题"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面图片" prop="coverImage">
              <el-upload
                class="avatar-uploader"
                action="#"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleAvatarChange"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="form.coverImage"
                  :src="form.coverImage"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="食谱描述" prop="description">
              <el-input
                type="textarea"
                class="list_inp"
                v-model="form.description"
                placeholder="请输入食谱描述"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 核心匹配区 -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="健身目标" prop="fitnessGoal">
              <el-select
                v-model="form.fitnessGoal"
                placeholder="请选择健身目标"
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
                v-model="form.dietaryRestrictions"
                placeholder="请选择饮食禁忌"
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
              <el-select v-model="form.mealScene" placeholder="请选择用餐场景">
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
            <el-form-item label="食材明细">
              <div
                v-for="(ingredient, index) in form.ingredients"
                :key="index"
                class="ingredient-item"
              >
                <el-row :gutter="20">
                  <el-col :span="7">
                    <el-form-item
                      :prop="`ingredients.${index}.ingredientName`"
                      :rules="[
                        {
                          required: true,
                          message: '请选择食材',
                          trigger: 'blur',
                        },
                      ]"
                    >
                      <el-autocomplete
                        v-model="ingredient.ingredientName"
                        :fetch-suggestions="querySearchAsync"
                        placeholder="请输入食材名称"
                        @select="(item) => handleIngredientSelect(item, index)"
                      ></el-autocomplete>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item
                      label="份数"
                      :prop="`ingredients.${index}.weight`"
                      :rules="[
                        {
                          required: true,
                          message: '请输入份数',
                          trigger: 'blur',
                        },
                      ]"
                    >
                      <div class="weight-input-container">
                        <el-input-number
                          v-model="ingredient.weight"
                          :min="ingredient.unit.includes('100g') ? 100 : 1"
                          :step="ingredient.unit.includes('100g') ? 100 : 1"
                          :precision="0"
                          placeholder="请输入份数"
                        ></el-input-number>
                        <span class="weight-unit">{{ ingredient.unit || 'g' }}</span>
                      </div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="3">
                    <el-form-item>
                      <el-input
                        v-model="ingredient.unit"
                        disabled
                        placeholder="单位"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <el-form-item>
                      <el-input
                        v-model="ingredient.calories"
                        disabled
                        placeholder="热量"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <el-button type="danger" @click="removeIngredient(index)"
                      >删除</el-button
                    >
                  </el-col>
                </el-row>
              </div>
              <el-button type="primary" @click="addIngredient"
                >添加食材</el-button
              >
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 总热量区 -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="单份总热量" prop="totalCalories">
              <el-input-number
                v-model="form.totalCalories"
                :min="0"
                placeholder="总热量"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="formModel_btn_box">
          <el-button class="formModel_cancel" @click="formVisible = false"
            >关闭</el-button
          >
          <el-button class="formModel_confirm" type="primary" @click="save">{{
            form.id ? "修改保存" : "发布"
          }}</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="detailVisible" :title="''" width="70%" destroy-on-close>
      <div class="forum_detail">
        <div class="forum_title">
          <span>{{ detailForm.title }}</span>
        </div>
        <div class="forum_info">
          <span class="forum_name"> 发布人：{{ detailForm.username }} </span>
          <span class="forum_time">发布时间：{{ detailForm.addtime }}</span>
        </div>
        <div
          class="thumbs_view"
          style="
            display: flex;
            column-gap: 20px;
            margin: 0 auto;
            justify-content: center;
          "
        >
          <template v-if="!thumbsupOrCrazilyInfo.type">
            <div
              class="zan_view"
              style="cursor: pointer"
              @click="thumbsupOrCrazilyClick(21)"
            >
              <i class="iconfont icon-thumb-up-line1"></i>
              <span>赞({{ detailForm.thumbsupNumber }})</span>
            </div>
            <div
              class="zan_view"
              style="cursor: pointer"
              @click="thumbsupOrCrazilyClick(22)"
            >
              <i class="iconfont icon-thumb-down-line1"></i>
              <span>踩({{ detailForm.crazilyNumber }})</span>
            </div>
          </template>
          <template v-else>
            <div
              class="zan_view"
              style="cursor: pointer"
              v-if="thumbsupOrCrazilyInfo.type == 21"
              @click="cancelThumbsupOrCrazilyClick(21)"
            >
              <i class="iconfont iconfontActive icon-thumb-up-fill1"></i>
              <span class="textActive"
                >取消赞({{ detailForm.thumbsupNumber }})</span
              >
            </div>
            <div
              class="zan_view"
              style="cursor: pointer"
              v-else
              @click="cancelThumbsupOrCrazilyClick(22)"
            >
              <i class="iconfont iconfontActive icon-thumb-down-fill1"></i>
              <span class="textActive"
                >取消踩({{ detailForm.crazilyNumber }})</span
              >
            </div>
          </template>
          <!-- 收藏按钮 -->
          <div
            class="zan_view"
            style="cursor: pointer"
            @click="collectClick"
          >
            <i :class="isCollected ? 'iconfont iconfontActive icon-star-fill' : 'iconfont icon-star-line'"></i>
            <span :class="isCollected ? 'textActive' : ''">{{ isCollected ? '已收藏' : '收藏' }}</span>
          </div>
        </div>
        <el-divider />
        <div class="forum_content" v-html="detailForm.content"></div>
      </div>
      <el-card class="forum_comment_box">
        <el-divider class="comment_divider" content-position="left"
          >评论列表</el-divider
        >
        <div class="comment_add_box">
          <el-button class="comment_add" @click="commentClick" type="success"
            >发表评论</el-button
          >
        </div>
        <div class="forum_comment_list">
          <div
            class="forum_comment"
            v-for="(item, index) in detailForm.childs"
            :key="index"
            @mouseenter="commentEnter(index)"
            @mouseleave="commentLeave"
          >
            <div class="forum_comment_user">
              <img
                v-if="item.avatarurl"
                class="forum_comment_user_avatar"
                :src="$config.url + item.avatarurl.split(',')[0]"
                alt=""
              />
              <img
                v-else
                class="forum_comment_user_avatar"
                src="@/assets/avatar.png"
                alt=""
              />
              <span class="forum_comment_username"
                >用户：{{ item.username }}</span
              >
            </div>
            <div class="forum_comment_content" v-html="item.content"></div>
            <div class="forum_comment_reply_view">
              <div
                class="forum_comment_del"
                v-if="commentShowIndex == index && item.userid == userid"
                @click="commentDelClick(item.id)"
              >
                删除
              </div>
              <div
                class="forum_comment_reply"
                v-if="commentShowIndex == index"
                @click="commentReplyClick(item.id)"
              >
                回复
              </div>
            </div>
            <div class="forum_comment_second_list" v-if="item.childs">
              <el-divider
                class="forum_comment_second_divider"
                content-position="left"
                >回复</el-divider
              >
              <div
                class="forum_comment_second"
                v-for="(items, indexs) in item.childs"
                :key="indexs"
                @mouseenter="commentEnter1(indexs)"
                @mouseleave="commentLeave1"
              >
                <div class="forum_comment_second_user">
                  <img
                    v-if="items.avatarurl"
                    class="forum_comment_second_user_avatar"
                    :src="$config.url + items.avatarurl.split(',')[0]"
                    alt=""
                  />
                  <img
                    v-else
                    class="forum_comment_second_user_avatar"
                    src="@/assets/avatar.png"
                    alt=""
                  />
                  <span class="forum_comment_second_username"
                    >用户：{{ items.username }}</span
                  >
                </div>
                <div
                  class="forum_comment_second_content"
                  v-html="items.content"
                ></div>
                <div class="forum_comment_second_reply_view">
                  <div
                    class="forum_comment_second_del"
                    v-if="
                      commentShowIndex == index &&
                      items.userid == userid &&
                      commentShowIndex1 == indexs
                    "
                    @click="commentDelClick(items.id)"
                  >
                    删除
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </el-dialog>
    <el-dialog
      v-model="commentVisible"
      :title="'发表评论'"
      width="50%"
      destroy-on-close
    >
      <el-form class="add_form" :model="commentForm" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item prop="content" label="内容">
              <editor
                :value="commentForm.content"
                placeholder="请输入评论"
                class="list_editor"
                @change="commentChange"
              ></editor>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="formModel_btn_box">
          <el-button class="formModel_cancel" @click="commentVisible = false"
            >关闭</el-button
          >
          <el-button
            class="formModel_confirm"
            type="primary"
            @click="commentSave"
            >发表</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, nextTick, getCurrentInstance, watch } from "vue";
import { ElMessageBox } from "element-plus";
import { useRoute, useRouter } from "vue-router";
const context = getCurrentInstance()?.appContext.config.globalProperties;
//基础信息
const tableName = "forum";
const formName = "美食论坛";
const router = useRouter();
const route = useRoute();
//基础信息
//权限验证
const btnAuth = (e, a) => {
  return context?.$toolUtil.isAuth(e, a);
};
const list = ref([]);
const listLoading = ref(false);
const listQuery = ref({
  page: 1,
  limit: 20,
  isdone: "开放",
  sort: "isTop,topTime,addtime",
  order: "desc,desc,desc",
});
const myListQuery = ref({
  page: 1,
  limit: 20,
  parentid: 0,
  sort: "addtime",
  order: "desc",
});
const searchQuery = ref({});
//分页
const layouts = ref(["total", "prev", "pager", "next", "sizes", "jumper"]);
const total = ref(0);
const sizeChange = (size) => {
  listQuery.value.limit = size;
  getList();
};
const currentChange = (page) => {
  listQuery.value.page = page;
  getList();
};
//分页
const searchClick = () => {
  listQuery.value.page = 1;
  getList();
};
const getList = () => {
  console.log('开始获取列表数据');
  console.log('当前页面类型:', myType.value ? '我的发布' : '论坛列表');
  listLoading.value = true;
  let params = myType.value
    ? JSON.parse(JSON.stringify(myListQuery.value))
    : JSON.parse(JSON.stringify(listQuery.value));
  if (searchQuery.value.title && searchQuery.value.title != "") {
    params["title"] = `%${searchQuery.value.title}%`;
  }
  console.log('请求参数:', params);
  console.log('请求URL:', `forum/${myType.value ? "page" : "flist"}`);
  context
    ?.$http({
      url: `forum/${myType.value ? "page" : "flist"}`,
      method: "get",
      params: params,
    })
    .then((res) => {
      console.log('获取列表成功响应:', res);
      listLoading.value = false;
      list.value = res.data.data.list;
      total.value = res.data.data.total;
      console.log('获取到的列表数据:', list.value);
      console.log('总记录数:', total.value);
      // 打印列表中的所有标题，确认新发布的食谱是否在列表中
      console.log('列表中的标题:', list.value.map(item => item.title));
    })
    .catch((error) => {
      console.error('获取列表失败:', error);
      listLoading.value = false;
    });
};
const userid = ref(0);
const form = ref({
  title: "",
  description: "",
  coverImage: "",
  fitnessGoal: "",
  dietaryRestrictions: "",
  mealScene: "",
  totalCalories: 0,
  isdone: "开放",
  parentid: 0,
  userid: userid.value,
  username: context?.$toolUtil.storageGet("frontName"),
  avatarurl: context?.$toolUtil.storageGet("headportrait") || "",
  ingredients: [],
});
const rules = ref({
  title: [
    {
      required: true,
      message: "请输入标题",
      trigger: "blur",
    },
  ],
  fitnessGoal: [
    {
      required: true,
      message: "请选择健身目标",
      trigger: "blur",
    },
  ],
  mealScene: [
    {
      required: true,
      message: "请选择用餐场景",
      trigger: "blur",
    },
  ],
  totalCalories: [
    {
      required: true,
      message: "请输入总热量",
      trigger: "blur",
    },
  ],
});
const formRef = ref(null);
const formVisible = ref(false);
const formTitle = ref("");
const forumShowIndex = ref(-1);
//判断按钮显示
const forumEnter = (index) => {
  forumShowIndex.value = index;
};
const forumLeave = () => {
  forumShowIndex.value = -1;
};
const resetForm = () => {
  form.value = {
    title: "",
    description: "",
    coverImage: "",
    fitnessGoal: "",
    dietaryRestrictions: "",
    mealScene: "",
    totalCalories: 0,
    status: "已发布",
    parentid: 0,
    userId: userid.value,
    username: context?.$toolUtil.storageGet("frontName"),
    avatarurl: context?.$toolUtil.storageGet("headportrait") || "",
    ingredients: [],
  };
};
//发布新贴
const addClick = () => {
  resetForm();
  formTitle.value = "发布食谱";
  formVisible.value = true;
};
//修改帖子
const editClick = (id = null) => {
  if (id) {
    context
      ?.$http({
        url: `forum/detail/${id}`,
        method: "get",
      })
      .then((res) => {
        form.value = res.data.data;
        formTitle.value = "修改帖子";
        formVisible.value = true;
      });
  }
};
//帖子内容回调
const contentChange = (e) => {
  form.value.content = e;
};
//判断是否调我的发布列表
const myType = ref(false);
//判断是否从个人中心跳转
const centerType = ref(false);
//返回
const backClick = () => {
  router.push(
    `/index/${context?.$toolUtil.storageGet("frontSessionTable")}Center`
  );
};
//初始化
const init = () => {
  if (route.query.centerType) {
    centerType.value = true;
  }
  if (route.query.myType) {
    myType.value = true;
  }
  userid.value = context?.$toolUtil.storageGet("userid");
  getList();
  
  // 检查是否有detailId参数，如果有，自动打开帖子详情
  if (route.query.detailId) {
    detailClick(route.query.detailId);
  }
};
const detailVisible = ref(false);
const detailForm = ref({});
const isCollected = ref(false);
//详情
const detailClick = (id = null) => {
  if (id) {
    context
      ?.$http({
        url: `forum/list/${id}`,
        method: "get",
      })
      .then((res) => {
        detailForm.value = res.data.data;
        // 赞踩状态
        getThumbsupOrCrazily();
        // 检查收藏状态
        checkIfCollected();
        detailVisible.value = true;
      });
  }
};
//删除
const delClick = (id = null) => {
  if (id) {
    ElMessageBox.confirm(`是否删除选中帖子`, "提示", {
      confirmButtonText: "是",
      cancelButtonText: "否",
      type: "warning",
    }).then(() => {
      context
        ?.$http({
          url: `forum/delete`,
          method: "post",
          data: [id],
        })
        .then((res) => {
          context?.$toolUtil.message("删除成功", "success", () => {
            getList();
          });
        });
    });
  }
};

// 检查帖子是否已收藏
const checkIfCollected = () => {
  if (!detailForm.value.id || !userid.value) {
    isCollected.value = false;
    return;
  }
  
  context
    ?.$http({
      url: `storeup/query`,
      method: "get",
      params: {
        userid: userid.value,
        refid: detailForm.value.id,
        tablename: "forum"
      },
    })
    .then((res) => {
      isCollected.value = res.data.code === 0 && res.data.data;
    })
    .catch(() => {
      isCollected.value = false;
    });
};

// 收藏/取消收藏
const collectClick = () => {
  if (!detailForm.value.id || !userid.value) {
    context?.$toolUtil.message("请先登录", "error");
    return;
  }
  
  if (isCollected.value) {
    // 取消收藏
    context
      ?.$http({
        url: "storeup/query",
        method: "get",
        params: {
          userid: userid.value,
          refid: detailForm.value.id,
          tablename: "forum"
        },
      })
      .then((res) => {
        if (res.data.code === 0 && res.data.data) {
          const storeupId = res.data.data.id;
          context
            ?.$http({
              url: "storeup/delete",
              method: "post",
              data: [storeupId],
            })
            .then((res) => {
              if (res.data.code === 0) {
                isCollected.value = false;
                context?.$toolUtil.message("取消收藏成功", "success");
              } else {
                context?.$toolUtil.message(res.data.msg || "操作失败", "error");
              }
            })
            .catch(() => {
              context?.$toolUtil.message("操作失败，请稍后重试", "error");
            });
        }
      })
      .catch(() => {
        context?.$toolUtil.message("操作失败，请稍后重试", "error");
      });
  } else {
    // 收藏
    context
      ?.$http({
        url: "storeup/save",
        method: "post",
        data: {
          userid: userid.value,
          refid: detailForm.value.id,
          tablename: "forum",
          name: detailForm.value.title,
          type: "1",
          picture: detailForm.value.coverImage || ""
        },
      })
      .then((res) => {
        if (res.data.code === 0) {
          isCollected.value = true;
          context?.$toolUtil.message("收藏成功", "success");
        } else {
          context?.$toolUtil.message(res.data.msg || "操作失败", "error");
        }
      })
      .catch(() => {
        context?.$toolUtil.message("操作失败，请稍后重试", "error");
      });
  }
};

//发布帖子
const save = () => {
  console.log('开始保存食谱');
  formRef.value.validate((valid) => {
    console.log('表单验证结果:', valid);
    if (valid) {
      // 验证食材明细
      if (!form.value.ingredients || form.value.ingredients.length === 0) {
        context?.$toolUtil.message("请至少添加一种食材", "error");
        return;
      }
      
      // 验证每个食材是否完整
      const hasInvalidIngredient = form.value.ingredients.some(ingredient => {
        return !ingredient.ingredientName || !ingredient.weight || !ingredient.unit || !ingredient.calories;
      });
      
      if (hasInvalidIngredient) {
        context?.$toolUtil.message("请完整填写所有食材信息", "error");
        return;
      }
      
      // 确保用户ID正确
      const userId = context?.$toolUtil.storageGet("userid") || userid.value;
      form.value.userid = userId;
      // 确保isdone字段设置正确
      form.value.isdone = "开放";
      console.log('用户ID:', userId);
      console.log('isdone:', form.value.isdone);
      console.log('准备提交的数据:', form.value);
      
      context
        ?.$http({
          url: `forum/${form.value.id ? "update" : "add"}`,
          method: "post",
          data: form.value,
        })
        .then((res) => {
          console.log('保存成功响应:', res);
          context?.$toolUtil.message(
            `${form.value.id ? "修改" : "发布"}成功`,
            "success",
            () => {
              // 强制刷新列表
              console.log('准备刷新列表');
              listQuery.value.page = 1; // 重置到第一页
              console.log('当前页面:', myType.value ? '我的发布' : '论坛列表');
              getList();
              formVisible.value = false;
              // 重置表单
              form.value = {
                title: "",
                description: "",
                coverImage: "",
                fitnessGoal: "",
                dietaryRestrictions: "",
                mealScene: "",
                totalCalories: 0,
                isdone: "开放",
                parentid: 0,
                userid: context?.$toolUtil.storageGet("userid") || userid.value,
                username: context?.$toolUtil.storageGet("frontName"),
                avatarurl: context?.$toolUtil.storageGet("headportrait") || "",
                ingredients: [],
              };
            }
          );
        })
        .catch((error) => {
          console.error("保存失败:", error);
          context?.$toolUtil.message("保存失败，请稍后重试", "error");
        });
    }
  });
};
//评论
const commentForm = ref({
  content: "",
  parentid: "",
  userid: "",
  username: "",
  avatarurl: "",
});
const commentVisible = ref(false);
//重置评论
const resetCommentForm = () => {
  commentForm.value = {
    content: "",
    parentid: "",
    userid: context?.$toolUtil.storageGet("userid"),
    username: context?.$toolUtil.storageGet("frontName"),
    avatarurl: context?.$toolUtil.storageGet("headportrait")
      ? context?.$toolUtil.storageGet("headportrait")
      : "",
  };
};
//发表评论
const commentClick = () => {
  resetCommentForm();
  commentForm.value.parentid = detailForm.value.id;
  commentVisible.value = true;
};
//回复按钮显示
const commentShowIndex = ref(-1);
const commentShowIndex1 = ref(-1);
const commentEnter = (index) => {
  commentShowIndex.value = index;
};
const commentLeave = () => {
  commentShowIndex.value = -1;
};
const commentEnter1 = (index) => {
  commentShowIndex1.value = index;
};
const commentLeave1 = () => {
  commentShowIndex1.value = -1;
};
//评论内容回调
const commentChange = (e) => {
  commentForm.value.content = e;
};
//发表保存
const commentSave = () => {
  if (commentForm.value.content == "") {
    context?.$toolUtil.message(`请输入内容`, "error");
    return false;
  }
  let sensitiveWords = "";
  let sensitiveWordsArr = [];
  if (sensitiveWords) {
    sensitiveWordsArr = sensitiveWords.split(",");
  }
  for (var i = 0; i < sensitiveWordsArr.length; i++) {
    //全局替换
    var reg = new RegExp(sensitiveWordsArr[i], "g");
    //判断内容中是否包括敏感词
    if (commentForm.value.content.indexOf(sensitiveWordsArr[i]) > -1) {
      // 将敏感词替换为 **
      commentForm.value.content = commentForm.value.content.replace(reg, "**");
    }
  }
  context
    ?.$http({
      url: "forum/add",
      method: "post",
      data: commentForm.value,
    })
    .then((res) => {
      context?.$toolUtil.message(`发表成功`, "success", () => {
        detailClick(detailForm.value.id);
        commentVisible.value = false;
      });
    });
};
//评论删除
const commentDelClick = (id = null) => {
  if (id) {
    ElMessageBox.confirm(`是否删除选中评论`, "提示", {
      confirmButtonText: "是",
      cancelButtonText: "否",
      type: "warning",
    }).then(() => {
      context
        ?.$http({
          url: `forum/delete`,
          method: "post",
          data: [id],
        })
        .then((res) => {
          context?.$toolUtil.message("删除成功", "success", () => {
            detailClick(detailForm.value.id);
          });
        });
    });
  }
};
//评论回复
const commentReplyClick = (id = null) => {
  if (id) {
    resetCommentForm();
    commentForm.value.parentid = id;
    commentVisible.value = true;
  }
};
// 赞or踩
const thumbsupOrCrazilyInfo = ref({});
// 获取赞踩状态
const getThumbsupOrCrazily = () => {
  if (context?.$toolUtil.storageGet("frontToken")) {
    context
      ?.$http({
        url: "storeup/page",
        method: "get",
        params: {
          page: 1,
          limit: 99,
          refid: detailForm.value.id,
          tablename: "forum",
          userid: context?.$toolUtil.storageGet("userid"),
        },
      })
      .then((res) => {
        let list = res.data.data.list.filter(
          (item) => item.type == 21 || item.type == 22
        );
        if (!list.length) {
          thumbsupOrCrazilyInfo.value = {};
        } else {
          thumbsupOrCrazilyInfo.value = list[0];
        }
      });
  }
};
// 赞踩按钮
const thumbsupOrCrazilyClick = (type) => {
  let params = {
    name: detailForm.value.title,
    picture: "",
    refid: detailForm.value.id,
    type: type,
    tablename: "forum",
    userid: context?.$toolUtil.storageGet("userid"),
  };
  context
    ?.$http({
      url: "storeup/add",
      method: "post",
      data: params,
    })
    .then((res) => {
      if (type == 21) detailForm.value.thumbsupNumber += 1;
      if (type == 22) detailForm.value.crazilyNumber += 1;
      context?.$http({
        url: `forum/update`,
        method: "post",
        data: detailForm.value,
      });
      getThumbsupOrCrazily();
      context?.$toolUtil.message("操作成功", "success");
    });
};
//取消赞踩按钮
const cancelThumbsupOrCrazilyClick = (type) => {
  let ids = [];
  ids.push(thumbsupOrCrazilyInfo.value.id);
  context
    ?.$http({
      url: "storeup/delete",
      method: "post",
      data: ids,
    })
    .then((res) => {
      if (type == 21 && detailForm.value.thumbsupNumber > 0)
        detailForm.value.thumbsupNumber -= 1;
      if (type == 22 && detailForm.value.crazilyNumber > 0)
        detailForm.value.crazilyNumber -= 1;
      context?.$http({
        url: `forum/update`,
        method: "post",
        data: detailForm.value,
      });
      getThumbsupOrCrazily();
      context?.$toolUtil.message("取消成功", "success");
    });
};

// 预设食材数据
const presetIngredients = [
  { foodId: "001", name: "鸡胸肉（水煮）", calories: 165, unit: "100g" },
  { foodId: "002", name: "鸡蛋（水煮）", calories: 70, unit: "1个" },
  { foodId: "003", name: "西兰花（水煮）", calories: 34, unit: "100g" },
  { foodId: "004", name: "全麦面包", calories: 80, unit: "1片" },
  { foodId: "005", name: "瘦牛肉（煎）", calories: 125, unit: "100g" },
  { foodId: "006", name: "红薯（蒸）", calories: 86, unit: "100g" },
  { foodId: "007", name: "燕麦片", calories: 389, unit: "100g" },
  { foodId: "008", name: "虾仁（水煮）", calories: 80, unit: "100g" },
  { foodId: "009", name: "黄瓜", calories: 16, unit: "100g" },
  { foodId: "010", name: "番茄", calories: 18, unit: "100g" },
  { foodId: "011", name: "豆腐（嫩）", calories: 85, unit: "100g" },
  { foodId: "012", name: "糙米（熟）", calories: 116, unit: "100g" },
  { foodId: "013", name: "牛油果", calories: 160, unit: "100g" },
  { foodId: "014", name: "希腊酸奶（无糖）", calories: 59, unit: "100g" },
  { foodId: "015", name: "菠菜", calories: 28, unit: "100g" },
];

// 搜索食材
const querySearchAsync = (query, callback) => {
  if (!query) {
    callback([]);
    return;
  }

  // 前端本地搜索
  const results = presetIngredients.filter(item => 
    item.name.includes(query)
  );
  
  callback(
    results.map((item) => ({
      value: item.name,
      foodId: item.foodId,
      calories: item.calories,
      unit: item.unit,
    }))
  );
};

// 处理食材选择
const handleIngredientSelect = (item, index) => {
  form.value.ingredients[index].ingredientCode = item.foodId;
  form.value.ingredients[index].unit = item.unit;
  form.value.ingredients[index].calories = item.calories;
  // 根据单位设置默认重量
  if (item.unit.includes("100g")) {
    form.value.ingredients[index].weight = 100;
  } else {
    form.value.ingredients[index].weight = 1;
  }
  calculateTotalCalories();
};

// 添加食材
const addIngredient = () => {
  form.value.ingredients.push({
    ingredientCode: "",
    ingredientName: "",
    weight: 1,
    calories: 0,
    unit: "",
  });
};

// 删除食材
const removeIngredient = (index) => {
  form.value.ingredients.splice(index, 1);
  calculateTotalCalories();
};

// 计算总热量
const calculateTotalCalories = () => {
  let total = 0;
  form.value.ingredients.forEach((ingredient) => {
    if (ingredient.weight && ingredient.calories) {
      // 检查单位是否包含"100g"
      if (ingredient.unit.includes("100g")) {
        total += (ingredient.weight / 100) * ingredient.calories;
      } else {
        // 其他单位（如"1个"、"1片"等）直接相乘
        total += ingredient.weight * ingredient.calories;
      }
    }
  });
  form.value.totalCalories = Math.round(total * 100) / 100;
};

// 监听食材重量变化，重新计算总热量
watch(
  () => form.value.ingredients,
  () => {
    calculateTotalCalories();
  },
  { deep: true }
);

// 图片上传
const handleAvatarChange = (file) => {
  // 验证文件类型
  const isJPG = file.raw.type === "image/jpeg" || file.raw.type === "image/png";
  const isLt2M = file.raw.size / 1024 / 1024 < 2;
  
  if (!isJPG) {
    ElMessage.error("请上传JPG或PNG格式的图片");
    return false;
  }
  
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过2MB");
    return false;
  }
  
  // 创建本地预览URL
  form.value.coverImage = URL.createObjectURL(file.raw);
  console.log('图片选择成功，预览路径:', form.value.coverImage);
  return false; // 阻止自动上传
};

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === "image/jpeg" || file.type === "image/png";
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isJPG) {
    ElMessage.error("请上传JPG或PNG格式的图片");
  }
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过2MB");
  }
  return isJPG && isLt2M;
};

init();
</script>

<style lang="scss" scoped>
// 返回盒子
.back_view {
  border-radius: 4px;
  padding: 10px 0px;
  margin: 10px auto;
  background: none;
  width: 100%;
  text-align: right;
  // 返回按钮
  .back_btn {
    border: 1px solid var(--theme);
    cursor: pointer;
    border-radius: 0px;
    padding: 0 24px;
    color: #fff;
    background: var(--theme);
    width: auto;
    font-size: 14px;
    height: 34px;
  }
  // 返回按钮-悬浮
  .back_btn:hover {
  }
}
//搜索
.list_search {
  .search_view {
    .search_label {
    }
    .search_box {
      // 输入框
      :deep(.search_inp) {
      }
    }
  }
  .search_btn_view {
    // 搜索按钮
    .search_btn {
    }
    // 搜索按钮-悬浮
    .search_btn:hover {
    }
    // 新增按钮
    .add_btn {
    }
    // 新增按钮-悬浮
    .add_btn:hover {
    }
  }
}
// 列表盒子
.forum_list {
  // item
  .forum_item {
    // item左部
    .forum_item_left {
      // 标题
      .forum_item_title {
      }
      // 发布人
      .forum_item_name {
      }
    }
    // 按钮盒子
    .forum_item_btn_box {
      // 修改按钮
      .forum_edit_btn {
      }
      // 修改按钮-悬浮
      .forum_edit_btn:hover {
      }
      // 删除按钮
      .forum_del_btn {
      }
      // 删除按钮-悬浮
      .forum_del_btn:hover {
      }
    }
    // 时间
    .forum_time {
    }
  }

  .forum_item:hover {
  }
}

//帖子详情
.forum_detail {
  .forum_title {
  }

  .forum_info {
    .forum_name {
    }
    .forum_time {
    }
  }

  .forum_content {
  }
}
// 评论盒子
.forum_comment_box {
  // 分割线
  :deep(.comment_divider) {
    border: none;
    background: #dcdfe6;
    // 分割线文字样式
    .el-divider__text {
      font-size: 16px;
    }
  }
  // 新增评论盒子
  .comment_add_box {
    margin: 0 0 20px;
    text-align: right;
    // 按钮
    .comment_add {
      border: 0;
      cursor: pointer;
      border-radius: 2px;
      padding: 0 24px;
      margin: 0 10px 0 0;
      color: #fff;
      background: var(--theme);
      width: auto;
      font-size: 15px;
      height: 36px;
    }
    // 按钮-悬浮
    .comment_add:hover {
    }
  }
  //帖子评论
  .forum_comment_list {
    // 评论item
    .forum_comment {
      // 评论用户盒子
      .forum_comment_user {
        // 用户头像
        .forum_comment_user_avatar {
        }
        // 用户名
        .forum_comment_username {
        }
      }
      // 评论内容
      .forum_comment_content {
      }
      // 回复按钮盒子
      .forum_comment_reply_view {
        // 删除按钮
        .forum_comment_del {
        }
        // 删除按钮-悬浮
        .forum_comment_del:hover {
        }
        // 回复按钮
        .forum_comment_reply {
        }
        // 回复按钮-悬浮
        .forum_comment_reply:hover {
        }
      }
      // 二级评论盒子
      .forum_comment_second_list {
        // 分割线
        :deep(.forum_comment_second_divider) {
          border: none;
          background: #dcdfe6;
          // 分割线文字样式
          .el-divider__text {
            font-size: 16px;
          }
        }
        // 二级评论item
        .forum_comment_second {
          // 用户盒子
          .forum_comment_second_user {
            // 用户头像
            .forum_comment_second_user_avatar {
            }
            // 用户名
            .forum_comment_second_username {
            }
          }
          // 二级内容
          .forum_comment_second_content {
          }
          // 二级按钮盒子
          .forum_comment_second_reply_view {
            // 二级删除
            .forum_comment_second_del {
            }
            // 二级删除-悬浮
            .forum_comment_second_del:hover {
            }
          }
        }
      }
    }
  }
}

// 分页器
.el-pagination {
  // 总页码
  :deep(.el-pagination__total) {
  }
  // 上一页
  :deep(.btn-prev) {
  }
  // 下一页
  :deep(.btn-next) {
  }
  // 上一页禁用
  :deep(.btn-prev:disabled) {
  }
  // 下一页禁用
  :deep(.btn-next:disabled) {
  }
  // 页码
  :deep(.el-pager) {
    // 数字
    .number {
    }
    // 数字悬浮
    .number:hover {
    }
    // 选中
    .number.is-active {
    }
  }
  // sizes
  :deep(.el-pagination__sizes) {
    .el-select {
      //去掉默认样式
      .select-trigger {
        height: 100%;
        .el-input {
          height: 100%;
        }
      }
    }
  }
  // 跳页
  :deep(.el-pagination__jump) {
    // 输入框
    .el-input {
    }
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
      // 富文本
      .list_editor {
      }
      //单选框样式
      .list_radio {
        //未选中样式
        .el-radio {
          //单选框
          .el-radio__inner {
          }
          //提示文字
          .el-radio__label {
          }
        }
        //选中样式
        .is-checked {
          //单选框
          .el-radio__inner {
          }
          //提示文字
          .el-radio__label {
          }
        }
      }
    }
  }
}

// 份数输入容器
.weight-input-container {
  display: flex;
  align-items: center;
  width: 100%;
  
  .el-input-number {
    flex: 1;
    width: 100%;
    
    :deep(.el-input-number__decrease),
    :deep(.el-input-number__increase) {
      width: 32px; /* 调整按钮宽度，与总热量样式一致 */
    }
    
    :deep(.el-input-number__input) {
      text-align: center; /* 文本居中 */
      width: 100%;
      min-width: 40px;
    }
  }
  
  .weight-unit {
    margin-left: 8px;
    font-size: 14px;
    color: #606266;
    white-space: nowrap;
  }
}

// 食材项
.ingredient-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #f9fafc;
}

// 调整form-item布局
.ingredient-item :deep(.el-form-item) {
  margin-bottom: 0;
  display: flex;
  align-items: center;
}

.ingredient-item :deep(.el-form-item__content) {
  flex: 1;
  margin-left: 0 !important;
}

// 食材搜索结果
:deep(.el-autocomplete-suggestion) {
  .el-autocomplete-suggestion__list {
    padding: 0;
    
    .el-autocomplete-suggestion__item {
      padding: 10px 15px;
      cursor: pointer;
      
      &:hover {
        background-color: #ecf5ff;
      }
      
      &.is-active {
        background-color: #409eff;
        color: #fff;
      }
    }
  }
}
// 按钮盒子
.formModel_btn_box {
  .formModel_cancel {
  }
  .formModel_cancel:hover {
  }

  .formModel_confirm {
  }
  .formModel_confirm:hover {
  }
}

// 封面图片上传
.avatar-uploader {
  display: block;
  width: 120px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  
  .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
  
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  
  &:hover {
    border-color: #409eff;
  }
}
</style>
<style>
/*总盒子*/
.forum_view {
  width: 1200px;
  margin: 20px auto;
  padding: 0px;
  background: #fff;
}
/**标题**/
.forum_view .section_title {
  width: 100%;
  margin: 0px 0 20px;
  z-index: 999;
  text-align: left;
  background: none;
  margin-left: -10px;
  border-bottom: 1px #e5e5e5 solid;
  position: relative;
}
.forum_view .section_title span {
  display: inline-block;
  font-size: 20px;
  background: var(--theme);
  color: #fff;
  font-weight: 500;
  padding: 15px 20px 15px 20px;
  min-width: 250px;
}
.forum_view .section_title:after {
  content: "";
  height: 1px;
  width: 1px;
  border-style: solid;
  border-width: 5px;
  position: absolute;
  bottom: -10px;
  left: 0;
  border-color: #222 #222 transparent transparent;
}

/*搜索盒子*/
.forum_view .list_search {
  width: 100%;
  margin: 30px auto;
  display: flex;
  align-items: center;
  justify-content: center;
}
.forum_view .list_search .search_view {
  display: flex;
  align-items: center;
  margin-right: 10px;
}
.forum_view .list_search .search_view .search_label {
  font-size: 16px;
  white-space: nowrap;
}
.forum_view .list_search .search_view .search_box {
}
.forum_view .list_search .search_view .search_box .search_inp {
  padding: 0 10px;
  border: 1px solid #ddd;
  font-size: 15px;
  height: 38px;
}
.forum_view .list_search .search_btn_view .search_btn {
  background: var(--theme);
  color: rgb(255, 255, 255);
  border: 0;
  border-radius: 0;
  height: 38px;
  font-size: 15px;
}
.forum_view .list_search .search_btn_view .search_btn:hover {
  opacity: 0.8;
}
.forum_view .list_search .search_btn_view .add_btn {
  background: #666;
  color: rgb(255, 255, 255);
  border: 0;
  border-radius: 0;
  height: 38px;
  font-size: 15px;
}
.forum_view .list_search .search_btn_view .add_btn:hover {
  opacity: 0.8;
}

/*列表盒子*/
.forum_view .forum_list {
  width: 100%;
  margin: 20px auto;
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
/*item*/
.forum_view .forum_list .forum_item {
  width: 100%;
  cursor: pointer;
  display: flex;
  flex-wrap: wrap;
  margin: 0px;
  height: auto;
  background: rgb(255, 255, 255);
  justify-content: space-between;
  padding: 20px 10px;
  background: none;
  margin: 0 0 20px;
  box-shadow: 0 0 0px rgba(20, 49, 49, 0.1);
  min-height: 90px;
  border: 1px solid #eee;
}

.forum_view .forum_list .forum_item:last-child {
}
.forum_view .forum_list .forum_item:hover {
  background: none;
  color: #333;
}
/*左边内容盒子*/
.forum_view .forum_list .forum_item .forum_item_left {
  display: flex;
  align-items: center;
}
/*标题*/
.forum_view .forum_list .forum_item .forum_item_left .forum_item_title {
  font-size: 16px;
  color: rgb(51, 51, 51);
  font-weight: 600;
}
/*发布人*/
.forum_view .forum_list .forum_item .forum_item_left .forum_item_name {
  color: rgb(153, 153, 153);
}
/*时间*/
.forum_view .forum_list .forum_item .forum_time {
  font-size: 15px;
  color: rgb(153, 153, 153);
  margin: 12px 0px 0px;
}
/*按钮盒子*/
.forum_view .forum_list .forum_item .forum_item_btn_box {
  width: auto;
}
/*修改*/
.forum_view .forum_list .forum_item .forum_item_btn_box .forum_edit_btn {
  margin: 0px 10px 0px 0px;
  padding: 0px 24px;
  width: auto;
  height: 40px;
  font-size: 14px;
  color: rgb(255, 255, 255);
  border-radius: 4px;
  border: 0px;
  background: rgba(254, 176, 67, 0.8);
  cursor: pointer;
}
/*删除*/
.forum_view .forum_list .forum_item .forum_item_btn_box .forum_del_btn {
  margin: 0px 10px 0px 0px;
  padding: 0px 24px;
  width: auto;
  height: 40px;
  font-size: 14px;
  color: rgb(255, 255, 255);
  border-radius: 4px;
  border: 0px;
  background: rgba(254, 66, 66, 0.8);
  cursor: pointer;
}

/*详情盒子*/
.forum_view .forum_detail {
  width: 100%;
  margin: 20px 0px;
}
/*标题*/
.forum_view .forum_detail .forum_title {
  width: 100%;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  color: rgb(0, 0, 0);
}
/*info盒子*/
.forum_view .forum_detail .forum_info {
  width: 100%;
  text-align: center;
  color: rgb(153, 153, 153);
  font-size: 16px;
  margin: 20px 0px 0px;
}
/*发布人*/
.forum_view .forum_detail .forum_info .forum_name {
  margin: 0px 10px 0px 0px;
}
/*时间*/
.forum_view .forum_detail .forum_info .forum_time {
  margin: 0px 10px 0px 0px;
}
/*分割线*/
.forum_view .forum_detail .el-divider--horizontal {
  display: block;
  height: 0px;
  width: 100%;
  margin: 24px 0;
}
/*内容区*/
.forum_view .forum_detail .forum_content {
  width: 100%;
  text-align: left;
  font-size: 16px;
  color: rgb(102, 102, 102);
  text-indent: 2rem;
}

.forum_view .thumbs_view {
  margin: 20px auto 0;
}

/*一级评论 盒子*/
.forum_view .forum_comment_box {
  margin: 40px 0px 0px;
  width: 100%;
  padding: 0px;
  border: 0px solid rgb(238, 238, 238);
  box-shadow: none;
  border-radius: 0px;
}
.forum_view .forum_comment_box .el-card__body {
  padding: 20px;
}
/*列表盒子*/
.forum_view .forum_comment_box .forum_comment_list {
  width: 100%;
  text-align: left;
}
/*item*/
.forum_view .forum_comment_box .forum_comment_list .forum_comment {
  width: 100%;
  padding: 0px;
  border: 1px solid rgb(228, 228, 228);
  margin: 0px 0px 20px;
}
/*用户盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_user {
  display: flex;
  align-items: center;
  background: none;
  padding: 5px 10px;
}
/*头像*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_user
  .forum_comment_user_avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0px 10px 0px 0px;
}
/*用户名*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_user
  .forum_comment_username {
  font-size: 16px;
  color: #333;
}
/*内容*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_content {
  width: calc(100% - 60px);
  margin: 0px 0px 0px 60px;
  padding: 10px 0px 0px;
  font-size: 16px;
}
/*按钮盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_reply_view {
  width: 100%;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0px 20px 0px 0px;
  box-sizing: border-box;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_reply_view
  .forum_comment_del {
  font-size: inherit;
  color: rgb(255, 255, 255);
  cursor: pointer;
  margin: 0px 0px 0px 10px;
  background: rgb(251, 137, 122);
  padding: 5px 10px;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_reply_view
  .forum_comment_reply {
  font-size: inherit;
  color: rgb(255, 255, 255);
  cursor: pointer;
  margin: 0px 0px 0px 10px;
  background: var(--theme);
  padding: 5px 10px;
}

/*二级评论 盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list {
  width: calc(100% - 60px);
  margin: 0px 0px 0px 60px;
  padding: 20px 0px 0px;
}
/*线*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second_divider {
  display: block;
  height: 1px;
  width: 100%;
  margin: 24px 0;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second_divider
  .el-divider__text {
  font-size: 16px;
}
/*item*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second {
  width: 100%;
  padding: 10px 0px;
  border-width: 0px 0px 1px;
  border-style: solid;
  border-color: rgb(238, 238, 238);
  border-image: initial;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second:last-child {
  border-width: 0px 0px 0px;
}

/*用户盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second
  .forum_comment_second_user {
  display: flex;
  align-items: center;
}
/*头像*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second
  .forum_comment_second_user
  .forum_comment_second_user_avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin: 0px 10px 0px 0px;
}
/*用户名*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second
  .forum_comment_second_user
  .forum_comment_second_username {
  font-size: 16px;
}
/*内容*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second
  .forum_comment_second_content {
  width: calc(100% - 60px);
  margin: 0px 0px 0px 60px;
  padding: 10px 0px 0px;
  color: #999;
  font-size: 16px;
}
/*按钮盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second
  .forum_comment_second_reply_view {
  width: 100%;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0px 20px 0px 0px;
  box-sizing: border-box;
}
/*删除*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second
  .forum_comment_second_reply_view
  .forum_comment_second_del {
  font-size: inherit;
  color: rgb(255, 255, 255);
  cursor: pointer;
  margin: 0px 0px 0px 10px;
  background: rgb(251, 137, 122);
  padding: 5px 10px;
}

/*时间*/
.forum_view .forum_list .forum_item .forum_time {
  font-size: 15px;
  color: rgb(153, 153, 153);
  margin: 12px 0px 0px;
}
/*按钮盒子*/
.forum_view .forum_list .forum_item .forum_item_btn_box {
  width: auto;
}
/*修改*/
.forum_view .forum_list .forum_item .forum_item_btn_box .forum_edit_btn {
  margin: 0px 10px 0px 0px;
  padding: 0px 24px;
  width: auto;
  height: 40px;
  font-size: 14px;
  color: rgb(255, 255, 255);
  border-radius: 4px;
  border: 0px;
  background: rgba(254, 176, 67, 0.8);
  cursor: pointer;
}
/*删除*/
.forum_view .forum_list .forum_item .forum_item_btn_box .forum_del_btn {
  margin: 0px 10px 0px 0px;
  padding: 0px 24px;
  width: auto;
  height: 40px;
  font-size: 14px;
  color: rgb(255, 255, 255);
  border-radius: 4px;
  border: 0px;
  background: rgba(254, 66, 66, 0.8);
  cursor: pointer;
}

/*详情盒子*/
.forum_view .forum_detail {
  width: 100%;
  margin: 20px 0px;
}
/*标题*/
.forum_view .forum_detail .forum_title {
  width: 100%;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  color: rgb(0, 0, 0);
}
/*info盒子*/
.forum_view .forum_detail .forum_info {
  width: 100%;
  text-align: center;
  color: rgb(153, 153, 153);
  font-size: 16px;
  margin: 20px 0px 0px;
}
/*发布人*/
.forum_view .forum_detail .forum_info .forum_name {
  margin: 0px 10px 0px 0px;
}
/*时间*/
.forum_view .forum_detail .forum_info .forum_time {
  margin: 0px 10px 0px 0px;
}
/*分割线*/
.forum_view .forum_detail .el-divider--horizontal {
  display: block;
  height: 0px;
  width: 100%;
  margin: 24px 0;
}
/*内容区*/
.forum_view .forum_detail .forum_content {
  width: 100%;
  text-align: left;
  font-size: 16px;
  color: rgb(102, 102, 102);
  text-indent: 2rem;
}

.forum_view .thumbs_view {
  margin: 20px auto 0;
}

/*一级评论 盒子*/
.forum_view .forum_comment_box {
  margin: 40px 0px 0px;
  width: 100%;
  padding: 0px;
  border: 0px solid rgb(238, 238, 238);
  box-shadow: none;
  border-radius: 0px;
}
.forum_view .forum_comment_box .el-card__body {
  padding: 20px;
}
/*列表盒子*/
.forum_view .forum_comment_box .forum_comment_list {
  width: 100%;
  text-align: left;
}
/*item*/
.forum_view .forum_comment_box .forum_comment_list .forum_comment {
  width: 100%;
  padding: 0px;
  border: 1px solid rgb(228, 228, 228);
  margin: 0px 0px 20px;
}
/*用户盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_user {
  display: flex;
  align-items: center;
  background: none;
  padding: 5px 10px;
}
/*头像*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_user
  .forum_comment_user_avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0px 10px 0px 0px;
}
/*用户名*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_user
  .forum_comment_username {
  font-size: 16px;
  color: #333;
}
/*内容*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_content {
  width: calc(100% - 60px);
  margin: 0px 0px 0px 60px;
  padding: 10px 0px 0px;
  font-size: 16px;
}
/*按钮盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_reply_view {
  width: 100%;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0px 20px 0px 0px;
  box-sizing: border-box;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_reply_view
  .forum_comment_del {
  font-size: inherit;
  color: rgb(255, 255, 255);
  cursor: pointer;
  margin: 0px 0px 0px 10px;
  background: rgb(251, 137, 122);
  padding: 5px 10px;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_reply_view
  .forum_comment_reply {
  font-size: inherit;
  color: rgb(255, 255, 255);
  cursor: pointer;
  margin: 0px 0px 0px 10px;
  background: var(--theme);
  padding: 5px 10px;
}

/*二级评论 盒子*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list {
  width: calc(100% - 60px);
  margin: 0px 0px 0px 60px;
  padding: 20px 0px 0px;
}
/*线*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second_divider {
  display: block;
  height: 1px;
  width: 100%;
  margin: 24px 0;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second_divider
  .el-divider__text {
  font-size: 16px;
}
/*item*/
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second {
  width: 100%;
  padding: 10px 0px;
  border-width: 0px 0px 1px;
  border-style: solid;
  border-color: rgb(238, 238, 238);
  border-image: initial;
}
.forum_view
  .forum_comment_box
  .forum_comment_list
  .forum_comment
  .forum_comment_second_list
  .forum_comment_second:last-child {
  border-width: 0px 0px 0px;
}
</style>
