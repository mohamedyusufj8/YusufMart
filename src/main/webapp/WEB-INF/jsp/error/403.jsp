<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Access Forbidden — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="error-container">
    <div class="error-card">
        <div class="error-code">403</div>
        <h2>Access Denied</h2>
        <p>You do not possess the necessary role privileges to view this area.</p>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Switch Account</a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Go to Catalog</a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
