package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    //业务层一定有数据库对象
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        //如果有条件把ProductInfoExample这个对象单独new出来，追加条件
        //没有条件就直接new出来就可以了
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNums, int pageSize) {
        //用PageHelper来进行分页
        PageHelper.startPage(pageNums, pageSize);
        //创建对象
        ProductInfoExample productInfoExample = new ProductInfoExample();
        //追加条件
        productInfoExample.setOrderByClause("p_id desc");
        //完成条件
        List<ProductInfo> list = productInfoMapper.selectByExample(productInfoExample);
        //将集合封装进PageInfo对象当中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        //返回
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getByID(int pid) {

        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //用PageHelper来进行分页
        PageHelper.startPage(vo.getPage(), pageSize);
        List<ProductInfo> list=productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list) ;
    }
}
