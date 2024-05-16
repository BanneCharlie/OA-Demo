<template>
  <div class="search-div">
    <!-- 搜索表单 -->
    <el-form label-width="70px" size="small">
      <el-form-item label="角色名称">
        <el-input
          v-model="pageRoleQueryRequest.roleName"
          style="width: 100%"
          placeholder="角色名称"
        ></el-input>
      </el-form-item>
      <el-row style="display:flex">
        <el-button type="primary" size="small" @click="searchSysRole">
          搜索
        </el-button>
        <el-button size="small" @click="resetData">重置</el-button>
      </el-row>
    </el-form>

    <!-- 添加按钮 -->
    <div class="tools-div">
      <el-button type="success" size="small" @click="addShow">添 加</el-button>
    </div>

    <!-- 添加角色表单对话框 -->
    <el-dialog v-model="dialogVisible" title="添加或修改角色" width="30%">
      <el-form label-width="120px">

        <el-form-item label="角色名称">
          <el-input v-model="role.roleName" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submit">提交</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

    <!--- 角色表格数据 -->
    <el-table :data="list" style="width: 100%">
      <el-table-column prop="roleName" label="角色名称" width="180" />
      <el-table-column prop="createTime" label="创建时间" />

        <el-table-column label="操作" align="center" width="280" #default="scope">

          <el-button type="primary" size="small" @click="editShow(scope.row)">修改</el-button>

          <el-button type="danger" size="small"  @click="deleteById(scope.row)" >
            删除
          </el-button>

          <el-button type="warning" size="small" @click="showAssignMenu(scope.row)">
            分配菜单
          </el-button>

        </el-table-column>

    </el-table>

    <el-dialog v-model="dialogMenuVisible" title="分配菜单" width="40%">

      <el-form label-width="80px">
        <el-tree
          :data="sysMenuTreeList"
          ref="tree"
          show-checkbox
          default-expand-all
          :check-on-click-node="true"
          node-key="id"
          :props="defaultProps"
        />

        <el-form-item>
          <el-button type="primary" @click="doAssign">提交</el-button>
          <el-button @click="dialogMenuVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--分页条-->
    <el-pagination
        v-model:current-page="pageRoleQueryRequest.page"
        v-model:page-size="pageRoleQueryRequest.pageSize"
        :page-sizes="[5, 8, 20, 50, 100]"
        @size-change="fetchData"
        @current-change="fetchData"
        layout="->, jumper, prev, pager, next, sizes, total"
        :total="total"
    />

  </div>

</template>

<script setup>
import { ref, onMounted } from "vue";
import { GetSysRoleListByPage, SaveSysRole ,UpdateSysRole ,DeleteSysRole,DoAssignRoleMenu} from "@/api/sysRole";

import {GetMenuListByRoleId} from "@/api/sysMenu"

import { ElMessage, ElMessageBox } from 'element-plus'
import { handleCurrentChange } from "element-plus/lib/components/tree/src/model/util";

/*
 * 角色管理页面  实现查询操作
 */

// 分页条总记录数
let total = ref(0);

// 定义表格数据模型
let list = ref([]);


// 搜索表单数据
let pageRoleQuery = {
  page: 1,
  pageSize: 8,
  roleName: ""
};

const pageRoleQueryRequest = ref(pageRoleQuery);

// 页面加载完毕以后请求后端接口获取数据
onMounted(() => {
  fetchData();
});

// 搜索按钮点击事件处理函数
const searchSysRole = () => {
  fetchData();
};

// 实现重置按钮
const resetData = () => {
  pageRoleQueryRequest.value.roleName = "";
  fetchData();
};
// 远程调用后端分页查询接口
const fetchData = async () => {
  const {
    data,
    code,
    message
  } = await GetSysRoleListByPage(pageRoleQueryRequest.value);
  list.value = data.list;
  total.value = data.total;
};

/*
 * 添加 或 修改角色对话框
 */
// 控制对话是否展示的变量
const dialogVisible = ref(false);

// 定义表单数据
const role = ref({
  id:"",
  roleName: ""
});

//进入添加
const addShow = () => {
  dialogVisible.value = true;
};

// 实现角色的添加
const submit = async () => {
  if(!role.value.id){
    const {
      data, code, message
    } = await SaveSysRole(role.value);
    if (code == 200) {
      dialogVisible.value = false;
      ElMessage.success("操作成功");
      fetchData();
    }
  }else{
    const {
      data, code, message
    } = await UpdateSysRole(role.value);
    if (code == 200) {
      dialogVisible.value = false;
      ElMessage.success("操作成功");
      fetchData();
    }
  }


};

// 修改操作  进行数据的回显  删除和修改 绑定在一起 执行一般
const editShow = (row) => {
  role.value = row
  dialogVisible.value = true
}

// 删除操作
const deleteById = (row) => {
  ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    const {code } = await DeleteSysRole(row.id)

    if(code == 200) {
      ElMessage.success('删除成功')
      pageRoleQueryRequest.value.page = 1
      fetchData()
    }
  })
}

// 实现菜单的分配
const defaultProps = {
  children: 'children',
  label: 'title',
}

const dialogMenuVisible = ref(false)

const sysMenuTreeList = ref([])

// 存储已经选中的节点 key
const checkedKeys = ref([])

// 存储半选中的节点 key
const indeterminateKeys = ref([])

// 树对象变量
const tree = ref()

// 默认选中的菜单数据集合
let roleId = ref()
const showAssignMenu = async row => {
  dialogMenuVisible.value = true
  roleId = row.id
  const { data } = await GetMenuListByRoleId(row.id)// 请求后端地址获取所有的菜单数据，以及当前角色所对应的菜单数据

  sysMenuTreeList.value = data.menuList

  tree.value.setCheckedKeys(data.roleMenuIds)   // 进行数据回显
}



// 点击提交按钮实现菜单的分配
const doAssign = async () => {
  const checkedNodes = tree.value.getCheckedNodes() ; // 获取选中的节点
  const checkedNodesIds = checkedNodes.map(node => {  // 获取选中的节点的id
    return {
      id: node.id,
      isHalf: 0
    }
  })

  // 获取半选中的节点数据，当一个节点的子节点被部分选中时，该节点会呈现出半选中的状态
  const halfCheckedNodes = tree.value.getHalfCheckedNodes() ;
  const halfCheckedNodesIds = halfCheckedNodes.map(node => {   // 获取半选中节点的id
    return {
      id: node.id,
      isHalf: 1
    }
  })

  // 将选中的节点id和半选中的节点的id进行合并
  const menuIds = [...checkedNodesIds , ...halfCheckedNodesIds]
  console.log(menuIds);

  // 构建请求数据
  const assignMenuDto = {
    roleId: roleId,
    menuIdList: menuIds
  }

  // 发送请求
  await DoAssignRoleMenu (assignMenuDto) ;
  ElMessage.success('操作成功')
  dialogMenuVisible.value = false
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

</style>