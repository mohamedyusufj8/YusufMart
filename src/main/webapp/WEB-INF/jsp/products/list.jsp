<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Marketplace Catalog — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="hero-section">
    <div class="hero-content">
        <h1>Discover Quality Products on YusufMart</h1>
        <p>Connecting verified sellers with buyers. Shop electronics, fashion, books, and home essentials.</p>
    </div>
</div>

<div class="catalog-container">
    <!-- Filters & Sort Bar -->
    <div class="filter-bar">
        <div class="category-chips">
            <a href="${pageContext.request.contextPath}/products"
               class="chip <c:if test='${empty selectedCategory}'>active</c:if>">All Categories</a>
            <c:forEach var="cat" items="${categories}">
                <a href="${pageContext.request.contextPath}/products?category=<c:out value='${cat}'/><c:if test='${not empty keyword}'>&q=<c:out value='${keyword}'/></c:if>"
                   class="chip <c:if test='${selectedCategory == cat}'>active</c:if>"><c:out value="${cat}" /></a>
            </c:forEach>
        </div>

        <form action="${pageContext.request.contextPath}/products" method="get" class="sort-form">
            <c:if test="${not empty selectedCategory}">
                <input type="hidden" name="category" value="<c:out value='${selectedCategory}'/>">
            </c:if>
            <c:if test="${not empty keyword}">
                <input type="hidden" name="q" value="<c:out value='${keyword}'/>">
            </c:if>
            <label for="sort" class="sort-label">Sort by:</label>
            <select name="sort" id="sort" onchange="this.form.submit()" class="sort-select">
                <option value="newest" <c:if test="${sortBy == 'newest'}">selected</c:if>>Newest Arrivals</option>
                <option value="price_asc" <c:if test="${sortBy == 'price_asc'}">selected</c:if>>Price: Low to High</option>
                <option value="price_desc" <c:if test="${sortBy == 'price_desc'}">selected</c:if>>Price: High to Low</option>
                <option value="rating" <c:if test="${sortBy == 'rating'}">selected</c:if>>Highest Customer Rating</option>
            </select>
        </form>
    </div>

    <!-- Active Search Notice -->
    <c:if test="${not empty keyword}">
        <div class="search-notice">
            Showing results for <strong>"<c:out value="${keyword}" />"</strong>
            <a href="${pageContext.request.contextPath}/products<c:if test='${not empty selectedCategory}'>?category=<c:out value='${selectedCategory}'/></c:if>" class="clear-search">&times; Clear</a>
        </div>
    </c:if>

    <!-- Product Grid -->
    <c:choose>
        <c:when test="${not empty products}">
            <div class="product-grid">
                <c:forEach var="p" items="${products}">
                    <div class="product-card">
                        <div class="product-image-box">
                            <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}">
                                <c:choose>
                                    <c:when test="${not empty p.imageUrl}">
                                        <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>" loading="lazy" class="product-img">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="placeholder-img"><span><c:out value="${p.category}"/></span></div>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <span class="category-tag"><c:out value="${p.category}" /></span>
                        </div>

                        <div class="product-info">
                            <div class="product-seller">Sold by <c:out value="${p.sellerName}" /></div>
                            <h3 class="product-title">
                                <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}">
                                    <c:out value="${p.name}" />
                                </a>
                            </h3>

                            <div class="product-rating">
                                <span class="stars">★</span>
                                <span class="rating-num"><fmt:formatNumber value="${p.averageRating}" pattern="0.0"/></span>
                                <span class="rating-count">(<c:out value="${p.reviewCount}" />)</span>
                            </div>

                            <div class="product-price-row">
                                <div class="price-box">
                                    <span class="currency">$</span>
                                    <span class="amount"><fmt:formatNumber value="${p.price}" pattern="0.00"/></span>
                                </div>

                                <c:choose>
                                    <c:when test="${p.inStock}">
                                        <span class="badge badge-stock in-stock"><c:out value="${p.stockQty}"/> in stock</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-stock out-of-stock">Out of stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="product-actions">
                                <c:choose>
                                    <c:when test="${p.inStock}">
                                        <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                            <input type="hidden" name="productId" value="${p.id}">
                                            <input type="hidden" name="quantity" value="1">
                                            <input type="hidden" name="redirect" value="/products">
                                            <button type="submit" class="btn btn-primary btn-block">Add to Cart</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-disabled btn-block" disabled>Sold Out</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-icon">🔍</div>
                <h3>No products found</h3>
                <p>Try clearing filters or searching for something else.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">View All Products</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../common/footer.jsp" />
