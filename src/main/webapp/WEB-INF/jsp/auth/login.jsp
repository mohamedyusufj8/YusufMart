<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Sign In — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <h2>Welcome Back</h2>
            <p>Sign in to your YusufMart account</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <c:out value="${errorMessage}" />
            </div>
        </c:if>
        <c:if test="${param.msg == 'logged_out'}">
            <div class="alert alert-success">
                You have been signed out safely.
            </div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">
                <c:out value="${param.error}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form" id="loginForm">
            <c:if test="${not empty param.redirect}">
                <input type="hidden" name="redirect" value="<c:out value='${param.redirect}'/>">
            </c:if>

            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" required placeholder="name@domain.com"
                       value="<c:out value='${email != null ? email : param.email}'/>">
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="••••••••">
            </div>

            <button type="submit" class="btn btn-primary btn-block">Sign In</button>
        </form>

        <div class="demo-accounts">
            <div class="demo-title">⚡ Quick 1-Click Demo Fill:</div>
            <div class="demo-btn-group">
                <button type="button" class="btn-demo" onclick="fillCredentials('buyer@yusufmart.com', 'Buyer@123')">
                    Buyer
                </button>
                <button type="button" class="btn-demo" onclick="fillCredentials('seller@yusufmart.com', 'Seller@123')">
                    Seller
                </button>
                <button type="button" class="btn-demo" onclick="fillCredentials('admin@yusufmart.com', 'Admin@123')">
                    Admin
                </button>
            </div>
        </div>

        <div class="auth-footer">
            Don't have an account yet? <a href="${pageContext.request.contextPath}/register">Create Account</a>
        </div>
    </div>
</div>

<script>
function fillCredentials(email, pass) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = pass;
}
</script>

<jsp:include page="../common/footer.jsp" />
