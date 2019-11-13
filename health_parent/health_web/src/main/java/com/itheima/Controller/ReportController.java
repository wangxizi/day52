package com.itheima.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 *报表控制层
 * */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /*
     * 会员数量折线图
     * */
    @RequestMapping(value = "/getMemberReport", method = RequestMethod.GET)
    public Result getMemberReport() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);//得到一年前的时间
        //1.获取年月时间
        List<String> moths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            moths.add(yearMonth);
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("moths", moths);
        //2.根据年月时间 获取每月底累加会员数量
        List<Integer> memberCount = memberService.findMemberCountBeforeDate(moths);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    //套餐占比统计
    @RequestMapping(value = "/getSetmealReport", method = RequestMethod.GET)
    public Result getSetmealReport() {
        List<Map<String, Object>> list = setmealService.findSetmealCount();

        Map<String, Object> map = new HashMap<>();
        map.put("setmealCount", list);
        List<String> setmealNames = new ArrayList<>();
        for (Map<String, Object> m : list) {
            String name = (String) m.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames", setmealNames);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);

    }

    //获取运营统计数据
    @RequestMapping(value = "/getBusinessReportData", method = RequestMethod.GET)
    public Result getBusinessReportData() {
        try {
            Map<String, Object> rsMap = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据统计报表
     */
    @RequestMapping(value = "/exportBusinessReport", method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.获取运营数据统计报表数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();
            //2.获取模板路径  File.separator 根据不同的系统返回相应目录符号
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //3.获取Excel模板对象 XSSFWorkbook：2007  HSSFWorkbook：2003
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //通过第三方模板技术  excel模板对象（例子：改造${reportData}） 模板对应数据map
            //针对poi开发模板技术  jxl
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook,businessReportData);
            //5.通过输出流返回页面下载本地磁盘
            OutputStream outputStream = response.getOutputStream();
            //设置文件名 文件类型
            //文件类型 告知浏览器返回2007 excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //attachment:以附件的形式下载 filename=report.xlsx:指定下载文件名
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);
            //6.释放资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return null;
    }

}
