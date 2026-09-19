package com.yusuf.yusufmart.service;

import com.yusuf.yusufmart.dao.CartDAO;
import com.yusuf.yusufmart.dao.OrderDAO;
import com.yusuf.yusufmart.dao.ProductDAO;
import com.yusuf.yusufmart.exception.ValidationException;
import com.yusuf.yusufmart.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl(orderDAO, cartDAO, productDAO);
    }

    @Test
    public void testCheckoutThrowsExceptionWhenCartIsEmpty() throws Exception {
        when(cartDAO.findByUserId(1)).thenReturn(Collections.emptyList());

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            orderService.checkout(1);
        });

        assertTrue(ex.getMessage().contains("empty"));
        verify(orderDAO, never()).createOrder(any(), any());
    }

    @Test
    public void testGetOrderCountAndRevenue() throws Exception {
        when(orderDAO.count()).thenReturn(15L);
        when(orderDAO.calculateTotalRevenue()).thenReturn(new BigDecimal("1245.50"));

        assertEquals(15L, orderService.getOrderCount());
        assertEquals(new BigDecimal("1245.50"), orderService.getTotalRevenue());
    }
}
