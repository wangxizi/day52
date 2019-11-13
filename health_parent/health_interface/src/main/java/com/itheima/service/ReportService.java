package com.itheima.service;

import java.util.Map;

/*
* 获取运营统计数据服务接口
* */
public interface ReportService {
    //获取运营统计的数据
    Map<String,Object> getBusinessReportData() throws Exception;

}
