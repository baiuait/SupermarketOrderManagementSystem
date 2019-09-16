package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
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

@RequestMapping("/role")
@Controller
public class RoleController {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private RoleService roleService;

    /**
     * 查询所有角色列表
     */
    @RequestMapping("/roleList")
    public String roleList(Model model){
        logger.info("role list=====");
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "rolelist";
    }

    /**
     * 前往新增角色页面
     */
    @RequestMapping("/goAdd")
    public String goToAddPage(){
        logger.info("go to add page=====");
        return "roleadd";
    }

    /**
     * 异步验证角色编码唯一性
     */
    @RequestMapping("/checkCode")
    @ResponseBody
    public String checkCode(@RequestParam("roleCode") String roleCode){
        logger.info("check code exist====="+roleCode);
        Role role = roleService.getRoleByRoleCode(roleCode);
        Map<String, String> map = new HashMap<>();
        map.put("result", role == null? "true":"false");
        return JSON.toJSONString(map);
    }

    /**
     * 保存角色信息
     */
    @RequestMapping("/saveAdd")
    public String saveAdd(Role role, HttpSession session){
        logger.info("save role add======");
        role.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        role.setCreationDate(new Date());
        Boolean result = roleService.saveRole(role);
        if (result)
            return "redirect:/role/roleList";
        return "roleadd";
    }

    /**
     * 前往修改角色页面
     */
    @RequestMapping("/goModify")
    public String goToModifyPage(Model model, @RequestParam("id")String id){
        logger.info("go modify page=====");
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        return "rolemodify";
    }

    /**
     * 修改角色操作
     */
    @RequestMapping("/saveModify")
    public String saveModify(Role role, HttpSession session){
        logger.info("save modify role=====");
        role.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        role.setModifyDate(new Date());
        Boolean result = roleService.modifyRole(role);
        if(result)
            return "redirect:/role/roleList";
        return "rolemodify";
    }

    /**
     * 异步删除角色操作
     */
    @RequestMapping("/deleteRole")
    @ResponseBody
    public String deleteRole(@RequestParam("id")String id){
        logger.info("delete role Ajax========"+id);
        Integer result = roleService.deleteRole(id);
        Map<String,String> map = new HashMap<>();
        if (result == 0)
            map.put("delResult", "true");
        else if(result == -1)
            map.put("delResult", "false");
        else
            map.put("delResult", "hasUsers");
        return JSON.toJSONString(map);
    }

    /**
     * 查看角色视图
     */
    @RequestMapping("/view/{id}")
    public String view(Model model, @PathVariable String id){
        logger.info("role view=====");
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        return "roleview";
    }
}
