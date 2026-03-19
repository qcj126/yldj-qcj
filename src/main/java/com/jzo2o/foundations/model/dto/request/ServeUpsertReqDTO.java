package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 区域服务新增更新
 *
 * @author Qoder
 */
@Data
@ApiModel("区域服务新增更新")
public class ServeUpsertReqDTO {

    /**
     * 服务id
     */
    @ApiModelProperty(value = "服务id", required = true)
    private Long serveItemId;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id", required = true)
    private Long regionId;
}