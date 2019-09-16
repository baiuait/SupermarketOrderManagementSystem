var roleName = null;
var addBtn = null;
var backBtn = null;

$(function(){
    roleName = $("#roleName");
    addBtn = $("#save");
    backBtn = $("#back");
    //初始化的时候，要把所有的提示信息变为：* 以提示必填项，更灵活，不要写在页面上
    roleName.next().html("*");

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
        if(roleName.attr("validateStatus") != "true"){
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