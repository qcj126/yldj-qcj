package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Mapper
public interface ServeMapper extends BaseMapper<Serve> {
    /**
     * 区域服务分页查询列表
     * @param regionId
     * @return
     */
    List<ServeResDTO> qryRegionServeListById(@Param("regionId") Long regionId);

    Long countServeItem(@Param("regionId") Long regionId, @Param("serveItemId") Long serveItemId);

    void insertIntoServe(
            @Param("primaryId") Long primaryId, @Param("price") BigDecimal price, @Param("cityCode") String cityCode,
            @Param("serveItemId") Long serveItemId, @Param("regionId") Long regionId
    );

    void deleteByServeId(@Param("id") Long id);

    void offSale(@Param("id") Long id);

    Boolean updatePriceByServeId(@Param("price") BigDecimal price, @Param("id") Long id);

    Boolean updateSalesStatus(@Param("status") int status, @Param("id") Long id);

    void setOnHotStatus(@Param("isHot") int isHot, @Param("id") Long id);

    Integer countOnSaleServeByRegionId(@Param("id") Long id, @Param("status") int status);

    Integer countOnSaleServeByServeItemId(Long id, int status);
}
