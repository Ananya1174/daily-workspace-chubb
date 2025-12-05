package com.chubb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chubb.repository.OrderRepository;
import com.chubb.request.Item;
import com.chubb.request.Order1;
import lombok.extern.slf4j.Slf4j;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public Order1 insertOrder(Order1 order) {
        if (order.getItems() != null) {
            Set<Item> toAttach = new HashSet<>();
            for (Item it : order.getItems()) {
                it.getOrders().add(order);
                toAttach.add(it);
            }
            order.setItems(toAttach);
        }
        Order1 saved = orderRepository.save(order);
        log.debug("Saved order: {}", saved.getId());
        return saved;
    }
}