package com.banne.template.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class File {
    /** id */
    private String id;

    /** 存储引擎 */
    private String engine;

    /** 存储桶 */
    private String bucket;

    /** 文件名称 */
    private String name;

    /** 文件后缀 */
    private String suffix;

    /** 文件大小kb */
    private String sizeKb;

    /** 文件大小（格式化后） */
    private String sizeInfo;

    /** 文件的对象名（唯一名称） */
    private String objName;

    /** 文件存储路径 */
    private String storagePath;

    /** 文件下载路径 */
    private String downloadPath;

    /** 图片缩略图 */
    private String thumbnail;

    /** 扩展信息 */
    private String extJson;

    @JsonIgnore
    @TableLogic
    private String isdelete;

    /** 创建时间 */
    private Date createTime;


    /** 创建人名称 */
    private String createUser;

    /** 更新时间 */
    private Date updateTime;


    /** 更新人名称 */
    private String updateUser;
}
