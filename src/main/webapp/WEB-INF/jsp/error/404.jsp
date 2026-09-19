<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="error-container">
    <div class="error-card">
        <div class="error-code">404</div>
        <h2>Page Not Found</h2>
        <p>The page or product you were looking for doesn't exist or has been moved.</p>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Back to Catalog</a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
