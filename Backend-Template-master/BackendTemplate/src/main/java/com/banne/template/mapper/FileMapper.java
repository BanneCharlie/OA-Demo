package com.banne.template.mapper;

import com.banne.template.model.dto.file.FilePageParam;
import com.banne.template.model.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<File> {
    List<File> page(FilePageParam filePageParam);
}
