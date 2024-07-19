package com.lenyan.lendada.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目
 * @TableName question
 */
@TableName(value ="question")
@Data
@Api(tags = "题目接口")
public class Question implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "题目ID", example = "1")
    private Long id;

    /**
     * 题目内容，JSON格式存储，可以根据应用需求包含问题文本、选项等
     */
    @ApiModelProperty(value = "题目内容（JSON格式）", example = "{\"text\": \"世界上最高的山是？\", \"options\": [\"珠穆朗玛峰\", \"喜马拉雅山脉\", \"乔戈里峰\"]}")
    private String questionContent;

    /**
     * 关联的应用ID
     */
    @ApiModelProperty(value = "应用ID", example = "1")
    private Long appId;

    /**
     * 创建该题目的用户ID
     */
    @ApiModelProperty(value = "创建用户ID", example = "1")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true) // 隐藏此字段在Swagger中的显示，通常创建时间和更新时间由系统管理
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 是否已删除标志
     */
    @TableLogic
    @ApiModelProperty(hidden = true)
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}