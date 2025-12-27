package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.foundations.model.domain.Region;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 区域表 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface RegionMapper extends BaseMapper<Region> {
    void updateActiveStatus(@Param("id") Long id, @Param("activeStatus") int activeStatus);
}
