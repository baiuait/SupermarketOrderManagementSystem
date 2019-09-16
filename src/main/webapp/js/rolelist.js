var roleObj;

//用户管理页面上点击删除按钮弹出删除框(userlist.jsp)
function deleteUser(obj){
    $.ajax({
        type:"GET",
        url:path+"/role/deleteRole",
        data:{"id":obj.attr("roleid")},
        dataType:"json",
        success:function(data){
            if(data.delResult == "true"){//删除成功：移除删除行
                obj.parents("tr").remove();
                cancleBtn();
            }else if(data.delResult == "false"){//删除失败
                //alert("对不起，删除用户【"+obj.attr("username")+"】失败");
                changeDLGContent("对不起，删除角色【"+obj.attr("rolename")+"】失败");
            }else if(data.delResult == "hasUsers"){
                //alert("对不起，用户【"+obj.attr("username")+"】不存在");
                changeDLGContent("对不起，角色【"+obj.attr("rolename")+"】下还有用户,无法删除");
            }
        },
        error:function(data){
            //alert("对不起，删除失败");
            changeDLGContent("对不起，删除失败");
        }
    });
}

function openYesOrNoDLG(){
    $('.zhezhao').css('display', 'block');
    $('#removeUse').fadeIn();
}

function cancleBtn(){
    $('.zhezhao').css('display', 'none');
    $('#removeUse').fadeOut();
}
function changeDLGContent(contentStr){
    var p = $(".removeMain").find("p");
    p.html(contentStr);
}

$(function(){
    //通过jquery的class选择器（数组）
    //对每个class为viewUser的元素进行动作绑定（click）
    /**
     * bind、live、delegate
     * on
     */
    $(".viewUser").on("click",function(){
        var obj = $(this);
        window.location.href=path+"/role/view/"+obj.attr("roleid");
    })
    $(".modifyUser").on("click",function(){
        var obj = $(this);
        window.location.href=path+"/role/goModify?id="+ obj.attr("roleid");
    });

    $('#no').click(function () {
        cancleBtn();
    });

    $('#yes').click(function () {
        deleteUser(roleObj);
    });

    $(".deleteUser").on("click",function(){
        roleObj = $(this);
        changeDLGContent("你确定要删除角色【"+roleObj.attr("rolename")+"】吗？");
        openYesOrNoDLG();
    });
});