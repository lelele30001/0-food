<template>
    <div class="list_view">
        <div class="list_search">
            <div class="search_inner">
                <el-input v-model="queryParams.title" placeholder="食谱标题" style="width: 300px" clearable>
                    <template #append>
                        <el-button @click="handleQuery">
                            <el-icon><Search /></el-icon>
                        </el-button>
                    </template>
                </el-input>
                <el-button type="primary" @click="handleAdd">
                    <el-icon><Plus /></el-icon> 发布食谱
                </el-button>
            </div>
        </div>
        <div class="list_table">
            <el-table v-loading="loading" :data="recipeList" style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="title" label="食谱标题" min-width="200" />
                <el-table-column prop="fitnessGoal" label="健身目标" width="100" />
                <el-table-column prop="mealScene" label="用餐场景" width="100" />
                <el-table-column prop="totalCalories" label="总热量(kcal)" width="120" />
                <el-table-column prop="createTime" label="发布时间" width="180" />
                <el-table-column label="操作" width="150" fixed="right">
                    <template #default="scope">
                        <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    v-model:current-page="queryParams.page"
                    v-model:page-size="queryParams.limit"
                    :page-sizes="[10, 20, 50, 100]"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                />
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import { useRouter } from 'vue-router';
import { Search, Plus } from '@element-plus/icons-vue';
const context = getCurrentInstance()?.appContext.config.globalProperties;
const router = useRouter();

// 加载状态
const loading = ref(false);
// 食谱列表
const recipeList = ref([]);
// 总条数
const total = ref(0);
// 查询参数
const queryParams = ref({
    page: 1,
    limit: 10,
    title: ''
});

// 查询食谱列表
const getRecipeList = () => {
    loading.value = true;
    context?.$http({
        url: 'recipe/public',
        method: 'get',
        params: queryParams.value
    }).then(res => {
        loading.value = false;
        if (res.data.code === 0) {
            recipeList.value = res.data.data.recipes || [];
            total.value = recipeList.value.length;
        }
    });
};

// 搜索
const handleQuery = () => {
    queryParams.value.page = 1;
    getRecipeList();
};

// 重置
const resetQuery = () => {
    queryParams.value = {
        page: 1,
        limit: 10,
        title: ''
    };
    getRecipeList();
};

// 分页
const handleSizeChange = (size) => {
    queryParams.value.limit = size;
    getRecipeList();
};

const handleCurrentChange = (current) => {
    queryParams.value.page = current;
    getRecipeList();
};

// 新增
const handleAdd = () => {
    router.push('/pages/recipe/publish');
};

// 编辑
const handleEdit = (row) => {
    // 编辑功能待实现
    context?.$toolUtil.message('编辑功能待实现', 'info');
};

// 删除
const handleDelete = (id) => {
    context?.$toolUtil.confirm('确定要删除这条食谱吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        // 删除功能待实现
        context?.$toolUtil.message('删除功能待实现', 'info');
    });
};

// 初始化
onMounted(() => {
    getRecipeList();
});
</script>
<style lang="scss" scoped>
.list_view {
    width: 1200px;
    margin: 20px auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.list_search {
    margin-bottom: 20px;
    .search_inner {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
}

.list_table {
    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: flex-end;
    }
}
</style>