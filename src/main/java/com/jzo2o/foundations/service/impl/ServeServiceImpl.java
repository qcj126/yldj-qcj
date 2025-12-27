package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    @Resource
    private IServeItemService serveItemService;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private ServeItemMapper serveItemMapper;

    @Autowired
    private ServeMapper serveMapper;

    /**
     * 区域服务分页查询
     */
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        return PageHelperUtils.selectPage(servePageQueryReqDTO,
                () -> baseMapper.qryRegionServeListById(servePageQueryReqDTO.getRegionId()));
    }

    /**
     * 区域服务新增
     */
    @Override
    @Transactional
    public void batchAdd(ServeUpsertReqDTO serveUpsertReqDTO) throws UnknownHostException {
        // 1. 校验服务项是否存在且已启用
        ServeItem serveItem = serveItemService.getById(serveUpsertReqDTO.getServeItemId());
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("服务项不存在");
        }
        if (FoundationStatusEnum.ENABLE.getStatus() != serveItem.getActiveStatus()) {
            throw new ForbiddenOperationException("服务项未启用，无法添加到区域");
        }
        // 3. 校验同一区域下是否已存在该服务项
        Long count = baseMapper.countServeItem(serveUpsertReqDTO.getRegionId(), serveUpsertReqDTO.getServeItemId());

        if (count > 0) {
            throw new ForbiddenOperationException("该区域已存在此服务项，不可重复添加");
        }
        // 4. 新增区域服务
        DefaultIdentifierGenerator defaultIdentifierGenerator = new DefaultIdentifierGenerator(new Sequence(InetAddress.getLocalHost()));
        Serve serve = BeanUtil.toBean(serveUpsertReqDTO, Serve.class);
        Long primaryId = defaultIdentifierGenerator.nextId(serve);
        Long regionId = serveUpsertReqDTO.getRegionId();
        BigDecimal price = serveItem.getReferencePrice();
        String cityCode = regionMapper.selectById(regionId).getCityCode();
        Long serveItemId = serveUpsertReqDTO.getServeItemId();
        baseMapper.insertIntoServe(primaryId, price, cityCode, serveItemId, regionId);
    }

    @Transactional
    @Override
    public void updatePrice(Long id, BigDecimal price) {
        // 修改价格
        Boolean isUpdate = serveMapper.updatePriceByServeId(price, id);
        if (!isUpdate) {
            throw new CommonException("修改服务价格失败");
        }
    }

    @Override
    @Transactional
    public void onSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //上架状态
        Integer saleStatus = serve.getSaleStatus();
        //草稿或下架状态方可上架
        if (!(saleStatus == FoundationStatusEnum.INIT.getStatus() || saleStatus == FoundationStatusEnum.DISABLE.getStatus())) {
            throw new ForbiddenOperationException("草稿或下架状态方可上架");
        }
        //服务项id
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        //服务项的启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //服务项为启用状态方可上架
        if (!(FoundationStatusEnum.ENABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
        }

        //更新为上架状态
        Boolean update = serveMapper.updateSalesStatus(FoundationStatusEnum.ENABLE.getStatus(), id);

        if (!update) {
            throw new CommonException("启动服务失败");
        }
        baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public void offSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务不存在");
        }
        // 售卖状态
        Integer saleStatus = serve.getSaleStatus();
        // 处于上架状态方可下架
        if (!(saleStatus == FoundationStatusEnum.ENABLE.getStatus())) {
            throw new ForbiddenOperationException("上架状态方可下架");
        }
        //服务项id
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        //服务项的启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //服务项为启用状态方可下架
        if (!(FoundationStatusEnum.ENABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可下架");
        }
        //更新状态
        baseMapper.offSale(id);
    }

    @Override
    public void onHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if (FoundationStatusEnum.IS_HOT.equals(serve.getIsHot())) {
            throw new ForbiddenOperationException("服务项已设置热门状态");
        }
        serveMapper.setOnHotStatus(FoundationStatusEnum.IS_HOT.getStatus(), id);
    }

    @Override
    public void offHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if (FoundationStatusEnum.NOT_HOT.equals(serve.getIsHot())) {
            throw new ForbiddenOperationException("服务项已取消热门状态");
        }
        serveMapper.setOnHotStatus(FoundationStatusEnum.NOT_HOT.getStatus(), id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if (serve.getSaleStatus() == FoundationStatusEnum.ENABLE.getStatus() || serve.getSaleStatus() == FoundationStatusEnum.DISABLE.getStatus()) {
            throw new ForbiddenOperationException("服务项状态为草稿时才可删除");
        }
        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("服务项不存在");
        }
        baseMapper.deleteByServeId(id);
    }
}
