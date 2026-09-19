<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Shopping Cart — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="cart-page-container">
    <div class="cart-title-row">
        <h1>Your Shopping Cart</h1>
        <a href="${pageContext.request.contextPath}/products" class="btn-link">&larr; Continue Shopping</a>
    </div>

    <c:if test="${not empty sessionScope.cartError}">
        <div class="alert alert-danger">
            <c:out value="${sessionScope.cartError}" />
        </div>
        <c:remove var="cartError" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div class="cart-layout">
                <!-- Cart Items Table -->
                <div class="cart-items-card">
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td class="product-cell">
                                        <div class="cart-item-preview">
                                            <c:if test="${not empty item.product.imageUrl}">
                                                <img src="<c:out value='${item.product.imageUrl}'/>" alt="<c:out value='${item.product.name}'/>" class="cart-thumb">
                                            </c:if>
                                            <div class="cart-item-details">
                                                <h4><a href="${pageContext.request.contextPath}/product-detail?id=${item.productId}"><c:out value="${item.product.name}" /></a></h4>
                                                <span class="muted">Sold by: <c:out value="${item.product.sellerName}" /></span>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="price-cell">
                                        $<fmt:formatNumber value="${item.product.price}" pattern="0.00"/>
                                    </td>
                                    <td class="qty-cell">
                                        <form action="${pageContext.request.contextPath}/cart/update" method="post" class="qty-form">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <div class="qty-controller">
                                                <button type="submit" name="quantity" value="${item.quantity - 1}" class="btn-qty" <c:if test="${item.quantity <= 1}">disabled</c:if>>-</button>
                                                <span class="qty-val"><c:out value="${item.quantity}" /></span>
                                                <button type="submit" name="quantity" value="${item.quantity + 1}" class="btn-qty" <c:if test="${item.quantity >= item.product.stockQty}">disabled</c:if>>+</button>
                                            </div>
                                        </form>
                                    </td>
                                    <td class="subtotal-cell">
                                        <strong>$<fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></strong>
                                    </td>
                                    <td class="action-cell">
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <button type="submit" class="btn-remove" title="Remove Item">&times;</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Order Summary Card -->
                <div class="cart-summary-card">
                    <h3>Order Summary</h3>
                    <div class="summary-line">
                        <span>Items Subtotal</span>
                        <span>$<fmt:formatNumber value="${cartTotal}" pattern="0.00"/></span>
                    </div>
                    <div class="summary-line">
                        <span>Estimated Shipping</span>
                        <span class="free-tag">FREE</span>
                    </div>
                    <div class="summary-divider"></div>
                    <div class="summary-line total-line">
                        <strong>Order Total</strong>
                        <strong class="total-amount">$<fmt:formatNumber value="${cartTotal}" pattern="0.00"/></strong>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-block btn-lg">
                        Proceed to Checkout &rarr;
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-icon">🛒</div>
                <h2>Your Shopping Cart is Empty</h2>
                <p>Explore thousands of products listed by verified sellers and add items to your cart.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Start Shopping</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../common/footer.jsp" />
