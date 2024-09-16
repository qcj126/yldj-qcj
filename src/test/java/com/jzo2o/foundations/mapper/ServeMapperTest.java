package com.jzo2o.foundations.mapper;

import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2024/9/13 19:03
 */
@SpringBootTest
public class ServeMapperTest {

   @Autowired
   private ServeMapper serveMapper;

   //对ServeMapper中的queryServeListByRegionId方法进行测试
   @Test
   public void testQueryServeListByRegionId(){
    List<ServeResDTO> serveResDTOS = serveMapper.queryServeListByRegionId(1686303222843662337L);
    Assert.notEmpty(serveResDTOS,"列表为空");
   }

}
