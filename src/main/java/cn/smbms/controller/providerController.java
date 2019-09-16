package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RequestMapping("/provider")
@Controller
public class providerController {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private ProviderService providerService;

    //查询供应商列表
    @RequestMapping("/providerList")
    public String getProviderList(Model model, @RequestParam(required = false)String queryProName,
                                  @RequestParam(required = false)String queryProCode){
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<Provider> providerList = providerService.getProviderList(queryProName,queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "providerlist";
    }

    //前往供应商添加页面
    @RequestMapping(value = "/toAddProvider", method = RequestMethod.GET)
    public String toAddProviderPage(@ModelAttribute("provider")Provider provider){
        logger.info("TO ADD PROVIDER PAGE ======");
        return "provideradd";
    }

    //执行供应商添加操作
    @RequestMapping(value = "/toAddProvider", method = RequestMethod.POST)
    public String toAddProvider(@Valid Provider provider, HttpServletRequest request,
                                @RequestParam(value = "photos",required = false) MultipartFile[] photos,
                                BindingResult bindingResult, HttpSession session){
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        Boolean flag = true;
        for (int i = 0 ; i < photos.length ; i++){
            MultipartFile photo = photos[i];
            if(!photo.isEmpty()){ //存在执行,不存在不执行
                String errorObj = i == 0 ? "companyLicPicPathError" : "orgCodePicPathError";
                //获取系统路径
                String path = session.getServletContext().getRealPath("images"+ File.separator+ "providerFiles");
                //获取原文件名(判断后缀
                String oldFileName = photo.getOriginalFilename();
                //判断文件后缀
                if(oldFileName.endsWith(".jpg") || oldFileName.endsWith(".png")){
                    //判断文件大小
                    if(photo.getSize() > 500000){
                        flag = false;
                        request.setAttribute(errorObj, "文件不能大于500KB");
                        continue;
                    }
                    //生成文件名
                    String fileName = new Random().nextInt(1000000)+"_Provider.jpg";
                    File file = new File(path, fileName);
                    //判断文件是否存在
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    try {
                        //转移文件
                        photo.transferTo(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        flag = false;
                        request.setAttribute(errorObj, "文件上传失败!");
                    }
                    if (i == 0)
                        companyLicPicPath = path + File.separator + fileName;
                    else
                        orgCodePicPath = path + File.separator + fileName;
                }else{
                    flag = false;
                    request.setAttribute(errorObj, "文件格式只能为.jpg,.png");
                }
            }
        }

        if(flag){
            logger.info("ADD PROVIDER NOW=====");
            if (bindingResult.hasErrors()){
                logger.debug("ADD PROVIDER ERROR =====");
                return "provideradd";
            }
            Integer nowUserId = ((User)(session.getAttribute(Constants.USER_SESSION))).getId();
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            provider.setCreatedBy(nowUserId);
            provider.setCreationDate(new Date());
            boolean result = providerService.add(provider);
            if(result){
                return "redirect:/provider/providerList";
            }
            return "provideradd";
        }
        return "provideradd";
    }

    //查看供应商详细信息
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String showView(@PathVariable String id, Model model){
        logger.info("SHOW PROVIDER VIEW NOW ID ========== "+id);
        Provider provider = providerService.getProviderById(id);
        model.addAttribute("provider", provider);
        return "providerview";
    }

    //前往修改供应商信息页面
    @RequestMapping(value = "/toSaveModify/{id}", method = RequestMethod.GET)
    public String toSaveModifyPage(@PathVariable String id, Model model){
        logger.info("GO TO MODIFY PAGE BY PROVIDER ======");
        Provider provider = providerService.getProviderById(id);
        model.addAttribute("provider",provider);
        return "providermodify";
    }

    //修改供应商信息
    @RequestMapping(value = "/saveModify", method = RequestMethod.POST)
    public String saveModify(Provider provider, HttpSession session){
        logger.info("SAVE MODIFY NOW======");
        Integer nowUserId = ((User)(session.getAttribute(Constants.USER_SESSION))).getId();
        provider.setModifyBy(nowUserId);
        provider.setModifyDate(new Date());
        boolean result = providerService.modify(provider);
        if(result){
            logger.info("SAVE MODIFY SUCCESS=====");
            return "redirect:/provider/providerList";
        }
        logger.info("SAVE MODIFY ERROR");
        return "providermodify";
    }

    //删除供应商信息
    @RequestMapping("/deleteProvider")
    @ResponseBody
    public String deleteProvider(@RequestParam("proId") String id){
        //判断是否有图片需要删除
        Provider provider = providerService.getProviderById(id);
        //获取图片路径
        String[] photos = {provider.getOrgCodePicPath(), provider.getCompanyLicPicPath()};
        System.out.println(photos[0]+"::::::::::::"+photos[1]);
        List<File> files = new ArrayList<>();
        for (String photo : photos) {
            if (null != photo)
                files.add(new File(photo));
        }
        for (File file : files) {
            if (file.exists())
                file.delete();
        }
        //删除图片后执行删除操作
        Map<String,String> map = new HashMap<>();
        int delResult = providerService.deleteProviderById(id);
        if(delResult != 0)
            map.put("delResult",delResult+"");
        else if (delResult == -1)
            map.put("delResult","false");
        else if (delResult == 0)
            map.put("delResult","true");
        else
            map.put("delResult","notexist");
        return JSON.toJSONString(map);
    }
}
