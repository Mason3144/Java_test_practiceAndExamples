package com.codestates.sliceTest.mock.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.member.entity.Member;
import com.codestates.order.entity.Order;
import com.codestates.order.entity.OrderCoffee;
import com.codestates.order.repository.OrderRepository;
import com.codestates.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceHomeworkTest {
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderService orderService;
    Long orderId = 1l;
    @Test
    @DisplayName("preProcessingOrder")
    public void cancelOrderTest() {
        Order order = new Order();
        order.setOrderId(orderId);

        given(orderRepository.findById(Mockito.anyLong())).willReturn(Optional.of(order));

        assertDoesNotThrow(() -> orderService.cancelOrder(orderId));

    }
    @Test
    @DisplayName("inProcessingOrder")
    public void cancelOrderTest2() {

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus(Order.OrderStatus.ORDER_CONFIRM);

        given(orderRepository.findById(Mockito.anyLong())).willReturn(Optional.of(order));

        assertThrows(BusinessLogicException.class, () -> orderService.cancelOrder(orderId));
    }
}
