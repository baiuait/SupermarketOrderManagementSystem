var roleCode = null;
var roleName = null;
var addBtn = null;
var backBtn = null;

$(function(){
    roleCode = $("#roleCode");
    roleName = $("#roleName");
    addBtn = $("#add");
    backBtn = $("#back");
    //初始化的时候，要把所有的提示信息变为：* 以提示必填项，更灵活，不要写在页面上
    roleCode.next().html("*");
    roleName.next().html("*");



    /*
     * 验证
     * 失焦\获焦
     * jquery的方法传递
     */
    roleCode.on("blur",function(){
        $.getJSON(path+"/role/checkCode?roleCode="+roleCode.val(),function(data){
            if(data.result == "true"){
                validateTip(roleCode.next(),{"color":"green"},imgYes,true);
            }else{
                validateTip(roleCode.next(),{"color":"red"},imgNo+" 编码已经存在，请重新输入",false);
            }
            if(roleCode.val() == null || roleCode.val() == ""){
                validateTip(roleCode.next(),{"color":"red"},imgNo+" 编码不能为空，请重新输入",false);
            }
        })
    }).on("focus",function(){
        //显示友情提示
        validateTip(roleCode.next(),{"color":"#666666"},"* 请输入角色编码",false);
    }).focus();

    roleName.on("focus",function(){
        validateTip(roleName.next(),{"color":"#666666"},"* 请输入角色名称",false);
    }).on("blur",function(){
        if(roleName.val() != null && roleName.val() != ""){
            validateTip(roleName.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(roleName.next(),{"color":"red"},imgNo+" 角色名称不能为空，请重新输入",false);
        }
    });

    addBtn.on("click",function(){
        if(roleCode.attr("validateStatus") != "true"){
            roleCode.blur();
        }else if(roleName.attr("validateStatus") != "true"){
            roleName.blur();
        }else{
            if(confirm("是否确认提交数据")){
                $("#billForm").submit();
            }
        }
    });

    backBtn.on("click",function(){
        if(referer != undefined
            && null != referer
            && "" != referer
            && "null" != referer
            && referer.length > 4){
            window.location.href = referer;
        }else{
            history.back(-1);
        }
    });
});