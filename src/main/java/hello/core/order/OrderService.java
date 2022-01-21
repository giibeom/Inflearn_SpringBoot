package hello.core.order;

/**
 * @author Created by 명기범 on 2021-12-01
 */
public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
