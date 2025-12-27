package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("服务响应值")
public class ServeResDTO {
    /** 主键id */
    @ApiModelProperty("主键id")
    private Long id;

    /** 服务项名称 */
    @ApiModelProperty("服务名称")
    private String serveItemName;

    /** 服务项id */
    @ApiModelProperty("服务id")
    private Long serveItemId;

    /** 售卖状态 */
    @ApiModelProperty("售卖状态  0：草稿 1下架 2上架")
    private Integer saleStatus;

    /** 服务类型名称 */
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;

    /** 服务类型id */
    @ApiModelProperty("服务类型id")
    private Long serveTypeId;

    /** 区域id */
    @ApiModelProperty("区域id")
    private Long regionId;

    /** 参考价格 */
    @ApiModelProperty("参考价格")
    private BigDecimal referencePrice;

    /** 价格 */
    @ApiModelProperty("区域价格")
    private BigDecimal price;

    /** 是否热门 */
    @ApiModelProperty("是否热门  0非热门 1热门")
    private Integer isHot;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
