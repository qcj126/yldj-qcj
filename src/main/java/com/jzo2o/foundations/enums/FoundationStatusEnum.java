package com.jzo2o.foundations.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FoundationStatusEnum {
    INIT(0,"草稿"),
    ENABLE(2,"启用"),
    DISABLE(1, "禁用"),
    NOT_HOT(0, "取消区域服务热门状态"),
    IS_HOT(1, "设置区域服务热门状态");
    private int status;
    private String description;

    public boolean equals(Integer status) {
        return this.status == status;
    }

    public boolean equals(FoundationStatusEnum enableStatusEnum) {
        return enableStatusEnum != null && enableStatusEnum.status == this.getStatus();
    }
}
