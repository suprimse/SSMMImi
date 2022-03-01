package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductTypeMapper;
import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.pojo.ProductTypeExample;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {
    //业务层一定有数据库对象mapper
    @Autowired
    ProductTypeMapper type;
    @Override
    public List<ProductType> getAll() {
        //无条件获取全部商品的类型
        return type.selectByExample(new ProductTypeExample());
    }
}
