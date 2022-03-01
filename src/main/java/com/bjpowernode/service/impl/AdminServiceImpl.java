package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        AdminExample adminExample = new AdminExample();
        //添加用户名a_name条件
        //等于 select * from admin where a_name="admin"
        adminExample.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        if (list.size() > 0) {
            Admin admin = list.get(0);
            //验证加密后的密码和数据库返回的密码是否相同
            if (MD5Util.getMD5(pwd).equals(admin.getaPass())) {
                return admin;
            }
        }
        return null;
    }
}
