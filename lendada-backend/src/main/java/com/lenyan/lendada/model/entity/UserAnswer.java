package com.lenyan.lendada.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户答题记录
 * @TableName user_answer
 */
@TableName(value ="user_answer")
@Data
@Api(tags = "用户答题记录接口")
public class UserAnswer implements Serializable {
    /**
     * 记录ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "记录ID", example = "1")
    private Long id;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID", example = "1")
    private Long appId;

    /**
     * 应用类型：0-得分类，1-角色测评类
     */
    @ApiModelProperty(value = "应用类型", notes = "0-得分类，1-角色测评类", example = "0")
    private Integer appType;

    /**
     * 评分策略：0-自定义，1-AI评分
     */
    @ApiModelProperty(value = "评分策略", notes = "0-自定义，1-AI评分", example = "1")
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON数组），例如：[{"questionId": 1, "selectedChoice": "A"}, ...]
     */
    @ApiModelProperty(value = "用户答案JSON数组")
    private String choices;

    /**
     * 关联的评分结果ID
     */
    @ApiModelProperty(value = "评分结果ID", example = "1")
    private Long resultId;

    /**
     * 结果名称，如物流师（关联自ScoringResult）
     */
    @ApiModelProperty(value = "结果名称", example = "物流师")
    private String resultName;

    /**
     * 结果描述
     */
    @ApiModelProperty(value = "结果描述", example = "擅长物流规划与管理")
    private String resultDesc;

    /**
     * 结果图标URL
     */
    @ApiModelProperty(value = "结果图标URL", example = "http://example.com/icon.png")
    private String resultPicture;

    /**
     * 用户最终得分
     */
    @ApiModelProperty(value = "用户得分", example = "85")
    private Integer resultScore;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 是否删除标志
     */
    @TableLogic
    @ApiModelProperty(hidden = true)
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}