<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Seller Dashboard — YusufMart" />
<jsp:include page="../common/header.jsp" />

<div class="seller-container">
    <div class="dashboard-header-row">
        <div>
            <h1>Seller Management Portal</h1>
            <p class="subtitle">Manage product listings, inventory levels, and pricing (Section 1 F2)</p>
        </div>
        <button type="button" class="btn btn-primary" onclick="openProductModal()">+ Add New Product Listing</button>
    </div>

    <c:if test="${param.saved == 'true'}">
        <div class="alert alert-success">Product listing saved successfully!</div>
    </c:if>
    <c:if test="${param.deleted == 'true'}">
        <div class="alert alert-success">Product listing was removed.</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
    </c:if>

    <!-- Listings Table -->
    <div class="card table-card">
        <h3>My Product Catalog</h3>
        <c:choose>
            <c:when test="${not empty products}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Rating</th>
                            <th>Listed Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td class="table-product-cell">
                                    <c:if test="${not empty p.imageUrl}">
                                        <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>" class="table-thumb">
                                    </c:if>
                                    <div>
                                        <strong><a href="${pageContext.request.contextPath}/product-detail?id=${p.id}"><c:out value="${p.name}"/></a></strong>
                                    </div>
                                </td>
                                <td><span class="category-pill"><c:out value="${p.category}"/></span></td>
                                <td><strong>$<fmt:formatNumber value="${p.price}" pattern="0.00"/></strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.stockQty > 5}">
                                            <span class="badge badge-stock in-stock"><c:out value="${p.stockQty}"/> units</span>
                                        </c:when>
                                        <c:when test="${p.stockQty > 0}">
                                            <span class="badge badge-stock low-stock"><c:out value="${p.stockQty}"/> low</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-stock out-of-stock">0 Out</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>★ <fmt:formatNumber value="${p.averageRating}" pattern="0.0"/> (<c:out value="${p.reviewCount}"/>)</td>
                                <td><fmt:formatDate value="${p.createdAt}" pattern="MMM dd, yyyy"/></td>
                                <td class="actions-cell">
                                    <button type="button" class="btn btn-outline-sm"
                                            onclick="editProduct(${p.id}, '<c:out value="${p.name}" escapeXml="true"/>', '<c:out value="${p.category}" escapeXml="true"/>', ${p.price}, ${p.stockQty}, '<c:out value="${p.imageUrl}" escapeXml="true"/>', '<c:out value="${p.description}" escapeXml="true"/>')">
                                        Edit
                                    </button>

                                    <form action="${pageContext.request.contextPath}/seller/products/delete" method="post" style="display:inline;" onsubmit="return confirm('Delete this listing?');">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button type="submit" class="btn btn-danger-sm">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>You have not listed any products yet. Click the button above to add your first product listing!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Modal Dialog for Add / Edit Product -->
<div id="productModal" class="modal-overlay" style="display:none;">
    <div class="modal-card">
        <div class="modal-header">
            <h3 id="modalTitle">Add Product Listing</h3>
            <button type="button" class="modal-close" onclick="closeProductModal()">&times;</button>
        </div>

        <form action="${pageContext.request.contextPath}/seller/products/save" method="post" class="modal-form">
            <input type="hidden" id="prodId" name="id" value="">

            <div class="form-group">
                <label for="prodName">Product Title *</label>
                <input type="text" id="prodName" name="name" required placeholder="e.g. Wireless Bluetooth Headphones">
            </div>

            <div class="form-row">
                <div class="form-group col-6">
                    <label for="prodCategory">Category *</label>
                    <select id="prodCategory" name="category" required class="form-control">
                        <option value="Electronics">Electronics</option>
                        <option value="Fashion">Fashion</option>
                        <option value="Books">Books</option>
                        <option value="Home & Kitchen">Home & Kitchen</option>
                        <option value="Sports">Sports</option>
                    </select>
                </div>
                <div class="form-group col-6">
                    <label for="prodPrice">Price ($ USD) *</label>
                    <input type="number" id="prodPrice" name="price" step="0.01" min="0.01" required placeholder="29.99">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group col-6">
                    <label for="prodStock">Stock Quantity *</label>
                    <input type="number" id="prodStock" name="stockQty" min="0" required placeholder="50">
                </div>
                <div class="form-group col-6">
                    <label for="prodImage">Image URL</label>
                    <input type="url" id="prodImage" name="imageUrl" placeholder="https://images.unsplash.com/...">
                </div>
            </div>

            <div class="form-group">
                <label for="prodDesc">Description</label>
                <textarea id="prodDesc" name="description" rows="3" placeholder="Provide product features, dimensions, specifications..."></textarea>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeProductModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Listing</button>
            </div>
        </form>
    </div>
</div>

<script>
function openProductModal() {
    document.getElementById('modalTitle').innerText = 'Add New Product Listing';
    document.getElementById('prodId').value = '';
    document.getElementById('prodName').value = '';
    document.getElementById('prodPrice').value = '';
    document.getElementById('prodStock').value = '';
    document.getElementById('prodImage').value = '';
    document.getElementById('prodDesc').value = '';
    document.getElementById('productModal').style.display = 'flex';
}

function editProduct(id, name, category, price, stock, image, desc) {
    document.getElementById('modalTitle').innerText = 'Edit Product Listing';
    document.getElementById('prodId').value = id;
    document.getElementById('prodName').value = name;
    document.getElementById('prodCategory').value = category;
    document.getElementById('prodPrice').value = price;
    document.getElementById('prodStock').value = stock;
    document.getElementById('prodImage').value = image;
    document.getElementById('prodDesc').value = desc;
    document.getElementById('productModal').style.display = 'flex';
}

function closeProductModal() {
    document.getElementById('productModal').style.display = 'none';
}
</script>

<jsp:include page="../common/footer.jsp" />
