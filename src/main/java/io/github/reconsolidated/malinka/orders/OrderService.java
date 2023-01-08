package io.github.reconsolidated.malinka.orders;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Getter
    private Order order = new Order();

}
