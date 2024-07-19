package com.lenyan.lendada.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评分结果
 * @TableName scoring_result
 */
@TableName(value ="scoring_result")
@Data
@Api(tags = "评分结果接口")
public class ScoringResult implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "评分结果ID", example = "1")
    private Long id;

    /**
     * 结果名称，如物流师
     */
    @ApiModelProperty(value = "结果名称", example = "物流师")
    private String resultName;

    /**
     * 结果描述
     */
    @ApiModelProperty(value = "结果描述", example = "擅长物流规划与管理")
    private String resultDesc;

    /**
     * 结果图片链接
     */
    @ApiModelProperty(value = "结果图片URL", example = "http://example.com/result_picture.jpg")
    private String resultPicture;

    /**
     * 结果属性集合 JSON，如 ["I","S","T","J"]
     */
    @ApiModelProperty(value = "结果属性集合（JSON格式）", example = "[\"I\",\"S\",\"T\",\"J\"]")
    private String resultProp;

    /**
     * 结果得分范围，如 80，表示 80及以上的分数命中此结果
     */
    @ApiModelProperty(value = "结果得分范围", example = "80")
    private Integer resultScoreRange;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID", example = "1")
    private Long appId;

    /**
     * 创建用户ID
     */
    @ApiModelProperty(value = "创建用户ID", example = "1")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true) // 隐藏Swagger中显示
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

}