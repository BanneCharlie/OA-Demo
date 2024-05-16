package com.banne.template.controller;

import com.banne.template.api.FileEngineTypeEnum;
import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.result.Result;
import com.banne.template.model.dto.file.FileIdParam;
import com.banne.template.model.dto.file.FilePageParam;
import com.banne.template.model.entity.File;
import com.banne.template.service.FileService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "附件管理")
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;


    /**
     * 上传本地文件返回 id
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传本地文件返回id")
    @PostMapping("/uploadLocalReturnId")
    public Result<String> uploadLocalReturnId(@RequestBody MultipartFile file) {
        String result = fileService.uploadReturnId(FileEngineTypeEnum.LOCAL.getValue(),file);
        return Result.build(result, ResultCodeEnum.SUCCESS);
    }



    /**
     * 本地文件上传，返回文件Url
     *
     **/
    @ApiOperation(value = "上传本地文件返回url")
    @PostMapping("/uploadLocalReturnUrl")
    public Result<String> uploadLocalReturnUrl(@RequestParam("file") MultipartFile file) {
        String result = fileService.uploadLocalReturnUrl(FileEngineTypeEnum.LOCAL.getValue(),file);
        return Result.build(result, ResultCodeEnum.SUCCESS);
    }


    /**
     * 本地多文件上传，返回文件Url
     *
     **/
    @ApiOperation(value = "上传本地多文件返回url")
    @PostMapping("/uploadLocalReturnUrlAll")
    public Result<String[]> uploadLocalReturnUrlAll(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile multipartFile : files) {
            this.uploadLocalReturnUrl(multipartFile);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    /**
     * 获取文件分页列表
     *
     */
    @ApiOperation(value = "获取文件分页列表")
    @PostMapping("/page")
    public Result<PageInfo<File>> page(@RequestBody FilePageParam filePageParam) {
        PageInfo<File> pageList = fileService.page(filePageParam);
        return Result.build(pageList,ResultCodeEnum.SUCCESS);
    }


    /**
     * 下载文件
     *
     **/
    @ApiOperation(value = "下载文件" )
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(FileIdParam fileIdParam, HttpServletResponse response) throws IOException {
        fileService.download(fileIdParam, response);
    }

    /**
     * 删除文件
     *
     */
    @ApiOperation(value = "删除文件")
    @PostMapping(value = "/delete/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        fileService.removeById(id);
        return Result.build("删除成功",ResultCodeEnum.SUCCESS);
    }

    /**
     * 获取文件详情
     *
     **/
    @ApiOperation(value = "获取文件详情")
    @GetMapping("/detail")
    public Result<File> detail(FileIdParam fileIdParam) {
        File file = fileService.detail(fileIdParam);
        return Result.build(file,ResultCodeEnum.SUCCESS);
    }

}
