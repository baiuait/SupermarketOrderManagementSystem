package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
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

@RequestMapping("/user")
@Controller
public class UserController extends BaseController{
    private Logger logger = LogManager.getLogger(this.getClass());

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "roleService")
    private RoleService roleService;

    //多条件查询用户列表
    @RequestMapping("/userList")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname",required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole",required = false) String queryUserRole,
                              @RequestParam(value = "pageIndex",required = false) String pageIndex){
        logger.info("getUserList ----> queryUserName:" + queryUserName);
        logger.info("getUserList ----> queryUserRole:" + queryUserRole);
        logger.info("getUserList ----> pageIndex:" + pageIndex);
        Integer _queryUserRole = 0;
        List<User> userList;
        //设置页面容量
        Integer pageSize = Constants.pageSize;
        //当前页面
        Integer currentPageNo = 1;
        if(null == queryUserName)
            queryUserName = "";
        if(null != queryUserRole && !"".equals(queryUserRole))
            _queryUserRole = Integer.parseInt(queryUserRole);
        if(null != pageIndex){
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            }catch (NumberFormatException e){
                return "redirect:/user/syserror";
            }
        }
        //总数量(表)
        Integer totalCount = userService.getUserCount(queryUserName, _queryUserRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        Integer totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1)
            currentPageNo = 1;
        else if(currentPageNo > totalPageCount)
            currentPageNo = totalPageCount;
        //获取user集合
        userList = userService.getUserList(queryUserName, _queryUserRole, currentPageNo, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    //系统错误页面
    @RequestMapping("/syserror")
    public String sysError(){
        return "syserror";
    }

    //前往添加用户页面
    @RequestMapping(value = "/toAddUser", method = RequestMethod.GET)
    public String toAddUserPage(@ModelAttribute("user") User user){
        logger.info("GO TO ADD USER PAGE ======");
        return "useradd";
    }

    //多文件上传
    @RequestMapping(value = "/toAddUser" ,method = RequestMethod.POST)
    public String AddUser(@Valid User user, BindingResult bindingResult, HttpSession session, HttpServletRequest request,
                          @RequestParam(value = "photo", required = false)MultipartFile[] photos){
        String idPicPath = "";
        String workPicPath = "";
        String errorInfo;
        Boolean flag = true;
        String path = session.getServletContext().getRealPath("images"+File.separator+"uploadFiles");
        logger.info("uploadFile path ========> "+path);
        for (int i = 0 ; i < photos.length ; i++){
            MultipartFile photo = photos[i];
            if(!photo.isEmpty()){
                errorInfo = i == 0? "uploadFileError":"uploadWpError"; //判断上传的是哪一张图片,用于提示错误信息
                String oldFileName = photo.getOriginalFilename(); //移动前原文件名
                logger.info("uploadFile oldName =======> "+oldFileName);
                String suffix = FilenameUtils.getExtension(oldFileName); //文件后缀名
                logger.debug("uploadFile suffix =======> "+suffix);
                Integer fileSize = 500000;
                Long oldFileSize = photo.getSize(); //文件大小
                logger.debug("uploadFile size ======> "+oldFileSize);
                //上传文件大小不能超过500kb
                if(oldFileSize > fileSize){
                    request.setAttribute(errorInfo, "* 上传大小不得超过500KB");
                    flag = false;
                }else if (suffix.equalsIgnoreCase("jpg")||suffix.equalsIgnoreCase("png")){ //判断上传文件格式
                    Random random = new Random();
                    String fileName = System.currentTimeMillis()+ random.nextInt(1000000)+ "_Personal.jpg";
                    logger.debug("new fileName ======> "+ fileName);
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    try {
                        //执行保存操作
                        photo.transferTo(targetFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, "* 上传失败!");
                        flag = false;
                    }
                    if (i == 0)
                        idPicPath = path+File.separator+fileName;
                    else
                        workPicPath = path+File.separator+fileName;
                }else {
                    request.setAttribute(errorInfo, "* 上传图片格式不正确");
                    flag = false;
                }
            }
        }

        //判断两张照片是否都上传成功
        if(flag){
            //判断数据校验是否异常
            if(bindingResult.hasErrors()){
                logger.debug("ADD USER VALIDATED HAS ERROR========");
                return "useradd";
            }
            Integer nowUserId = ((User)(session.getAttribute(Constants.USER_SESSION))).getId();
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            user.setCreatedBy(nowUserId);
            user.setCreationDate(new Date());
            boolean add = userService.add(user);
            if(add){
                return "redirect:/user/userList";
            }
            return "useradd";
        }
        return "useradd";
    }

    //单文件上传
    //执行添加用户操作 //@Valid注解可以让SpringMVC完成数据绑定之后,执行数据校验的工作,后面必须跟上BindingResult对象,否则抛出异常
//    @RequestMapping(value = "/toAddUser" ,method = RequestMethod.POST)
//    public String AddUser(@Valid User user, BindingResult bindingResult, HttpSession session, HttpServletRequest request,
//                          @RequestParam(value = "a_idPicPath", required = false)MultipartFile attach){
//        logger.info("DO ADD USER NOW ===========");
//        //文件不为空执行操作.
//        String idPicPathData = "";
//        if(!attach.isEmpty()){
//            String path = session.getServletContext().getRealPath("images"+File.separator+"uploadFiles"); //要保存到的系统目录 File.separator为分隔符
//            logger.info("uploadFile path ========> "+path);
//            String oldFileName = attach.getOriginalFilename(); //移动前原文件名
//            logger.info("uploadFile oldName =======> "+oldFileName);
//            String suffix = FilenameUtils.getExtension(oldFileName); //文件后缀名
    //        Boolean suffix = oldFileName.endsWith(".jpg"); //使用endsWith(str)判断字符串是否以str结尾
//            logger.debug("uploadFile suffix =======> "+suffix);
//            Integer fileSize = 500000;
//            Long oldFileSize = attach.getSize(); //文件大小
//            logger.debug("uploadFile size ======> "+oldFileSize);
//            //上传文件大小不能超过500kb
//            if(oldFileSize > fileSize){
//                request.setAttribute("uploadFileError", "* 上传大小不得超过500KB");
//                return "useradd";
//            }else if (suffix.equalsIgnoreCase("jpg")||suffix.equalsIgnoreCase("png")){ //判断上传文件格式
//                Random random = new Random();
//                String fileName = System.currentTimeMillis()+ random.nextInt(1000000)+ "_Personal.jpg";
//                logger.debug("new fileName ======> "+ fileName);
//                File targetFile = new File(path, fileName);
//                if(!targetFile.exists()){
//                    targetFile.mkdirs();
//                }
//                try {
//                    //执行保存操作
//                    attach.transferTo(targetFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    request.setAttribute("uploadFileError", "* 上传失败!");
//                    return "useradd";
//                }
//                idPicPathData = path+File.separator+fileName;
//            }else {
//                request.setAttribute("uploadFileError", "* 上传图片格式不正确");
//                return "useradd";
//            }
//        }
//
//        //判断数据校验是否异常
//        if(bindingResult.hasErrors()){
//            logger.debug("ADD USER VALIDATED HAS ERROR========");
//            return "useradd";
//        }
//        Integer nowUserId = ((User)(session.getAttribute(Constants.USER_SESSION))).getId();
//        user.setIdPicPath(idPicPathData);
//        user.setCreatedBy(nowUserId);
//        user.setCreationDate(new Date());
//        boolean add = userService.add(user);
//        if(add){
//            return "redirect:/user/userList";
//        }
//        return "useradd";
//    }

    //修改用户信息(根据id获取用户信息并跳转至修改页面)
    @RequestMapping(value = "/toUserModify", method = RequestMethod.GET)
        public String getUserById(@RequestParam String uid, Model model){
        logger.info("TO USER MODIFY --> GET USER BY ID UID====="+uid);
        User user = userService.getUserById(uid);
        model.addAttribute("user",user);
        return "usermodify";
    }

    //修改用户信息(保存修改信息)
    @RequestMapping(value = "/userModify", method = RequestMethod.POST)
    public String saveUserModify(User user, HttpSession session){
        logger.info("SAVE USER MODIFY NOW======");
        Integer nowUserId = ((User)(session.getAttribute(Constants.USER_SESSION))).getId();
        user.setCreatedBy(nowUserId);
        user.setCreationDate(new Date());
        boolean modify = userService.modify(user);
        if (modify){
            logger.info("SAVE USER MODIFY SUCCESS======");
            return "redirect:/user/userList";
        }
        logger.info("SAVE USER MODIFY ERROR======"+user.getId());
        return "usermodify";
    }

    @GetMapping(value = "/view/{id}")
    public String view(@PathVariable String id, Model model){
        logger.info("SHOW USER VIEW AND ID ======= "+id);
        User user = userService.getUserById(id);
        model.addAttribute(user);
        return "userview";
    }

    @RequestMapping("/userExist")
    @ResponseBody
    public Object userCodeIsExist(@RequestParam String userCode){
        logger.debug("userCodeIsExist userCode:"+userCode);
        Map<String , String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode", "exist");
        }else{
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode", "exist");
            }else{
                resultMap.put("userCode", "noExist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    //解决JSON数据传递的字符乱码问题(使用produces属性) 使用配置文件配置日期格式的返回值类型必须为对象
    @RequestMapping(value = "/view", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object showViewUnderList(@RequestParam String id){
        logger.debug("showViewUnderList id:"+id);
        if(null == id || "".equals(id)){
            return "nodata";
        }
        User user = userService.getUserById(id);
        return user;
//        String json = JSON.toJSONString(user, SerializerFeature.WriteDateUseDateFormat); //json字符串:转换日期格式为yyyy-MM-dd HH:mm:ss
//        logger.debug("json:"+json);
//        return json;
    }

    //前往密码修改页面
    @RequestMapping("/goToModifyPwd")
    public String goToModifyPwd(){
        return "pwdmodify";
    }

    //异步判断修改密码的原密码是否正确
    @RequestMapping("/checkOldPwd")
    @ResponseBody
    public String checkOldPwd(@RequestParam(required = false) String pwd, HttpSession session){
        logger.debug("check old pwd ====");
        //获取session对象中的user对象
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        Map<String, String> map = new HashMap<>();
        //判断密码是否为空
        if(null == pwd || "".equals(pwd))
            map.put("result","error");
        //判断用户session是否过期
        else if (null == user)
            map.put("result","sessionerror");
        //判断两个密码是否一致
        else if(user.getUserPassword().equals(pwd))
            map.put("result","true");
        else
            map.put("result","false");
        return JSON.toJSONString(map);
    }

    //修改密码
    @RequestMapping("/modifyPwd")
    public String modifyPwd(@RequestParam(value = "newpassword") String newPwd, HttpSession session, HttpServletRequest request){
        //获取session对象中的user对象
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        boolean updatePwd = userService.updatePwd(user.getId(), newPwd);
        if (updatePwd) {
            return "redirect:/user/userList";
        }
        request.setAttribute("message","服务器异常,请稍后重试");
        return "pwdmodify";
    }

    //异步请求获取添加时的用户列表
    @RequestMapping("/getAllRole")
    @ResponseBody
    public String getAllRoleSelect(){
        logger.error("get all role select ===");
        List<Role> roleList = roleService.getRoleList();
        return JSON.toJSONString(roleList);
    }

    //异步删除用户信息
    @RequestMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(@RequestParam String id){
        //先根据id获取用户对象 , 判断并删除用户上传的图片
        User user = userService.getUserById(id);
        //获取两个图片路径
        String[] photos = {user.getIdPicPath(),user.getWorkPicPath()};
        System.out.println(photos[0]+":::::"+photos[1]);
        List<File> files = new ArrayList<>();
        for (String photo : photos) {
            if (null != photo)
                files.add(new File(photo));
        }
        for (File file : files) {
            if(file.exists())
                file.delete();
        }
        //删除图片后删除数据库中的用户信息
        boolean result = userService.deleteUserById(Integer.parseInt(id));
        Map<String , String> map = new HashMap<>();
        if(result)
            map.put("delResult","true");
        else if(null == user)
            map.put("delResult","notexist");
        else
            map.put("delResult","false");
        return JSON.toJSONString(map);
    }
}
