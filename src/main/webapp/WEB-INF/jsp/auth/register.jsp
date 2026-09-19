<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create Account — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <h2>Create Account</h2>
            <p>Join YusufMart as a Buyer or Seller</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
            <div class="form-group">
                <label for="name">Full Name</label>
                <input type="text" id="name" name="name" required placeholder="e.g. Mohamed Yusuf"
                       value="<c:out value='${name}'/>">
            </div>

            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" required placeholder="name@domain.com"
                       value="<c:out value='${email}'/>">
            </div>

            <div class="form-group">
                <label for="password">Password (min. 6 characters)</label>
                <input type="password" id="password" name="password" minlength="6" required placeholder="••••••••">
            </div>

            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" minlength="6" required placeholder="••••••••">
            </div>

            <div class="form-group">
                <label>I want to join as:</label>
                <div class="role-selector">
                    <label class="role-option">
                        <input type="radio" name="role" value="BUYER" checked>
                        <div class="role-box">
                            <span class="role-title">🛒 Buyer</span>
                            <span class="role-desc">Browse, buy products & write reviews</span>
                        </div>
                    </label>

                    <label class="role-option">
                        <input type="radio" name="role" value="SELLER">
                        <div class="role-box">
                            <span class="role-title">💼 Seller</span>
                            <span class="role-desc">List products & fulfill customer orders</span>
                        </div>
                    </label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Complete Registration</button>
        </form>

        <div class="auth-footer">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Sign In</a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
