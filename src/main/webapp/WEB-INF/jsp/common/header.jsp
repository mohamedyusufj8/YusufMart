<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'YusufMart — Marketplace'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>

<header class="navbar-wrapper">
    <div class="navbar-container">
        <!-- Brand Logo -->
        <a href="${pageContext.request.contextPath}/products" class="brand-logo">
            <span class="brand-badge">YM</span>
            <span class="brand-name">Yusuf<span>Mart</span></span>
        </a>

        <!-- Search Bar -->
        <form action="${pageContext.request.contextPath}/products" method="get" class="search-form">
            <input type="text" name="q" placeholder="Search products, brands, categories..." value="<c:out value='${param.q}'/>">
            <c:if test="${not empty param.category}">
                <input type="hidden" name="category" value="<c:out value='${param.category}'/>">
            </c:if>
            <button type="submit" class="btn-search" title="Search">
                <svg width="18" height="18" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            </button>
        </form>

        <!-- Navigation Links & User Menu -->
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/products" class="nav-link">Shop Catalog</a>

            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <c:if test="${sessionScope.currentUser.role == 'BUYER' || sessionScope.currentUser.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/cart" class="nav-link cart-link" title="Shopping Cart">
                            <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"/></svg>
                            <span>Cart</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/orders" class="nav-link">My Orders</a>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'SELLER' || sessionScope.currentUser.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/seller/products" class="nav-link badge-link">Seller Dashboard</a>
                        <a href="${pageContext.request.contextPath}/seller/orders" class="nav-link badge-link">Incoming Orders</a>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link admin-tag">Admin Panel</a>
                    </c:if>

                    <div class="user-menu">
                        <span class="user-greeting">Hi, <strong><c:out value="${sessionScope.currentUser.name}" /></strong></span>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-sm">Logout</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Sign In</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Register</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>

<main class="main-content">
