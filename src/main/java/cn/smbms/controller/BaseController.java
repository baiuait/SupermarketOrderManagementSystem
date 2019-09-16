package cn.smbms.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用@InitBinder解决SpringMVC日期类型无法绑定的问题
 */
public class BaseController {
    //标注了@InitBinder注解的方法会在控制器初始化时调用
    //在initBinder()方法体内,通过dataBinder的registerCustomEditor()方法注册一个自定义编辑器:第一个参数表示编辑器为日期类型(Date.class);
    //第二个参数表示使用自定义的日期编辑器(CustomDateEditor),时间格式为yyyy-MM-dd,true表示允许为空(该参数是否允许为空)
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        dataBinder.registerCustomEditor
                (Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
