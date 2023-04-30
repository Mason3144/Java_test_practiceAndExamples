package com.codestates.unitTest;

import com.codestates.helper.StampCalculator;
import com.codestates.order.entity.Order;
import com.codestates.order.entity.OrderCoffee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StampCalculatorTest {
    static int oldCount;
    static int newCount;
    @BeforeAll
    public static void init(){
        oldCount = (int) Math.floor(Math.random()*10);
        newCount = (int) Math.floor(Math.random()*10);
    }
    @Test
    @DisplayName("실습1: 스탬프 카운트 계산 단위 테스트")
    public void calculateStampCountTest() {
        int stamp = StampCalculator.calculateStampCount(oldCount, newCount);
        assertEquals(oldCount+newCount, stamp);
    }

    @Test
    @DisplayName("실습1: 주문 후 누적 스탬프 카운트 계산 단위 테스트")
    public void calculateEarnedStampCountTest(){
        Order order = new Order();
        OrderCoffee orderCoffee1 = new OrderCoffee();
        OrderCoffee orderCoffee2 = new OrderCoffee();

        orderCoffee1.setQuantity(oldCount);
        orderCoffee2.setQuantity(newCount);

        order.setOrderCoffees(List.of(orderCoffee1,orderCoffee2));

        int expect = orderCoffee1.getQuantity() + orderCoffee2.getQuantity();

        int actual = StampCalculator.calculateEarnedStampCount(order);

        assertEquals(expect, actual);
    }
}
