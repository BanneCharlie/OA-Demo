<template>

  <div class="container">

    <div class="depart-div">
      <h2> 部门信息 </h2>

      <el-form-item label="查询部门">
        <el-input
            v-model="pageQueryRequest.departmentName"
            style="width: 100%"
            placeholder="请输入部门名称"
        ></el-input>
      </el-form-item>

      <el-tree
          :data="departTreeList"
          :props="defaultProps"
          :default-expanded-keys="expandedKeys"
          @node-click="handleNodeClick($event)"
      ></el-tree>

    </div>

    <div class="search-div">

      <!---搜索表单-->
      <div class="search-div">
        <el-form label-width="70px" size="small">

          <el-row>
            <el-col :span="8">
              <el-form-item label="账号搜索">
                <el-input
                    v-model="pageQueryRequest.userAccount"
                    style="width: 100%"
                    placeholder="请输入账号"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="2"></el-col>

            <el-col :span="8">
              <el-form-item label="昵称搜索">
                <el-input
                    v-model="pageQueryRequest.userName"
                    style="width: 100%"
                    placeholder="请输入昵称"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="2"></el-col>

            <el-col :span="4">
              <el-form-item label="状态搜索">

                <el-select v-model="pageQueryRequest.status" placeholder="请选择状态">
                  <el-option v-for="item in statusOpetions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>

              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="手机查询">
                <el-input
                    v-model="pageQueryRequest.phone"
                    style="width: 100%"
                    placeholder="请输入正确的11位手机号"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="2"></el-col>

            <el-col :span="10">
              <el-form-item label="创建时间">
                <el-date-picker
                    v-model="createTimes"
                    type="daterange"
                    range-separator="To"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row style="display:flex">
            <el-button type="primary" size="small" @click="searchSysUser">
              搜索
            </el-button>
            <el-button size="small" @click="resetData">重置</el-button>
          </el-row>

        </el-form>
      </div>


      <!-- 添加按钮 -->
      <div class="tools-div">
        <el-button type="success" size="small" @click="addShow">添 加</el-button>
      </div>

      <!-- 添加用户表单对话框-->
      <el-dialog v-model="dialogVisible" title="添加或修改用户" width="30%">
        <el-form label-width="120px">

          <el-form-item label="用户账号">
            <el-input v-model="user.userAccount"/>
          </el-form-item>

          <el-form-item label="用户名称">
            <el-input v-model="user.userName"/>
          </el-form-item>

          <el-form-item label="手机号">
            <el-input v-model="user.phone"/>
          </el-form-item>

          <el-form-item label="用户简介">
            <el-input v-model="user.description"/>
          </el-form-item>


          <el-form-item label="部门名称">

            <el-cascader
                v-model="user.departmentId"
                :options="options"
                :show-all-levels="false"
                :props="props"
                @change="handleChange"
                style="width:380px"
                placeholder="请选择合适的部门"
            />

          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submit">提交</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </el-form-item>

        </el-form>
      </el-dialog>

      <!--- 用户表格数据 -->
      <el-table :data="list" style="width: 100%">
        <el-table-column prop="userAccount" label="用户账号" width="120"/>

        <el-table-column prop="userName" label="用户昵称" width="120"/>

        <el-table-column prop="phone" label="手机号" width="180"/>

        <el-table-column prop="departmentName" label="部门名称" width="180"/>

        <el-table-column prop="description" label="描述"/>

        <el-table-column prop="status" label="状态" #default="scope">
          <el-switch
              v-model="scope.row.status"
              @change="changeNewStatus(scope.row)"
          />
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" :formatter="formatDate" />

        <el-table-column prop="updateTime" label="更新时间" :formatter="formatDate"/>

        <el-table-column label="操作" align="center" width="280" #default="scope">

          <el-button type="primary" size="small" @click="editShow(scope.row)">修改</el-button>

          <el-button type="danger" size="small" @click="deleteById(scope.row)">
            删除
          </el-button>

          <el-button v-if="canAssignRole" type="warning" size="small" @click="showAssignRole(scope.row)">
            分配角色
          </el-button>

        </el-table-column>

      </el-table>

      <!--  分配角色对话框    -->
      <el-dialog v-model="dialogRoleVisible" title="分配角色" width="40%">
        <el-form label-width="80px">
          <el-form-item label="用户名">
            <el-input disabled :value="user.userName"></el-input>
          </el-form-item>

          <el-form-item label="角色列表">
            <el-checkbox-group v-model="userRoleIds">
              <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">
                {{ role.roleName }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="doAssign">提交</el-button>
            <el-button @click="dialogRoleVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <!--分页条-->
      <el-pagination
          v-model:current-page="pageQueryRequest.page"
          v-model:page-size="pageQueryRequest.pageSize"
          :page-sizes="[2,5, 8, 20, 50, 100]"
          @size-change="fetchData"
          @current-change="fetchData"
          layout="->, jumper, prev, pager, next, sizes, total"
          :total="total"
      ></el-pagination>

    </div>

  </div>

</template>

<script setup>
import {ref, onMounted} from "vue";
import {
  GetSysUserListByPage,
  SaveSysUser,
  UpdateSysUser,
  GetSysUserById,
  DeleteSysUser,
  DoAssignUserRole
} from "@/api/sysUser";
// 根据 id 获取部门信息
import {GetSysDepartById, GetAllSysDepartList, FindNodes} from "@/api/sysDepartment";
// 查询所有角色信息
import {GetAllRoleList} from "@/api/sysRole";

import {GetSysUserListByDepartId, GetRoleIdByUserId, UpdateSysUserStatus} from "@/api/sysUser";

import {ElMessage, ElMessageBox, ElTree} from "element-plus";
import "element-plus/dist/index.css";
/*
 * 用户管理页面  实现查询操作
 */

// 分页条总记录数
let total = ref(0);

// 定义用户表格数据模型
let list = ref([]);

// 定义属性结构的页面展示
let departTreeList = ref([]);

// 需要展示的数据
let defaultProps = {
  children: "children",
  parentId: "parentId",
  id: "id",
  label: "departmentName"
};

// 定义部门表格数据信息
let departList = ref([]);

// 接收 传递的数据
const options = ref([{
  id: "",
  departmentName: "",
  parentId: "",
  createTime: "",
  children: ""
}]);

const value = ref([]);

const props = {
  value: "id",
  label: "departmentName",
  children: "children",
  expandTrigger: "hover"
};

// 搜索表单数据
let pageQuery = {
  page: 1,
  pageSize: 8,
  userName: "",
  userAccount: "",
  phone: "",
  status: "",
  createTimeBegin: '',
  createTimeEnd: '',
  departmentName: "",
};

const createTimes = ref([])

const pageQueryRequest = ref(pageQuery);

const statusOpetions = ref([
  {
    value: "1",
    label: "启用"
  },
  {
    value: "2",
    label: "禁用"
  }
]);

const handleStatusChange = (value) => {
  pageQueryRequest.value.status = value[0];
}

// 页面加载完毕以后请求后端接口获取数据
onMounted(() => {
  fetchData();
  fetchDataTree();
  fetchRoleId();
});

// 搜索按钮点击事件处理函数
const searchSysUser = () => {
  fetchData();
  fetchDataTree();
  fetchRoleId();
};

// 实现重置按钮
const resetData = () => {
  pageQueryRequest.value.userAccount = "",
      pageQueryRequest.value.phone = "",
      pageQueryRequest.value.userName = "",
      pageQueryRequest.value.status = "",
      pageQueryRequest.value.departmentName = "",
      createTimes.value = []
  pageQueryRequest.value.createTimeBegin = "",
      pageQueryRequest.value.createTimeEnd = ""
  fetchData();
  fetchDataTree();
  fetchRoleId();
};
// 远程调用后端分页查询接口
const fetchData = async () => {
  console.log(createTimes.value.length)
  if (createTimes.value.length == 2) {
    pageQueryRequest.value.createTimeBegin = createTimes.value[0]
    pageQueryRequest.value.createTimeEnd = createTimes.value[1]
  }
  const {
    data,
    code,
    message
  } = await GetSysUserListByPage(pageQueryRequest.value);

  // 遍历 data.list 数组 将它的  status为1的设置为 true
  data.list.forEach((item) => {
    item.status = item.status == 1 ? true : false;
  });
  list.value = data.list;
  total.value = data.total;
};

const fetchDataTree = async () => {
  const {
    data,
    code,
    message
  } = await FindNodes();
  departTreeList.value = data;
  options.value = data;
};

// 设置时间的格式
const formatDate = (row,column,cellValue) =>{
   if(cellValue){
     return cellValue.split(' ')[0]
   }
   return '';;
}

const canAssignRole = ref(false);

const fetchRoleId = async () => {
  const {
    data,
    code,
    message
  } = await GetRoleIdByUserId();
  if (data && Array.isArray(data)) {
    if (data.includes(3129568305489920)) {
      canAssignRole.value = true;
    }
  }
};

// 实现 部门树的查询
const handleNodeClick = async (event) => {
  const {data, code, message}
      = await GetSysUserListByDepartId(event.id, event.parentId);
  // 遍历 data.list 数组 将它的  status为1的设置为 true
  data.forEach((item) => {
    item.status = item.status == 1 ? true : false;
  });
  list.value = data;
};

/*
 * 添加 或 修改用户对话框
 */

// 修改用户的状态
const changeNewStatus = async (value) => {
  let userId = value.id;
  let status = value.status ? 1 : 2;

  const {data, code, message} = await UpdateSysUserStatus(userId, status);
  if (code == 200) {
    ElMessage.success("状态操作成功");
    fetchData();
  } else {
    ElMessage.error("状态操作失败");
  }
};

// 控制对话是否展示的变量
const dialogVisible = ref(false);

// 定义表单数据
const user = ref({
  id: "",
  userName: "",
  departmentId: "",
  departmentName: "",
  userAccount: "",
  phone: "",
  description: ""
});

//进入添加
const addShow = () => {
  user.value = {}
  dialogVisible.value = true;
};

// 修改操作  进行数据的回显  删除和修改 绑定在一起 执行一般
const editShow = (row) => {
  user.value = row;
  // 根据用户 id 获取到用户信息
  GetSysUserById(row.id).then(res => {
    user.value.departmentId = res.data.departmentId;
  });
  dialogVisible.value = true;
};

// 将 user.value 转换为需要的数据形式
let userForm = ref({
  id: "",
  userName: "",
  departmentId: "",
  departmentName: "",
  userAccount: "",
  phone: "",
  description: ""
});

// 实现角色的添加/修改
const submit = async () => {
  // 进行一次转换
  userForm.value.id = user.value.id;
  userForm.value.userName = user.value.userName;
  userForm.value.userAccount = user.value.userAccount;
  userForm.value.phone = user.value.phone;
  userForm.value.description = user.value.description;
  userForm.value.departmentName = user.value.departmentName;
  userForm.value.departmentId = user.value.departmentId[user.value.departmentId.length - 1];

  if (!userForm.value.id) {
    const {
      data, code, message
    } = await SaveSysUser(userForm.value);
    if (code == 200) {
      dialogVisible.value = false;
      ElMessage.success("操作成功");
      fetchData();
    } else {
      ElMessage.error("操作失败");
    }
  } else {
    const {
      data, code, message
    } = await UpdateSysUser(userForm.value);
    if (code == 200) {
      dialogVisible.value = false;
      ElMessage.success("操作成功");
      fetchData();
    } else {
      ElMessage.error("操作失败");
    }
  }
};


// 删除操作
const deleteById = (row) => {
  ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "Warning", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    const {code} = await DeleteSysUser(row.id);

    if (code == 200) {
      ElMessage.success("删除成功");
      pageQueryRequest.value.page = 1;
      fetchData();
    }
  });
};

// 角色列表  分配角色
const userRoleIds = ref([]);
const allRoles = ref([]);
const dialogRoleVisible = ref(false);
const showAssignRole = async row => {
  user.value = row;
  dialogRoleVisible.value = true;

  // 查询所有的角色数据
  const {code, message, data} = await GetAllRoleList(row.id);

  // 获取所有的角色信息
  allRoles.value = data.allRoleList;

  // 获取当前登录用户的角色数据
  userRoleIds.value = data.roleIdList;
};

// 实角色的分配
// 角色分配按钮事件处理函数
const doAssign = async () => {
  let assginRoleVo = {
    userId: user.value.id,
    roleIdList: userRoleIds.value
  };
  const {code, message, data} = await DoAssignUserRole(assginRoleVo);
  if (code == 200) {
    ElMessage.success("操作成功");
    dialogRoleVisible.value = false;
    fetchData();
  }
};

</script>

<style scoped>

.container {
  display: flex;
  height: 100%;
}

.depart-div {
  width: calc(100% / 6);
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}

.search-div {
  flex: 1;
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
