<template>

  <!-- 实现部门的添加  -->
  <div class="tools-div">
    <el-button type="success" size="small" @click="addShow">添 加</el-button>
  </div>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="30%">
    <el-form label-width="120px">

      <el-form-item label="部门标题">
        <el-input v-model="sysDepart.departmentName"/>
      </el-form-item>

      <!-- 关于部门的信息通过 下拉框的形式进行展示 将动态展示 managerList中的数据 -->
      <el-form-item label="部门经理">
        <el-select v-model="sysDepart.manager" placeholder="请选择合适的经理"  style="width: 420px">
          <el-option
              v-for="item in managerList"
              :key="item.id"
              :label="item.userName"
              :value="item.userName"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="部门描述">
        <el-input v-model="sysDepart.description" type="textarea" :rows="6" style="max-height:500px"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="saveOrUpdate">提交</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>

    </el-form>
  </el-dialog>


  <el-table
      :data="list"
      style="width: 100%; margin-bottom: 20px"
      row-key="id"
      border
      default-expand-all
  >
    <el-table-column prop="departmentName" label="部门名称"/>

    <el-table-column prop="manager" label="部门经理"/>

    <el-table-column prop="description" label="部门描述">
      <template #default="scope">
        <div class="cell-content">{{ scope.row.description }}</div>
      </template>
    </el-table-column>

    <el-table-column prop="createTime" label="创建时间"/>

    <el-table-column prop="updateTime" label="更新时间"/>

    <el-table-column label="操作" align="center" width="280" #default="scope">
      <el-button type="success" size="small" @click="addShow(scope.row)">
        添加下级节点
      </el-button>
      <el-button type="primary" size="small" @click="editShow(scope.row)">
        修改
      </el-button>
      <el-button type="danger" size="small" @click="remove(scope.row.id)">
        删除
      </el-button>
    </el-table-column>

  </el-table>

</template>

<script setup>
//引入调用的方法
import {ref, onMounted} from "vue"
import {GetSysUserListNotForbidden} from '@/api/sysUser'
import {FindNodes, SaveSysDepart, UpdateSysDepart, DeleteSysDepart} from '@/api/sysDepartment'
import {ElMessage, ElMessageBox} from 'element-plus'

// 定义表格数据模型
const list = ref([])

// 定义添加表单菜单表单相关数据模型
const dialogTitle = ref('添加')
const dialogVisible = ref(false)

// 查询存在的用户信息
const managerList = ref([])

//页面表单数据
const defaultForm = {
  id: '',
  parentId: 0,
  departmentName: '',
  manager: '',
  description: '',
}
// 表单响应式数据模型对象
const sysDepart = ref(defaultForm)

//=======================加载数据=========================
onMounted(() => {
  fetchData()
  fetchUserData()
})

//=======================分页列表====================
const fetchData = async () => {
  const {code, data, message} = await FindNodes()
  list.value = data
}

const fetchUserData = async () => {
  const {code, data, message} = await GetSysUserListNotForbidden();
  managerList.value = data
}


//=======================添加和修改功能====================
//进入添加
const addShow = (row) => {
  sysDepart.value = {}
  dialogVisible.value = true
  if (!row.id) {
    dialogTitle.value = '添加'
  } else {
    dialogTitle.value = '添加下级节点'
    sysDepart.value.parentId = row.id
  }
}

//进入修改
const editShow = row => {
  sysDepart.value = row
  dialogVisible.value = true
}

//提交保存与修改
const saveOrUpdate = () => {

  console.log("添加的部门信息", sysDepart.value)

  if (!sysDepart.value.id) {
    if (!sysDepart.value.parentId) {
      sysDepart.value.parentId = 0
    }
    saveData()
  } else {
    updateData()
  }
}

// 修改
const updateData = async () => {
  await UpdateSysDepart(sysDepart.value)
  dialogVisible.value = false
  ElMessage.success('操作成功')
  fetchData()
}

// 新增
const saveData = async () => {
  await SaveSysDepart(sysDepart.value)
  dialogVisible.value = false
  ElMessage.success('操作成功')
  fetchData()
}


//=======================删除功能====================
const remove = async id => {
  console.log('removeDataById:' + id)
  ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    const {code, message} = await DeleteSysDepart(id)
    if (code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(message)
    }
  })
}
</script>

<style scoped>
.search-div {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}

.tools-div {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}

.cell-content {
  max-height: 150px; /* 设置最大高度 */
  overflow: auto; /* 如果内容超出最大高度，显示滚动条 */
}
</style>



