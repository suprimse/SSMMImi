package com.bjpowernode.pojo.vo;

public class ProductInfoVo {
    //商品名称
    private String pname;
    //商品类型
    private Integer typeid;
    //商品最低价格
    private Integer Iprice;
    //商品最高价格
    private Integer hprice;
    //当前页数
    private Integer page=1;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Integer getIprice() {
        return Iprice;
    }

    public void setIprice(Integer iprice) {
        Iprice = iprice;
    }

    public Integer getHprice() {
        return hprice;
    }

    public void setHprice(Integer hprice) {
        this.hprice = hprice;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ProductInfoVo(String pname, Integer typeid, Integer iprice, Integer hprice, Integer page) {
        this.pname = pname;
        this.typeid = typeid;
        Iprice = iprice;
        this.hprice = hprice;
        this.page = page;
    }

    public ProductInfoVo() {
    }

    @Override
    public String toString() {
        return "ProductInfoVo{" +
                "pname='" + pname + '\'' +
                ", typeid=" + typeid +
                ", Iprice=" + Iprice +
                ", hprice=" + hprice +
                ", page=" + page +
                '}';
    }
}
