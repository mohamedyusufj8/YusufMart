<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Orders — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="orders-page-container">
    <div class="orders-title-row">
        <h1>My Order History</h1>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-sm">Shop More</a>
    </div>

    <c:if test="${param.placed == 'true'}">
        <div class="alert alert-success">
            🎉 <strong>Order Placed Successfully!</strong> Your mock payment was authorized and inventory updated. Order reference: <strong>#<c:out value="${param.orderId}"/></strong>.
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="orders-stack">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-card-header">
                            <div class="order-meta-col">
                                <span class="meta-label">ORDER PLACED</span>
                                <span class="meta-val"><fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm"/></span>
                            </div>
                            <div class="order-meta-col">
                                <span class="meta-label">TOTAL AMOUNT</span>
                                <span class="meta-val">$<fmt:formatNumber value="${order.totalAmount}" pattern="0.00"/></span>
                            </div>
                            <div class="order-meta-col">
                                <span class="meta-label">ORDER ID</span>
                                <span class="meta-val">#<c:out value="${order.id}"/></span>
                            </div>
                            <div class="order-status-col">
                                <span class="status-badge status-<c:out value='${order.status}'/>">
                                    <c:out value="${order.status}"/>
                                </span>
                            </div>
                        </div>

                        <div class="order-items-body">
                            <c:forEach var="item" items="${order.items}">
                                <div class="order-item-entry">
                                    <c:if test="${not empty item.productImage}">
                                        <img src="<c:out value='${item.productImage}'/>" alt="<c:out value='${item.productName}'/>" class="order-thumb">
                                    </c:if>
                                    <div class="order-item-text">
                                        <h4><a href="${pageContext.request.contextPath}/product-detail?id=${item.productId}"><c:out value="${item.productName}"/></a></h4>
                                        <p>Quantity: <strong><c:out value="${item.quantity}"/></strong> &bull; Price: <strong>$<fmt:formatNumber value="${item.unitPrice}" pattern="0.00"/></strong> each</p>
                                    </div>
                                    <div class="order-item-action">
                                        <c:choose>
                                            <c:when test="${order.status == 'DELIVERED'}">
                                                <a href="${pageContext.request.contextPath}/product-detail?id=${item.productId}#reviews" class="btn btn-outline-sm">
                                                    ★ Rate & Review
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="muted-note">Review available once delivered</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-icon">📦</div>
                <h2>No Orders Yet</h2>
                <p>You have not placed any orders yet on YusufMart.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Start Shopping</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../common/footer.jsp" />
