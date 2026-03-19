package com.jzo2o.foundations.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.dto.response.ServeResDTO;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {
    @Resource
    private ServeMapper serveMapper;
    @Resource
    private RegionMapper regionMapper;
    @Resource
    private ServeItemMapper serveItemMapper;

    @Override
    public PageResult<ServeResDTO> qryPageByRegionId(ServePageQueryReqDTO servePageQueryReqDTO) {
        if (servePageQueryReqDTO != null && servePageQueryReqDTO.getRegionId() != null) {
            return PageHelperUtils.selectPage(
                    servePageQueryReqDTO, () -> serveMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
        }
        return null;
    }

    @Override
    public void add(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        // 可以进行优化，批量插入
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            Long regionId = serveUpsertReqDTO.getRegionId();
            Long serveItemId = serveUpsertReqDTO.getServeItemId();
            // 检查服务项是否为启用状态，如果为禁用状态则不能添加
            ServeItem serveItem = serveItemMapper.selectById(serveItemId);
            if (serveItem == null || serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
                throw new RuntimeException("服务项状态异常");
            }
            // 检查当前区域下的服务项中，是否已添加过服务
            Integer count = serveMapper.qryServeByRegionIdAndServeItemId(regionId, serveItemId);
            if (count != null && count > 0) {
                throw new ForbiddenOperationException("服务已存在");
            }
            Serve serve = new Serve();
            serve.setRegionId(regionId);
            serve.setServeItemId(serveItemId);
            Region region = regionMapper.selectById(regionId);
            if (region != null) {
                serve.setCityCode(region.getCityCode());
            }
            serve.setPrice(serveItem.getReferencePrice());
            serveMapper.insert(serve);
            log.info("添加服务成功，服务项ID：{}", serveItemId);
        }
    }

    @Override
    public void updatePrice(Long id, BigDecimal price) {
        int count = serveMapper.updatePriceById(id, price);
        if (count == 0) {
            throw new ForbiddenOperationException("服务价格更新失败");
        }
    }

    @Override
    public void onSale(Long id) {
        // 1.查询服务信息
        Serve serve = baseMapper.selectById(id);
        if (serve == null) {
            throw new ForbiddenOperationException("服务不存在");
        }

        // 2.判断服务状态是否为草稿或下架（0 或 1）
        Integer saleStatus = serve.getSaleStatus();
        if (!FoundationStatusEnum.INIT.equals(saleStatus) && !FoundationStatusEnum.DISABLE.equals(saleStatus)) {
            throw new ForbiddenOperationException("只有草稿或下架状态的服务才允许上架");
        }

        // 3.查询关联的服务项，判断服务项状态是否为启用状态（2）
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if (serveItem == null) {
            throw new ForbiddenOperationException("服务项不存在");
        }

        if (!FoundationStatusEnum.ENABLE.equals(serveItem.getActiveStatus())) {
            throw new ForbiddenOperationException("服务项未启用，无法上架");
        }

        // 4.更新服务状态为上架（2）
        int count = serveMapper.updateSaleStatus(id, FoundationStatusEnum.ENABLE.getStatus());
        if (count == 0) {
            throw new ForbiddenOperationException("服务上架失败");
        }

        log.info("服务上架成功，服务 ID：{}, 服务项 ID：{}", id, serveItemId);
    }

    @Override
    public void offSale(Long id) {
        // 1.查询服务信息
        Serve serve = baseMapper.selectById(id);
        if (serve == null) {
            throw new ForbiddenOperationException("服务不存在");
        }

        // 2.判断服务状态是否为上架状态（2）
        Integer saleStatus = serve.getSaleStatus();
        if (!FoundationStatusEnum.ENABLE.equals(saleStatus)) {
            throw new ForbiddenOperationException("只有上架状态的服务才允许下架");
        }

        // 3.更新服务状态为下架（1）
        int count = serveMapper.updateSaleStatus(id, FoundationStatusEnum.DISABLE.getStatus());
        if (count == 0) {
            throw new ForbiddenOperationException("服务下架失败");
        }

        log.info("服务下架成功，服务 ID：{}", id);
    }

    @Override
    public void deleteById(Long id) {
        // 1.查询服务信息
        Serve serve = baseMapper.selectById(id);
        if (serve == null) {
            throw new ForbiddenOperationException("服务不存在");
        }

        // 2.判断服务状态是否为草稿状态（0）
        Integer saleStatus = serve.getSaleStatus();
        if (!FoundationStatusEnum.INIT.equals(saleStatus)) {
            throw new ForbiddenOperationException("只有草稿状态的服务才允许删除");
        }

        // 3.更新服务状态为删除（1100）
        int count = serveMapper.updateStatus(id, FoundationStatusEnum.DELETED.getStatus());
        if (count == 0) {
            throw new ForbiddenOperationException("服务删除失败");
        }

        log.info("服务删除成功，服务 ID：{}", id);
    }

    @Override
    public void onHot(Long id) {
        // 1.查询服务信息
        Serve serve = baseMapper.selectById(id);
        if (serve == null) {
            throw new ForbiddenOperationException("服务不存在");
        }

        // 2.判断服务是否为非热门状态（0）
        Integer isHot = serve.getIsHot();
        if (!FoundationStatusEnum.OFFHOT.equals(isHot)) {
            throw new ForbiddenOperationException("只有非热门状态的服务才允许设置为热门");
        }

        // 3.更新服务为热门状态（1），并设置热门时间戳
        long hotTimeStamp = System.currentTimeMillis();
        int count = serveMapper.updateOnHotStatus(id, FoundationStatusEnum.ONHOT.getStatus(), hotTimeStamp);
        if (count == 0) {
            throw new ForbiddenOperationException("设置热门服务失败");
        }

        log.info("设置热门服务成功，服务 ID：{}, 热门时间戳：{}", id, hotTimeStamp);
    }

    @Override
    public void offHot(Long id) {
        // 1.查询服务信息
        Serve serve = baseMapper.selectById(id);
        if (serve == null) {
            throw new ForbiddenOperationException("服务不存在");
        }

        // 2.判断服务是否为非热门状态（0）
        Integer isHot = serve.getIsHot();
        if (!FoundationStatusEnum.ONHOT.equals(isHot)) {
            throw new ForbiddenOperationException("只有热门状态的服务才允许取消热门");
        }

        // 3.更新服务为热门状态（1），并设置热门时间戳
        long hotTimeStamp = System.currentTimeMillis();
        int count = serveMapper.updateOffHotStatus(id, FoundationStatusEnum.OFFHOT.getStatus(), hotTimeStamp);
        if (count == 0) {
            throw new ForbiddenOperationException("取消热门服务失败");
        }

        log.info("取消热门服务成功，服务 ID：{}, 热门时间戳：{}", id, hotTimeStamp);
    }
}