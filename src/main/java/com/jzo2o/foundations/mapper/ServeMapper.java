package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.api.foundations.dto.response.ServeResDTO;
import com.jzo2o.foundations.model.domain.Serve;
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
    List<ServeResDTO> queryServeListByRegionId(@Param("regionId") Long regionId);

    Integer qryServeByRegionIdAndServeItemId(@Param("regionId") Long regionId, @Param("serveItemId") Long serveItemId);

    int updatePriceById(Long id, BigDecimal price);

    /**
     * 更新服务售卖状态
     * @param id 服务 ID
     * @param saleStatus 售卖状态
     * @return 影响行数
     */
    int updateSaleStatus(@Param("id") Long id, @Param("saleStatus") Integer saleStatus);

    /**
     * 更新服务状态（逻辑删除）
     * @param id 服务 ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新服务为热门状态
     * @param id 服务 ID
     * @param isHot 是否热门 (1:热门)
     * @param hotTimeStamp 热门时间戳
     * @return 影响行数
     */
    int updateOnHotStatus(@Param("id") Long id, @Param("isHot") Integer isHot, @Param("hotTimeStamp") Long hotTimeStamp);

    /**
     * 更新服务为非热门状态
     * @param id 服务 ID
     * @param isHot 是否热门 (0:非热门)
     * @param hotTimeStamp 热门时间戳
     * @return 影响行数
     */
    int updateOffHotStatus(@Param("id") Long id, @Param("isHot") Integer isHot, @Param("hotTimeStamp") Long hotTimeStamp);
}
