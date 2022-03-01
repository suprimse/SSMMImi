package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.font.MultipleMaster;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //图片名称
    String saveFileName = "";
    //每页的记录数
    public static final int PAGE_SIZE = 5;
    //显示全部商品不分页
    //在这个界面一定会有业务逻辑层的对象

    @Autowired
    ProductInfoService productInfoService;

    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    //分页
    //显示第一页的五条
    //增加功能，让其在更新或者删除后，停留在当前页面。
    //思路：把ProductInfoVo这个属性通过前端返回，封装起来存在Session中，使用完事后从Session中删除
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            //清除这个Session
            request.getSession().removeAttribute("prodVo");
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
            request.setAttribute("info", info);
        }
        //request.setAttribute("info",info);
        return "product";
    }

    //ajax翻页请求处理
    @RequestMapping("/ajaxsplit")
    @ResponseBody
    public void ajaxsplit(ProductInfoVo vo, HttpSession session) {
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //条件查询
    @RequestMapping(value = "/condition")
    @ResponseBody
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }

    //ajax文件上传处理
    @RequestMapping("/ajaxImg")
    @ResponseBody
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名字UURD+图片后缀
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中文件储存的路径
        String path = request.getServletContext().getRealPath("/image_big");
        //转存 可以把文件转存到E:\Ajava\SSMMImi\image_big\dsdssadasdasd.jbg E:\Ajava\SSMMImi\src\main\webapp
        //E:/Ajava/SSMMImi/src/main/webapp
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回客户端的json对象，封装图片路径，为了在页面实现回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }

    //保存属性
    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request, HttpServletResponse response) {
        info.setpDate(new Date());
        info.setpImage(saveFileName);
        //info中有其他对象提交上来的属性
        int nums = -1;
        try {
            nums = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nums > 0) {
            request.setAttribute("msg", "上传成功");
        } else {
            request.setAttribute("msg", "上传失败");
        }
        //清空saveFileName
        saveFileName = "";
        //增加数据应该重新访问数据库，所以跳转到分页显示的action上
        return "forward:/prod/split.action";
    }

    //更新数据
    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session) {
        ProductInfo info = productInfoService.getByID(pid);
        model.addAttribute("prod", info);
        //将多条件以及页面放进session，进行更新处理结束后读取条件和页码进行出来
        session.setAttribute("prodVo", vo);
        return "update";
    }

    //保存更新的数据
    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        //因为ajax的异步图片上传，如果有上传过
        //saveFileName中就会有上传上来图片的名称，如果没有上传过saveFileName就是空
        //则实体类info就使用隐藏表单提供Image原始图片的名称
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        int nums = -1;
        try {
            nums = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nums > 0) {
            request.setAttribute("msg", "更新成功");
        } else {
            request.setAttribute("msg", "更新失败");
        }
        //防止与下一次的更新报错，再次清空
        //清空saveFileName
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    //删除的数据
    @RequestMapping("/delete")
    public String delete(int pid, ProductInfoVo vo, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "删除成功");
            request.getSession().setAttribute("deleteProdVo", vo);
        } else {
            request.setAttribute("msg", "删除失败");
        }
        return "forward:/prod/deleteAjaxSpilt.action";
    }

    //删除后，为了显示删除成功的所以重新做分页
    @RequestMapping(value = "/deleteAjaxSpilt", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object DeleteAjaxSpilt(HttpServletRequest request) {
        //取第一页数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }

        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");
    }

    //批量删除功能
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpServletRequest request) {
        //将上传上来的字符串截开，形成商品id的字符串
        String[] ps = pids.split(",");
        try {
            int nums = productInfoService.deleteBatch(ps);
            if (nums > 0) {
                request.setAttribute("msg", "批量删除成功");
            } else {
                request.setAttribute("msg", "批量删除失败");
            }
        } catch (Exception e) {
            request.setAttribute("msg", "批量删除出现异常");
        }
        return "forward:/prod/deleteAjaxSpilt.action";
    }

}

