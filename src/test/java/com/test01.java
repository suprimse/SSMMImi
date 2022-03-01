package com;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml" ,"classpath:applicationContext_service.xml"})
public class test01 {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Test
    public void test01(){
        ProductInfoVo vo=new ProductInfoVo();
        vo.setIprice(4000);
        vo.setHprice(6000);
        List<ProductInfo> list=productInfoMapper.selectCondition(vo);
        list.forEach(productInfo->System.out.println(productInfo));
    }

}
