package com.lenyan.lendada.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用
 * @TableName app
 */
@TableName(value ="app")
@Data
@Api(tags = "应用接口")
public class App implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "应用名", required = true, example = "得分类应用")
    private String appName;

    @ApiModelProperty(value = "应用描述", example = "这是一个得分类的应用，帮助用户管理日常任务。")
    private String appDesc;

    @ApiModelProperty(value = "应用图标URL", example = "http://example.com/icon.png")
    private String appIcon;

    @ApiModelProperty(value = "应用类型（0-得分类，1-测评类）", allowableValues = "0,1", example = "0")
    private Integer appType;

    @ApiModelProperty(value = "评分策略（0-自定义，1-AI）", allowableValues = "0,1", example = "1")
    private Integer scoringStrategy;

    @ApiModelProperty(value = "审核状态：0-待审核, 1-通过, 2-拒绝", allowableValues = "0,1,2", example = "0")
    private Integer reviewStatus;

    @ApiModelProperty(value = "审核信息", example = "审核通过，应用符合要求。")
    private String reviewMessage;

    @ApiModelProperty(value = "审核人ID", example = "25")
    private Long reviewerId;

    @ApiModelProperty(value = "审核时间")
    private Date reviewTime;

    @ApiModelProperty(value = "创建用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty(hidden = true) // Hide this field from Swagger as it's a system field
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}