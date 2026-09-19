<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Server Error — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="error-container">
    <div class="error-card">
        <div class="error-code">500</div>
        <h2>Server Error Encountered</h2>
        <p>An unexpected internal error occurred. Our team has been notified and stack traces are suppressed for security.</p>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Return to Marketplace</a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
