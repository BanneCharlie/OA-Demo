package com.banne.template.service.imp;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.banne.template.api.FileEngineTypeEnum;
import com.banne.template.common.context.BaseContext;
import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.utils.CommonDownloadUtil;
import com.banne.template.common.utils.FileLocalUtil;
import com.banne.template.common.utils.SnowflakeIdGeneratorUtil;
import com.banne.template.mapper.FileMapper;
import com.banne.template.mapper.UserMapper;
import com.banne.template.model.dto.file.FileIdParam;
import com.banne.template.model.dto.file.FilePageParam;
import com.banne.template.model.entity.File;
import com.banne.template.model.entity.User;
import com.banne.template.service.FileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
public class FileServiceImp extends ServiceImpl<FileMapper, File> implements FileService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

    private static final String BACKEND_URL = "http://localhost:9091";

    private static final String FRONTEND_URL = "http://localhost:3001";
    @Resource
    private SnowflakeIdGeneratorUtil snowflakeIdGeneratorUtil;

    @Override
    public String uploadReturnId(String engine, MultipartFile file) {
        return this.storageFile(engine,file,true);
    }


    @Override
    public String uploadLocalReturnUrl(String engine, MultipartFile file) {
        return this.storageFile(engine,file,false);
    }

    private String storageFile(String engine, MultipartFile file, boolean returnFileId) {

        // 生成id
        String fileId = IdWorker.getIdStr();

        // 存储桶名称
        String bucketName;

        // 定义存储的url，本地文件返回文件实际路径，其他引擎返回网络地址
        String storageUrl;

        // 根据引擎类型执行不同方法
        if(engine.equals(FileEngineTypeEnum.LOCAL.getValue())) {

            // 使用固定名称defaultBucketName
            bucketName = "defaultBucketName";
            storageUrl = FileLocalUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);
        }/* else if(engine.equals(FileEngineTypeEnum.MINIO.getValue())) {

            // 使用MINIO默认配置的bucketName
            bucketName = FileMinIoUtil.getDefaultBucketName();
            storageUrl = FileMinIoUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);
        }*/ else {
            throw new BusinessException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }

        // 将文件信息保存到数据库
        File devFile = new File();

        // 设置文件id
        devFile.setId(fileId);

        // 设置存储引擎类型
        devFile.setEngine(engine);
        devFile.setBucket(bucketName);
        devFile.setName(file.getOriginalFilename());
        String suffix = ObjectUtil.isNotEmpty(file.getOriginalFilename())? StrUtil.subAfter(file.getOriginalFilename(),
                StrUtil.DOT, true):null;
        devFile.setSuffix(suffix);
        devFile.setSizeKb(Convert.toStr(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024))
                .setScale(0, RoundingMode.HALF_UP)));

        devFile.setSizeInfo(FileUtil.readableFileSize(file.getSize()));
        devFile.setObjName(ObjectUtil.isNotEmpty(devFile.getSuffix())?fileId + StrUtil.DOT + devFile.getSuffix():null);
        // 如果是图片，则压缩生成缩略图
        if(ObjectUtil.isNotEmpty(suffix)) {
            if(isPic(suffix)) {
                try {
                    devFile.setThumbnail(ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(file.getBytes()),
                            100, 100, null), suffix));
                } catch (Exception ignored) {
                }
            }
        }
        devFile.setIsdelete("0");

        // 获取当前登录的用户信息
        Long currentId = BaseContext.getCurrentId();
        User user = userMapper.userQueryById(currentId);
        devFile.setCreateUser(user.getUserName());
        devFile.setUpdateUser(user.getUserName());

        // 存储路径
        devFile.setStoragePath(storageUrl);

        // 定义下载地址
        String downloadUrl;

        // 下载路径，注意：本地文件下载地址设置为下载接口地址 + 文件id
        if(engine.equals(FileEngineTypeEnum.LOCAL.getValue())) {
            String apiUrl = BACKEND_URL;
            if(ObjectUtil.isEmpty(apiUrl)) {
                throw new BusinessException(ResultCodeEnum.BACKEND_URL_ERROR);
            }
            downloadUrl= apiUrl + "/file/download?id=" + fileId;
            devFile.setDownloadPath(downloadUrl);
        } else {
            // 阿里云、腾讯云、MINIO可以直接使用存储地址（公网）作为下载地址
            downloadUrl= storageUrl;
            devFile.setDownloadPath(devFile.getStoragePath());
        }



        this.save(devFile);

        // 如果是返回id则返回文件id
        if(returnFileId) {
            return fileId;
        } else {
            // 否则返回下载地址
            return downloadUrl;
        }
    }

    /**
     * 根据文件后缀判断是否图片
     *
     */
    private static boolean isPic(String fileSuffix) {
        fileSuffix = fileSuffix.toLowerCase();
        return ImgUtil.IMAGE_TYPE_GIF.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_JPG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_JPEG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_BMP.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_PNG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_PSD.equals(fileSuffix);
    }

    /**
     * 生成文件的key，格式如 2021/10/11/1377109572375810050.docx
     **/
    public String genFileKey(String fileId, MultipartFile file) {

        // 获取文件原始名称
        String originalFileName = file.getOriginalFilename();

        // 获取文件后缀
        String fileSuffix = FileUtil.getSuffix(originalFileName);

        // 生成文件的对象名称，格式如:1377109572375810050.docx
        String fileObjectName = fileId + StrUtil.DOT + fileSuffix;

        // 获取日期文件夹，格式如，2021/10/11/
        String dateFolderPath = DateUtil.thisYear() + StrUtil.SLASH +
                (DateUtil.thisMonth() + 1) + StrUtil.SLASH +
                DateUtil.thisDayOfMonth() + StrUtil.SLASH;

        // 返回
        return dateFolderPath + fileObjectName;
    }


    @Override
    public PageInfo<File> page(FilePageParam filePageParam) {
        // 初始化 pageInfo
        PageHelper.startPage(filePageParam.getPage(),filePageParam.getPageSize());
        List<File>  pageList =  fileMapper.page(filePageParam);
        PageInfo<File> filePageInfo = new PageInfo<>(pageList);
        return filePageInfo;
    }


    @Override
    public void download(FileIdParam fileIdParam, HttpServletResponse response) {
        File devFile;
        try {
            devFile = this.queryEntity(fileIdParam.getId());
        } catch (Exception e) {
            log.error("出现异常");
            return;
        }
        if(!devFile.getEngine().equals(FileEngineTypeEnum.LOCAL.getValue())) {
            log.error("非本地文件不支持此方式下载，id值为：" + devFile.getId());
            return;
        }
        java.io.File file = FileUtil.file(devFile.getStoragePath());
        if(!FileUtil.exist(file)) {
            log.error("找不到存储的文件，id值为：" + devFile.getId());
            return;
        }
        CommonDownloadUtil.download(devFile.getName(), IoUtil.readBytes(FileUtil.getInputStream(file)), response);
    }

    public File queryEntity(String id) {
        File devFile = this.getById(id);
        if(ObjectUtil.isEmpty(devFile)) {
            throw new BusinessException(ResultCodeEnum.FILE_NOT_EXIST);
        }
        return devFile;
    }

    @Override
    public void delete(List<FileIdParam> fileIdParamList) {
        this.removeByIds(CollStreamUtil.toList(fileIdParamList, FileIdParam::getId));
    }

    @Override
    public File detail(FileIdParam fileIdParam) {
        return this.queryEntity(fileIdParam.getId());
    }


}
