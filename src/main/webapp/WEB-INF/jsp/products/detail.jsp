<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="${product.name} — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="product-detail-wrapper">
    <div class="breadcrumbs">
        <a href="${pageContext.request.contextPath}/products">Catalog</a> &gt;
        <a href="${pageContext.request.contextPath}/products?category=<c:out value='${product.category}'/>"><c:out value="${product.category}" /></a> &gt;
        <span><c:out value="${product.name}" /></span>
    </div>

    <c:if test="${param.reviewed == 'true'}">
        <div class="alert alert-success">Thank you! Your product review and rating have been posted.</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger"><c:out value="${param.error}" /></div>
    </c:if>

    <div class="detail-grid">
        <!-- Product Image -->
        <div class="detail-image-box">
            <c:choose>
                <c:when test="${not empty product.imageUrl}">
                    <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>" class="detail-img">
                </c:when>
                <c:otherwise>
                    <div class="detail-placeholder"><span><c:out value="${product.category}"/></span></div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Product Specs & Purchasing -->
        <div class="detail-info-box">
            <span class="category-tag"><c:out value="${product.category}" /></span>
            <h1 class="detail-title"><c:out value="${product.name}" /></h1>

            <div class="detail-meta">
                <div class="rating-stars">
                    <span class="stars">★</span>
                    <strong><fmt:formatNumber value="${product.averageRating}" pattern="0.0"/></strong> / 5.0
                    <span class="muted">(<c:out value="${product.reviewCount}" /> customer reviews)</span>
                </div>
                <div class="seller-meta">Listed by <strong><c:out value="${product.sellerName}" /></strong></div>
            </div>

            <div class="detail-price-box">
                <span class="detail-price">$<fmt:formatNumber value="${product.price}" pattern="0.00"/></span>
                <c:choose>
                    <c:when test="${product.inStock}">
                        <span class="badge badge-stock in-stock"><c:out value="${product.stockQty}"/> In Stock</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-stock out-of-stock">Out of Stock</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="detail-description">
                <h3>About this item</h3>
                <p><c:out value="${product.description}" /></p>
            </div>

            <div class="detail-action-card">
                <c:choose>
                    <c:when test="${product.inStock}">
                        <form action="${pageContext.request.contextPath}/cart/add" method="post" class="purchase-form">
                            <input type="hidden" name="productId" value="${product.id}">
                            <input type="hidden" name="redirect" value="/product-detail?id=${product.id}">
                            
                            <div class="qty-group">
                                <label for="quantity">Quantity:</label>
                                <input type="number" id="quantity" name="quantity" value="1" min="1" max="${product.stockQty}" class="qty-input">
                            </div>

                            <button type="submit" class="btn btn-primary btn-lg">Add to Cart</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-disabled btn-lg" disabled>Currently Unavailable</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Customer Reviews Section (Section 1 F8) -->
    <div class="reviews-section">
        <div class="reviews-header">
            <h2>Customer Reviews & Ratings</h2>
            <div class="rating-summary">
                <span class="big-rating"><fmt:formatNumber value="${product.averageRating}" pattern="0.0"/></span>
                <div class="rating-sub">
                    <span class="stars">★★★★★</span>
                    <span>Based on <c:out value="${product.reviewCount}" /> verified reviews</span>
                </div>
            </div>
        </div>

        <!-- Write Review Form for eligible verified buyers -->
        <c:if test="${canReview}">
            <div class="review-form-card">
                <h3>Write a Verified Customer Review</h3>
                <p>Share your honest experience with this item:</p>
                <form action="${pageContext.request.contextPath}/reviews/add" method="post" class="review-form">
                    <input type="hidden" name="productId" value="${product.id}">
                    
                    <div class="form-group">
                        <label for="rating">Rating (1 to 5 Stars):</label>
                        <select name="rating" id="rating" class="form-control" required>
                            <option value="5">★★★★★ — 5 Stars (Excellent)</option>
                            <option value="4">★★★★☆ — 4 Stars (Good)</option>
                            <option value="3">★★★☆☆ — 3 Stars (Average)</option>
                            <option value="2">★★☆☆☆ — 2 Stars (Poor)</option>
                            <option value="1">★☆☆☆☆ — 1 Star (Terrible)</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="comment">Your Feedback & Experience:</label>
                        <textarea name="comment" id="comment" rows="3" class="form-control" required placeholder="What did you like or dislike about this product?"></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary">Submit Verified Review</button>
                </form>
            </div>
        </c:if>

        <!-- Reviews List -->
        <div class="reviews-list">
            <c:choose>
                <c:when test="${not empty reviews}">
                    <c:forEach var="rev" items="${reviews}">
                        <div class="review-card">
                            <div class="review-top">
                                <span class="reviewer-name"><c:out value="${rev.userName}" /></span>
                                <span class="badge badge-verified">Verified Purchase</span>
                                <span class="review-date"><fmt:formatDate value="${rev.createdAt}" pattern="MMM dd, yyyy"/></span>
                            </div>
                            <div class="review-stars">
                                <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                                <c:forEach begin="${rev.rating + 1}" end="5">☆</c:forEach>
                            </div>
                            <p class="review-body"><c:out value="${rev.comment}" /></p>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-reviews">
                        <p>No customer reviews yet for this product. Be the first buyer to review it once delivered!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
