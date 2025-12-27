package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.net.UnknownHostException;

public interface IServeService extends IService<Serve> {

    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    void batchAdd(ServeUpsertReqDTO serveUpsertReqDTO) throws UnknownHostException;

    void updatePrice(Long id, BigDecimal price);

    void onSale(Long id);

    void deleteById(Long id);

    void offSale(Long id);

    void onHot(Long id);

    void offHot(Long id);
}
