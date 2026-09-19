<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Mock Checkout — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="checkout-page-container">
    <h1>Complete Your Purchase</h1>
    <p class="subtitle">Mock Payment & Order Confirmation (Anna University Specification Compliant)</p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            <c:out value="${errorMessage}" />
        </div>
    </c:if>

    <div class="checkout-grid">
        <!-- Payment & Delivery Form -->
        <div class="checkout-form-column">
            <form action="${pageContext.request.contextPath}/checkout/process" method="post" id="checkoutForm" class="checkout-form">
                
                <!-- Shipping Address Section -->
                <div class="checkout-card">
                    <h3>1. Delivery Address</h3>
                    <div class="form-group">
                        <label for="shippingName">Recipient Name</label>
                        <input type="text" id="shippingName" name="shippingName" required value="<c:out value='${sessionScope.currentUser.name}'/>">
                    </div>
                    <div class="form-group">
                        <label for="street">Street Address</label>
                        <input type="text" id="street" name="street" required placeholder="123 University Campus Avenue">
                    </div>
                    <div class="form-row">
                        <div class="form-group col-6">
                            <label for="city">City</label>
                            <input type="text" id="city" name="city" required value="Chennai">
                        </div>
                        <div class="form-group col-6">
                            <label for="pincode">Postal Code</label>
                            <input type="text" id="pincode" name="pincode" required value="600025">
                        </div>
                    </div>
                </div>

                <!-- Mock Payment Section -->
                <div class="checkout-card">
                    <h3>2. Mock Payment Confirmation</h3>
                    <div class="payment-method-selector">
                        <label class="method-option">
                            <input type="radio" name="paymentMethod" value="card" checked onclick="togglePaymentFields('card')">
                            <div class="method-box">
                                <strong>💳 Mock Credit / Debit Card</strong>
                                <span>Instant mock authorization</span>
                            </div>
                        </label>
                        <label class="method-option">
                            <input type="radio" name="paymentMethod" value="upi" onclick="togglePaymentFields('upi')">
                            <div class="method-box">
                                <strong>📱 Mock UPI / VPA</strong>
                                <span>Simulated instant UPI transfer</span>
                            </div>
                        </label>
                    </div>

                    <div id="cardFields" class="payment-fields">
                        <div class="form-group">
                            <label for="accountIdentifier">Mock Card Number</label>
                            <input type="text" id="accountIdentifier" name="accountIdentifier" value="4111-2222-3333-4444" placeholder="4111-2222-3333-4444">
                        </div>
                        <div class="form-row">
                            <div class="form-group col-6">
                                <label>Expiry Date</label>
                                <input type="text" value="12/28" placeholder="MM/YY">
                            </div>
                            <div class="form-group col-6">
                                <label>Mock CVV</label>
                                <input type="text" value="789" placeholder="123">
                            </div>
                        </div>
                    </div>

                    <div id="upiFields" class="payment-fields" style="display:none;">
                        <div class="form-group">
                            <label for="upiId">Virtual Payment Address (VPA)</label>
                            <input type="text" id="upiId" placeholder="user@ybl" value="student@yusufmart">
                        </div>
                    </div>

                    <p class="mock-notice">ℹ️ Note: This is a safe simulated payment step conforming to project scope constraints. No real money will be charged.</p>
                </div>

                <button type="submit" class="btn btn-primary btn-block btn-lg">
                    Confirm Mock Payment & Place Order ($<fmt:formatNumber value="${cartTotal}" pattern="0.00"/>)
                </button>
            </form>
        </div>

        <!-- Order Review Sidebar -->
        <div class="checkout-summary-column">
            <div class="checkout-summary-card">
                <h3>Order Summary</h3>
                <div class="checkout-items-list">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="checkout-item-row">
                            <div class="ci-info">
                                <span class="ci-name"><c:out value="${item.product.name}" /></span>
                                <span class="ci-meta"><c:out value="${item.quantity}" /> x $<fmt:formatNumber value="${item.product.price}" pattern="0.00"/></span>
                            </div>
                            <span class="ci-subtotal">$<fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></span>
                        </div>
                    </c:forEach>
                </div>

                <div class="summary-divider"></div>

                <div class="summary-line">
                    <span>Subtotal</span>
                    <span>$<fmt:formatNumber value="${cartTotal}" pattern="0.00"/></span>
                </div>
                <div class="summary-line">
                    <span>Shipping</span>
                    <span class="free-tag">FREE</span>
                </div>

                <div class="summary-divider"></div>

                <div class="summary-line total-line">
                    <strong>Total to Pay</strong>
                    <strong class="total-amount">$<fmt:formatNumber value="${cartTotal}" pattern="0.00"/></strong>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function togglePaymentFields(method) {
    if (method === 'card') {
        document.getElementById('cardFields').style.display = 'block';
        document.getElementById('upiFields').style.display = 'none';
    } else {
        document.getElementById('cardFields').style.display = 'none';
        document.getElementById('upiFields').style.display = 'block';
    }
}
</script>

<jsp:include page="../common/footer.jsp" />
