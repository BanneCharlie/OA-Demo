import request  from "@/utils/request";

// 本地文件上传 返回文件id
export  const  fileUploadReturnId = (data) => {
    const requestData = {
        ...data,
    }
    return request({
        url: '/file/uploadLocalReturnId',
        method: 'post',
        data: requestData,
    })
}

// 本地文件上传 返回文件 url
export  const  fileUploadLocalReturnUrl = (data) => {
    const requestData = {
       ...data,
    }
    return request({
        url: '/file/uploadLocalReturnUrl',
        method: 'post',
        data: requestData,
    })
}

// MINIO文件上传 返回文件id
export  const  fileUploadMinioReturnId = (data) => {
    const requestData = {
       ...data,
    }
    return request({
        url: '/file/uploadMinioReturnId',
        method: 'post',
        data: requestData,
    })
}

// MINIO文件上传 返回文件 url
export  const  fileUploadMinioReturnUrl = (data) => {
    const requestData = {
       ...data,
    }
    return request({
        url: '/file/uploadMinioReturnUrl',
        method: 'post',
        data: requestData,
    })
}

// 获取上传文件分页列表
export  const  filePage = (value) => {
    const requestData = {
       ...value,
    }
    return request({
        url: '/file/page',
        method: 'post',
        data: requestData,
    })
}

// 获取上传文件列表
export  const  fileList = (data) => {
    const requestData = {
       ...data,
    }
    return request({
        url: '/file/list',
        method: 'get',
        params: requestData,
    })
}

// 下载文件
export  const  fileDownload = (data) => {
    const requestData = {
       ...data,
    }
    return request({
        url: '/file/download',
        method: 'get',
        params: requestData,
    })
}

// 获取文件详情
export  const  fileDetail = (data) => {
    const requestData = {
       ...data,
    }
    return request({
        url: '/file/detail',
        method: 'get',
        params: requestData,
    })
}

// 删除文件
export  const  fileDelete = (id) => {
    return request({
        url: `/file/delete/${id}`,
        method: 'post',
    })
}



/*export default {
/!*    // 动态上传文件返回id
    fileUploadDynamicReturnId(data) {
        return request('uploadDynamicReturnId', data)
    },
    // 动态上传文件返回url
    fileUploadDynamicReturnUrl(data) {
        return request('uploadDynamicReturnUrl', data)
    },*!/

/!*    // 本地文件上传，返回文件id
    fileUploadReturnId(data) {
        return request('uploadLocalReturnId', data)
    },

    // 本地文件上传，返回文件Url
    fileUploadLocalReturnUrl(data) {
        return request('uploadLocalReturnUrl', data)
    },

    // MINIO文件上传，返回文件id
    fileUploadMinioReturnId(data) {
        return request('uploadMinioReturnId', data)
    },

    // MINIO文件上传，返回文件Url
    fileUploadMinioReturnUrl(data) {
        return request('uploadMinioReturnUrl', data)
    },*!/

    // 获取文件分页列表
    filePage(data) {
        return request('page', data, 'get')
    },
    // 获取文件列表
    fileList(data) {
        return request('list', data, 'get')
    },
    // 下载文件，这里要带上blob类型
    fileDownload(data) {
        return request('download', data, 'get', {
            responseType: 'blob'
        })
    },
    // 获取文件详情
    fileDetail(data) {
        return request('detail', data, 'get')
    },
    // 删除文件
    fileDelete(data) {
        return request('delete', data)
    }



    /!*!// 阿里云文件上传，返回文件id
fileUploadAliyunReturnId(data) {
    return request('uploadAliyunReturnId', data)
},
// 腾讯云文件上传，返回文件id
fileUploadTencentReturnId(data) {
    return request('uploadTencentReturnId', data)
},*!/

    /!*    // 阿里云文件上传，返回文件Url
        fileUploadAliyunReturnUrl(data) {
            return request('uploadAliyunReturnUrl', data)
        },
        // 腾讯云文件上传，返回文件Url
        fileUploadTencentReturnUrl(data) {
            return request('uploadTencentReturnUrl', data)
        },*!/
}*/
