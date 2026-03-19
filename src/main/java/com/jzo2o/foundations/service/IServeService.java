package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.foundations.dto.response.ServeResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IServeService extends IService<Serve> {
    PageResult<ServeResDTO> qryPageByRegionId(ServePageQueryReqDTO servePageQueryReqDTO);

    void add(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    void updatePrice(Long id, BigDecimal price);

    void onSale(Long id);
    
    void offSale(Long id);

    /**
     * 删除服务
     * @param id 服务 ID
     */
    void deleteById(Long id);

    /**
     * 设置热门服务
     * @param id 服务 ID
     */
    void onHot(Long id);

    void offHot(Long id);
}
