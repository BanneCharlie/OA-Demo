<template>
  <a-upload-dragger
      multiple
      drag
      :show-upload-list="false"
      :before-upload="beforeUpload"
      @change="handleChange"
  >
    <p class="ant-upload-drag-icon">
      <a-icon :icon="uploadIcon" />
    </p>
    <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
  </a-upload-dragger>
</template>

<script>
import { ref } from 'vue';
import { UploadOutlined } from '@ant-design/icons-vue';
//导入 axios配置
import axios from 'axios';

export default {
  name: 'FileUploader',
  setup() {
    const uploadIcon = ref(UploadOutlined);

    const beforeUpload = (file) => {
      console.log('Before Upload:', file);
      return true; // 返回 true 允许上传，返回 false 不允许上传
    };

    const handleChange = (info) => {
      console.log('Upload Change:', info);
      // 在这里可以处理上传的文件信息，如发送到后端保存等操作
      const fileList = info.fileList.map(file => file.originFileObj);
      // 此时fileList为原生的File对象数组，可以通过FormData对象上传
      const formData = new FormData();
      fileList.forEach(file => {
        formData.append('files', file);
      });

      // 发送文件到后端
      // 使用fetch或者axios发送POST请求到后端的上传接口
      // 此处以fetch为例
      axios.post('http://localhost:9090/file/uploadLocalReturnUrlAll', formData,{
         headers: {
          'Content-Type':'multipart/form-data'
        }
      }).then(response => {
            if (response.ok) {
              console.log('Files uploaded successfully');
              // 可以在这里处理上传成功的逻辑，如显示成功提示等
            } else {
              console.error('Failed to upload files');
              // 可以在这里处理上传失败的逻辑，如显示错误提示等
            }
          })
          .catch(error => {
            console.error('Failed to upload files:', error);
            // 可以在这里处理上传失败的逻辑，如显示错误提示等
          });
    };

    return {
      uploadIcon,
      beforeUpload,
      handleChange,
    };
  },
};
</script>

<style scoped>
.ant-upload-drag-icon {
  font-size: 32px;
  color: #1890ff;
}
.ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
