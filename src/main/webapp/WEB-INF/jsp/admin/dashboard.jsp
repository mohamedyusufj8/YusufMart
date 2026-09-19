<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Admin Oversight Panel — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="admin-container">
    <div class="dashboard-header-row">
        <div>
            <h1>Admin Oversight & Moderation Center</h1>
            <p class="subtitle">Platform administration, user management, and catalog moderation (Section 1 F7)</p>
        </div>
    </div>

    <c:if test="${param.moderated == 'true'}">
        <div class="alert alert-success">Administrative action processed successfully.</div>
    </c:if>

    <!-- Metrics Cards -->
    <div class="metrics-grid">
        <div class="metric-card">
            <span class="metric-icon">👥</span>
            <div class="metric-content">
                <span class="metric-label">Total Users</span>
                <span class="metric-value"><c:out value="${userCount}"/></span>
            </div>
        </div>

        <div class="metric-card">
            <span class="metric-icon">📦</span>
            <div class="metric-content">
                <span class="metric-label">Live Listings</span>
                <span class="metric-value"><c:out value="${productCount}"/></span>
            </div>
        </div>

        <div class="metric-card">
            <span class="metric-icon">📑</span>
            <div class="metric-content">
                <span class="metric-label">System Orders</span>
                <span class="metric-value"><c:out value="${orderCount}"/></span>
            </div>
        </div>

        <div class="metric-card">
            <span class="metric-icon">💰</span>
            <div class="metric-content">
                <span class="metric-label">Platform Revenue</span>
                <span class="metric-value">$<fmt:formatNumber value="${totalRevenue}" pattern="0.00"/></span>
            </div>
        </div>
    </div>

    <!-- Product Moderation Section -->
    <div class="card table-card">
        <h3>Catalog Moderation (Moderate / Remove Listings)</h3>
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Product Title</th>
                    <th>Seller</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>#<c:out value="${p.id}"/></td>
                        <td>
                            <strong><a href="${pageContext.request.contextPath}/product-detail?id=${p.id}"><c:out value="${p.name}"/></a></strong>
                        </td>
                        <td><c:out value="${p.sellerName}"/></td>
                        <td><span class="category-pill"><c:out value="${p.category}"/></span></td>
                        <td>$<fmt:formatNumber value="${p.price}" pattern="0.00"/></td>
                        <td><c:out value="${p.stockQty}"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/products/delete" method="post" style="display:inline;" onsubmit="return confirm('Remove listing permanently?');">
                                <input type="hidden" name="productId" value="${p.id}">
                                <button type="submit" class="btn btn-danger-sm">Moderate & Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- User Management Section -->
    <div class="card table-card">
        <h3>Registered Platform Users</h3>
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Registered At</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>#<c:out value="${u.id}"/></td>
                        <td><strong><c:out value="${u.name}"/></strong></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><span class="role-badge role-<c:out value='${u.role}'/>"><c:out value="${u.role}"/></span></td>
                        <td><fmt:formatDate value="${u.createdAt}" pattern="MMM dd, yyyy"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.id == sessionScope.currentUser.id}">
                                    <span class="muted">(Current Admin)</span>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/users/delete" method="post" style="display:inline;" onsubmit="return confirm('Delete this user account?');">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <button type="submit" class="btn btn-danger-sm">Delete Account</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Orders Oversight Section -->
    <div class="card table-card">
        <h3>System Orders Oversight</h3>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Order #</th>
                    <th>Date</th>
                    <th>Buyer</th>
                    <th>Status</th>
                    <th>Total Amount</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td><strong>#<c:out value="${o.id}"/></strong></td>
                        <td><fmt:formatDate value="${o.createdAt}" pattern="MMM dd, yyyy HH:mm"/></td>
                        <td><c:out value="${o.buyerName}"/> (<c:out value="${o.buyerEmail}"/>)</td>
                        <td><span class="status-badge status-<c:out value='${o.status}'/>"><c:out value="${o.status}"/></span></td>
                        <td><strong>$<fmt:formatNumber value="${o.totalAmount}" pattern="0.00"/></strong></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
