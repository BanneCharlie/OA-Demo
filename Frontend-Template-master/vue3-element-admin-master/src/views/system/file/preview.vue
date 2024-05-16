<!--
<template>
&lt;!&ndash;  @goBack="emit('goBack')"&ndash;&gt;
&lt;!&ndash;
  <xn-file-preview :src="src"  :file-type="fileType"  />
&ndash;&gt;

&lt;!&ndash;  parentData.currentData.suffix 内容为 png jpg jepg ico bnp gif 通过图片进行展示&ndash;&gt;
  <div v-if = "parentData.currentData.suffix == 'jpg'
  || parentData.currentData.suffix == 'jpeg'
  || parentData.currentData.suffix == 'png'
  || parentData.currentData.suffix == 'gif'
  || parentData.currentData.suffix == 'ico'
  || parentData.currentData.suffix == 'bnp'
  || parentData.currentData.suffix == 'jepg'
  || parentData.currentData.suffix == 'bmp'
  || parentData.currentData.suffix == 'webp'
  || parentData.currentData.suffix == 'tif'
  || parentData.currentData.suffix == 'tiff'
  || parentData.currentData.suffix == 'psd'
  || parentData.currentData.suffix == 'raw'
  || parentData.currentData.suffix == 'arw'
  || parentData.currentData.suffix == 'cr2' ">
&lt;!&ndash;    <el-image-viewer :src="parentData.currentData.storagePath" alt="显示图片的类型" />&ndash;&gt;

    <el-image-viewer v-model="parentData.currentData.thumbnail" alt="显示图片的类型" />

  </div>


  文件存放的本地地址: {{parentData.currentData.storagePath}}

  文件的后缀 : {{parentData.currentData.suffix}}
</template>

<script setup name="filePreview">
import {ref} from "vue";
import { defineExpose,defineProps } from "vue";

const src = ref()
const fileType = ref()


// 获取 父组件 传递的数据
const parentData = defineProps(['currentData'])

/*// eslint-disable-next-line no-undef
const emit = defineEmits({ goBack: null })

const onOpen = (record) => {
  src.value = record.downloadPath
  fileType.value = record.suffix
}

// eslint-disable-next-line no-undef
defineExpose({
  onOpen
})*/
</script>
-->
