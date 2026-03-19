package com.jzo2o.foundations.controller.operation;

import com.jzo2o.api.foundations.dto.response.ServeResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务相关接口")
public class ServeController {
    @Resource
    private IServeService serveService;

    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        return serveService.qryPageByRegionId(servePageQueryReqDTO);
    }

    @PostMapping("/batch")
    @ApiOperation("区域服务批量添加")
    public void batch(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        serveService.add(serveUpsertReqDTOList);
    }

    @PutMapping({"/{id}"})
    @ApiOperation("区域服务价格修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务 id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void update(@PathVariable Long id, @RequestParam BigDecimal price) {
        serveService.updatePrice(id, price);
    }

    @PutMapping("/onSale/{id}")
    @ApiOperation("服务上架")
    @ApiImplicitParam(name = "id", value = "服务 ID", required = true, dataTypeClass = Long.class)
    public void onSale(@PathVariable Long id) {
        serveService.onSale(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation("服务下架")
    @ApiImplicitParam(name = "id", value = "服务 ID", required = true, dataTypeClass = Long.class)
    public void offSale(@PathVariable Long id) {
        serveService.offSale(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("服务删除")
    @ApiImplicitParam(name = "id", value = "服务 ID", required = true, dataTypeClass = Long.class)
    public void delete(@PathVariable Long id) {
        serveService.deleteById(id);
    }

    @PutMapping("/onHot/{id}")
    @ApiOperation("设置热门服务")
    @ApiImplicitParam(name = "id", value = "服务 ID", required = true, dataTypeClass = Long.class)
    public void onHot(@PathVariable Long id) {
        serveService.onHot(id);
    }

    @PutMapping("/offHot/{id}")
    @ApiOperation("取消热门服务")
    @ApiImplicitParam(name = "id", value = "服务 ID", required = true, dataTypeClass = Long.class)
    public void offHot(@PathVariable Long id) {
        serveService.offHot(id);
    }
}
