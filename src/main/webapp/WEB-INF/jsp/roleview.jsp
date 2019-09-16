<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>
<div class="right">
    <div class="location">
        <strong>你现在所在的位置是:</strong>
        <span>角色管理页面 >> 信息查看</span>
    </div>
    <div class="providerView">
        <p><strong>角色编号：</strong><span>${role.roleCode }</span></p>
        <p><strong>角色名称：</strong><span>${role.roleName }</span></p>
        <p><strong>创建时间：</strong><span>${role.creationDate }</span></p>
        <div class="providerAddBtn">
            <input type="button" id="back" name="back" value="返回" >
        </div>
    </div>
</div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/billview.js"></script>