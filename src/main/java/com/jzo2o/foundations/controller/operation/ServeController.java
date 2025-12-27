package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务相关接口")
public class ServeController {
    @Autowired
    private IServeService iServeService;

    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        return iServeService.page(servePageQueryReqDTO);
    }
    
    @PostMapping("/batch")
    @ApiOperation("区域服务批量新增")
    public void batchAdd(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList) throws UnknownHostException {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            iServeService.batchAdd(serveUpsertReqDTO);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("区域价格修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void setPrice(@PathVariable("id") Long id, @RequestParam("price") BigDecimal price) {
        iServeService.updatePrice(id, price);
    }

    @PutMapping("/onSale/{id}")
    @ApiOperation("区域服务上架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public void onSale(@PathVariable("id") Long id) {
        iServeService.onSale(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation("区域服务下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public void offSale(@PathVariable("id") Long id) {
        iServeService.offSale(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public void delete(@PathVariable Long id) {
        iServeService.deleteById(id);
    }

    @PutMapping("/onHot/{id}")
    @ApiOperation("设置区域热门服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public void onHot(@PathVariable Long id) {
        iServeService.onHot(id);
    }

    @PutMapping("/offHot/{id}")
    @ApiOperation("取消区域热门服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public void offHot(@PathVariable Long id) {
        iServeService.offHot(id);
    }
}
