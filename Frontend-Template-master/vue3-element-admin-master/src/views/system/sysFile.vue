<template>
  <div v-if="indexShow">
    <el-card :body-style="{ padding: '20px 10px' }" class="xn-mb10">
      <el-form
          ref="searchFormRef"
          :model="searchFormState"
          label-position="right"
          label-width="100px"
      >
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="名称关键词" prop="searchKey">
              <el-input size="small" v-model="searchFormState.fileName" placeholder="请输入文件名称关键词"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="文件后缀" prop="fileSuffix">

              <el-select size="small" v-model="searchFormState.fileSuffix" placeholder="请选择文件类型">
                <el-option v-for="item in fileTypes" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>

            </el-form-item>
          </el-col>
        </el-row>

        <el-row >
          <el-col :span="6">
            <el-form-item label="创建者名称" prop="createUser">
              <el-select size="small" v-model="searchFormState.createUser" placeholder="请选择创建者名称">
                <el-option v-for="item in userList" :key="item.id" :label="item.userName" :value="item.userName">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-button  size="small" type="primary" @click="searchData">
              <i class="el-icon-search"></i>
              查询
            </el-button>
            <el-button size="small" class="snowy-button-left" @click="reset">
              <i class="el-icon-refresh"></i>
              重置
            </el-button>
          </el-col>
        </el-row>

      </el-form>
    </el-card>

    <!-- 文件上传按钮 -->
    <div class="tools-div">
      <el-space>
        <el-button type="primary" @click="uploadFile">
          <i class="el-icon-upload2"></i>
          文件上传
        </el-button>
      </el-space>
    </div>

    <el-dialog v-model="isUploadFile" title="上传文件页面"
               width="40%"
               custom-class="right-dialog">
      <UploadForm ref="uploadFormRef"> </UploadForm>
    </el-dialog>


    <!--  进行文件内容的展示   -->
    <el-card :body-style="{ padding: '0' }">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="name" label="文件名称" width="380"/>

        <el-table-column prop="storagePath" label="缩略图" width="180">
          <template v-slot="{row}">
            <img
                :src="row.thumbnail"
                class="record-img"
                v-if="
								row.suffix === 'png' ||
								row.suffix === 'jpg' ||
								row.suffix === 'jpeg' ||
								row.suffix === 'ico' ||
								row.suffix === 'bmp' ||
								row.suffix === 'gif'
							"
            />
            <img
                src="/src/assets/images/fileImg/docx.png"
                class="record-img"
                v-else-if="row.suffix === 'doc' || row.suffix === 'docx'"
            />
            <img
                src="/src/assets/images/fileImg/xlsx.png"
                class="record-img"
                v-else-if="row.suffix === 'xls' ||row.suffix === 'xlsx'"
            />
            <img src="/src/assets/images/fileImg/zip.png" class="record-img" v-else-if="row.suffix === 'zip'"/>
            <img src="/src/assets/images/fileImg/rar.png" class="record-img" v-else-if="row.suffix === 'rar'"/>
            <img
                src="/src/assets/images/fileImg/ppt.png"
                class="record-img"
                v-else-if="row.suffix === 'ppt' || row.suffix === 'pptx'"
            />
            <img src="/src/assets/images/fileImg/pdf.png" class="record-img" v-else-if="row.suffix === 'pdf'"/>
            <img src="/src/assets/images/fileImg/txt.png" class="record-img" v-else-if="row.suffix === 'txt'"/>
            <img src="/src/assets/images/fileImg/html.png" class="record-img" v-else-if="row.suffix === 'html'"/>
            <img src="/src/assets/images/fileImg/file.png" class="record-img" v-else/>
          </template>
        </el-table-column>

        <el-table-column prop="sizeInfo" label="文件大小" width="180"/>

        <el-table-column prop="suffix" label="文件后缀" width="180"/>

        <el-table-column prop="engine" label="存储引擎" width="180"/>

        <el-table-column prop="createUser" label="创建者"/>

        <el-table-column label="操作" align="center" width="280" #default="scope">

          <el-button type="primary" size="small" @click="previewShow(scope.row)">预览</el-button>

          <el-button type="primary" size="small" @click="detailShow(scope.row)">详情</el-button>

          <el-button type="info" size="small" @click="downloadFile(scope.row)">
            下载
          </el-button>

          <el-button type="danger" size="small" @click="deleteById(scope.row)">
            删除
          </el-button>

        </el-table-column>

      </el-table>
    </el-card>

    <!-- 详情页面展示 -->
    <el-dialog v-model="isDetailFile" title="文件详情页面">
      <detail ref="detailRef" :currentData="currentData"/>
    </el-dialog>


    <el-pagination
        v-model:current-page="searchFormState.page"
        v-model:page-size="searchFormState.pageSize"
        @size-change="fetchData"
        @current-change="fetchData"
        :page-sizes="[5, 8, 20, 50, 100]"
        layout="->, jumper, prev, pager, next, sizes, total"
        :total="total"
    ></el-pagination>

  </div>

</template>

<script setup name="devFile">
import {
  fileUploadReturnId,
  fileUploadLocalReturnUrl,
  fileDelete,
  filePage,
  fileDownload,
  fileList,
  fileDetail
} from "@/api/file";
import {UploadOutlined} from '@ant-design/icons-vue';
//导入 axios配置
import axios from 'axios';
import UploadForm from './file/uploadForm.vue'
import Detail from './file/detail.vue'
import Preview from './file/preview.vue'
import tool from '@/utils/tool'
import {nextTick, onMounted, ref, defineProps} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

import {Base64} from 'js-base64'

import {DeleteSysUser, GetSysUserListNotForbidden} from "@/api/sysUser";
import "element-plus/dist/index.css";
import fs from "fs";

// 定义tableDOM
const tableRef = ref()
const formRef = ref()
const searchFormRef = ref()
const uploadFormRef = ref()
const detailRef = ref()
const previewRef = ref()
const indexShow = ref(true)

const  list = ref([])

const  total = ref(0)
const searchFormState = ref({
  page: 1,
  pageSize: 8,
  fileName:'',
  fileSuffix:'',
  createUser:'',
})

// 定义文件后缀的数据
const fileTypes = ref( [
  { value: 'png', label: 'png图片文件' },
  { value: 'gif', label: 'gif图片文件' },
  { value: 'jpg', label: 'jpg图片文件' },
  { value: 'pdf', label: 'PDF 文件' },
  { value: 'doc', label: 'Word 文件' },
  { value: 'xls', label: 'Excel 文件' },
  { value: 'ppt', label: 'PPT 文件' },
  { value: 'txt', label: '文本文件' },
])

// 定义创建者的数据
const userList = ref([])
const fetchUserData = async () => {
  const {code, data, message} = await GetSysUserListNotForbidden();
  userList.value = data
}
const columns = [
  // 表格列配置
  {
    title: '文件名称',
    dataIndex: 'name',
    ellipsis: true
  },
  {
    title: '缩略图',
    dataIndex: 'thumbnail',
    ellipsis: true,
    width: 80
  },
  {
    title: '文件大小',
    dataIndex: 'sizeInfo',
    ellipsis: true,
    width: 120
  },
  {
    title: '文件后缀',
    dataIndex: 'suffix',
    ellipsis: true,
    width: 120
  },
  {
    title: '储存引擎',
    dataIndex: 'engine',
    ellipsis: true,
    width: 120
  },
  {
    title: '操作',
    dataIndex: 'action',
    align: 'center',
    width: 220
  }
]

const selectedRowKeys = ref([])
// 列表选择配置
const options = {
  alert: {
    show: false,
    clear: () => {
      selectedRowKeys.value = ref([])
    }
  },
  rowSelection: {
    onChange: (selectedRowKey, selectedRows) => {
      selectedRowKeys.value = selectedRowKey
    }
  }
}

// 展示表单数据
const tableData = ref([]);
// 表格查询 返回 Promise 对象
const fetchData = async () => {
  const {data, code, message} = await filePage(searchFormState.value);
  tableData.value = data.list;
  total.value = data.total;
}

onMounted(
    () => {
      fetchData();
      fetchUserData();
    }
)

// 点击查询按钮
const searchData = async () => {
    fetchData()
}

// 重置
const reset = () => {
  searchFormRef.value.resetFields()
  searchFormState.value.fileName = '';
  fetchData()
}


const previewData = ref();

// 文件预览
const isPreviewFile = ref(false);

const previewShow = (record) => {
  isPreviewFile.value = true;
  previewData.value = record;
  // fullfilename必须存在 要预览文件的访问地址
  var url = previewData.value.downloadPath + '&fullfilename=' + previewData.value.name;

  // 弹出提示框
  alert('是否要跳转到预览页面');

  // 打开新窗口
  window.open('http://127.0.0.1:8012/onlinePreview?url=' +
      encodeURIComponent(
          Base64.encode(url)
      ));
}

// 文件详情页面
const isDetailFile = ref(false);

const currentData = ref();
const detailShow = (record) => {
  isDetailFile.value = true;
  currentData.value = record;
}


// 文件的上传
const isUploadFile = ref(false);

const uploadFile = () => {
  isUploadFile.value = true;
}




// 文件的下载
const downloadFile = async (record) => {
  try {
    // 下载地址为  localhost:9091/file/download
    const response = await fetch(record.downloadPath);
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${record.name}.${record.suffix}`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('下载失败:', error);
  }
}


// 进行文件的删除
const deleteById = (record) => {
  // 删除逻辑
  ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "Warning", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    const {code} = await fileDelete(record.id);
    if (code == 200) {
      ElMessage.success("删除成功");
      // 刷新页面
      searchData()
    }
  });
}


// 存储位置
const engineOptions = tool.dictList('FILE_ENGINE')
</script>

<style scoped>
.record-img {
  width: 40px;
  height: 40px;
}

.ant-form-item {
  margin-bottom: 0 !important;
}

.snowy-button-left {
  margin-left: 8px;
}

.tools-div {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}

</style>
