<template>
  <xn-form-container
      title="文件上传"
      :width="550"
      :visible="visible"
      :destroy-on-close="true"
      :bodyStyle="{ 'padding-top': '0px' }"
      @close="onClose"
  >
<!--    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="Local" tab="本地">
        <a-spin :spinning="uploadLoading">
          <a-upload-dragger :show-upload-list="false" :custom-request="customRequestLocal">
            <p class="ant-upload-drag-icon">
              <inbox-outlined></inbox-outlined>
            </p>
            <p class="ant-upload-text">点击单文件到此区域进行上传</p>
            <p class="ant-upload-hint">支持单个上传</p>
          </a-upload-dragger>
        </a-spin>
      </a-tab-pane>
    </a-tabs>-->

    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="Local" tab="本地">
        <a-spin :spinning="uploadLoading">
          <a-upload-dragger
              multiple
              drag
              :show-upload-list="false"
              :before-upload="beforeUpload"
              :custom-request="customRequestLocalAll"
          >
            <p class="ant-upload-drag-icon">
              <a-icon :icon="uploadIcon"/>
            </p>
            <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
            <p class="ant-upload-hint">支持单个或多个上传</p>
          </a-upload-dragger>
        </a-spin>
      </a-tab-pane>
    </a-tabs>

  </xn-form-container>
</template>

<script setup name="uploadForm">
import {filePage, fileList, fileDownload, fileDelete, fileUploadReturnId, fileUploadLocalReturnUrl} from '@/api/file'
import {ref} from "vue";
import {defineExpose} from "vue";
import axios from 'axios'
// 定义emit事件
// eslint-disable-next-line no-undef
const emit = defineEmits({successful: null})
// 默认是关闭状态
const visible = ref(false)
const activeKey = ref('Local')
const uploadLoading = ref(false)

// 打开抽屉
const openUpload = () => {
  visible.value = true
}
// 关闭抽屉
const onClose = () => {
  visible.value = false
  emit('successful')
}
// 上传单文件本地
const customRequestLocal = (data) => {
  uploadLoading.value = true
  const fileData = new FormData()

  fileData.append('file', data.file)

  axios.post('http://localhost:9090/file/uploadLocalReturnUrl', fileData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(() => {
    emit('successful')
  })
      .finally(() => {
        uploadLoading.value = false
        // 刷新一下页面
        window.location.reload()
      })
}


// 上传单多文件到本地
import {UploadOutlined} from '@ant-design/icons-vue';

const uploadIcon = ref(UploadOutlined);

const formData = new FormData();

const beforeUpload = (file) => {
  formData.append('files', file);
  return true; // 返回 true 允许上传，返回 false 不允许上传
};

let flag = ref(true);
const  customRequestLocalAll = ( ) => {
  uploadLoading.value = true
  if (flag.value) {
    axios.post('http://localhost:9090/file/uploadLocalReturnUrlAll', formData, {
      headers: {
        'Content-Type':'multipart/form-data'
      }
    }).then(() => {
      emit('successful')
    })
        .finally(() => {
          uploadLoading.value = false
          // 刷新一下页面
          window.location.reload()
        })
    flag.value = false
  }
}


// 调用这个函数将子组件的一些数据和方法暴露出去
// eslint-disable-next-line no-undef

defineExpose({
  openUpload
})
</script>


