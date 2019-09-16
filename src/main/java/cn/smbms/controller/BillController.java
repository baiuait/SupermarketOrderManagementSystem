package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/bill")
@Controller
public class BillController {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private BillService billService;
    @Resource
    private ProviderService providerService;

    /**
     * 查询订单列表
     * @return
     */
    @RequestMapping("/billList")
    public String getBillList(Model model, String currentPageNo,
                              String queryProductName, String queryProviderId, String queryIsPayment){
        logger.info("get bill list ============");
        List<Provider> providerList = providerService.getProviderList("","");
        model.addAttribute("providerList", providerList);

        if(StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }

        Bill bill = new Bill();
        if(StringUtils.isNullOrEmpty(queryIsPayment)){
            bill.setIsPayment(0);
        }else{
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        if(StringUtils.isNullOrEmpty(queryProviderId)){
            bill.setProviderId(0);
        }else{
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);
        //实现分页
        Integer currPage = currentPageNo == null?0:Integer.parseInt(currentPageNo);
        Integer pageSize = 5;
        int totalCount = billService.getBillCount(bill);
        int totalPageCount = totalCount % pageSize == 0 ? totalCount/pageSize : totalCount/pageSize+1;
        //控制页首页尾
        if(currPage < 1){
            currPage = 1;
        }else if (currPage > totalPageCount){
            currPage = totalPageCount;
        }
        List<Bill> billList = billService.getBillList(bill.getProductName(), bill.getProviderId(), bill.getIsPayment(), currPage, pageSize);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        model.addAttribute("currentPageNo", currPage);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPageCount", totalPageCount);
//        request.getRequestDispatcher("billlist.jsp").forward(request, response);
        return "billlist";
    }

    /**
     * 前往订单添加页面
     */
    @RequestMapping("/goAdd")
    public String goAddPage(){
        logger.info("go add page===========");
        return "billadd";
    }

    /**
     * 执行订单添加操作
     */
    @RequestMapping("/saveAdd")
    public String saveAdd(Bill bill, HttpSession session){
        logger.info("save add ========");
        //添加创建日期和创建人
        bill.setCreationDate(new Date());
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        boolean result = billService.add(bill);
        if(result)
            return "redirect:/bill/billList";
        else
            return "billadd";
    }

    /**
     * 异步获取订单添加页面的供应商列表
     */
    @RequestMapping("/getProviders")
    @ResponseBody
    public String getProviders(){
        logger.info("get providers Ajax=======");
        List<Provider> providerList = providerService.getProviderList("", "");
        return JSON.toJSONString(providerList);
    }

    /**
     * 前往订单修改页面
     */
    @RequestMapping("/toModify")
    public String goToModifyPage(@RequestParam("billid") String id, Model model){
        logger.info("go to modify page=======");
        Bill bill = billService.getBillById(id);
        model.addAttribute(bill);
        return "billmodify";
    }

    /**
     * 修改订单操作
     */
    @RequestMapping("/saveModify")
    public String saveModify(Bill bill, HttpSession session){
        logger.info("do save modify bill");
        //添加更改人和更改时间
        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean result = billService.modify(bill);
        if (result)
            return "redirect:/bill/billList";
        return "billmodify";
    }

    /**
     * 查询订单详情
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model){
        logger.info("bill view======");
        Bill bill = billService.getBillById(id);
        model.addAttribute("bill", bill);
        return "billview";
    }

    /**
     * 使用异步删除订单
     */
    @RequestMapping("/deleteBill")
    @ResponseBody
    public String deleteBill(@RequestParam("billid")String id){
        logger.info("delete bill=====");
        Map<String,String> map = new HashMap<>();
        boolean result = billService.deleteBillById(id);
        map.put("delResult",result?"true":"false");
        return JSON.toJSONString(map);
    }
}
