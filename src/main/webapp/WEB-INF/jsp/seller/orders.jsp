<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Incoming Orders — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="seller-container">
    <div class="dashboard-header-row">
        <div>
            <h1>Incoming Customer Orders</h1>
            <p class="subtitle">Track customer purchases and advance fulfillment status (Section 1 F6 & O2)</p>
        </div>
        <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline-sm">&larr; Back to Listings</a>
    </div>

    <c:if test="${param.updated == 'true'}">
        <div class="alert alert-success">Order status updated successfully!</div>
    </c:if>

    <div class="card table-card">
        <c:choose>
            <c:when test="${not empty orders}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Order #</th>
                            <th>Date</th>
                            <th>Buyer Details</th>
                            <th>Products & Quantities</th>
                            <th>Current Status</th>
                            <th>Advance Workflow</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td><strong>#<c:out value="${order.id}"/></strong></td>
                                <td><fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm"/></td>
                                <td>
                                    <strong><c:out value="${order.buyerName}"/></strong><br>
                                    <span class="muted"><c:out value="${order.buyerEmail}"/></span>
                                </td>
                                <td>
                                    <ul class="order-items-compact">
                                        <c:forEach var="item" items="${order.items}">
                                            <li>
                                                <c:out value="${item.productName}"/>
                                                &times; <strong><c:out value="${item.quantity}"/></strong>
                                                ($<fmt:formatNumber value="${item.unitPrice}" pattern="0.00"/>)
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </td>
                                <td>
                                    <span class="status-badge status-<c:out value='${order.status}'/>">
                                        <c:out value="${order.status}"/>
                                    </span>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/seller/orders/status" method="post" class="status-update-form">
                                        <input type="hidden" name="orderId" value="${order.id}">
                                        <select name="status" class="status-select">
                                            <option value="PENDING" <c:if test="${order.status == 'PENDING'}">selected</c:if>>Pending</option>
                                            <option value="CONFIRMED" <c:if test="${order.status == 'CONFIRMED'}">selected</c:if>>Confirmed</option>
                                            <option value="SHIPPED" <c:if test="${order.status == 'SHIPPED'}">selected</c:if>>Shipped</option>
                                            <option value="DELIVERED" <c:if test="${order.status == 'DELIVERED'}">selected</c:if>>Delivered</option>
                                            <option value="CANCELLED" <c:if test="${order.status == 'CANCELLED'}">selected</c:if>>Cancelled</option>
                                        </select>
                                        <button type="submit" class="btn btn-outline-sm">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>No incoming orders yet for your listed products.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
