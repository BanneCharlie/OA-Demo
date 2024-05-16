package com.banne.template.service;

import com.banne.template.model.dto.file.FileIdParam;
import com.banne.template.model.dto.file.FilePageParam;
import com.banne.template.model.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface FileService extends IService<File> {
    String uploadReturnId(String engine, MultipartFile file);

    String uploadLocalReturnUrl(String engine, MultipartFile file);

    PageInfo<File> page(FilePageParam filePageParam);


    void download(FileIdParam fileIdParam, HttpServletResponse response);

    void delete(List<FileIdParam> fileIdParamList);

    File detail(FileIdParam fileIdParam);
}
