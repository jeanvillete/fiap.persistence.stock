package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.product.domain.Product;
import org.springframework.stereotype.Service;

@Service
class OrderServiceImpl implements OrderService {

    private final OrderProductRepository orderProductRepository;

    OrderServiceImpl(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public Integer sumApprovedOrdersForSpecificProduct(Product product) {
        return orderProductRepository.sumApprovedOrdersForSpecificProduct(product, OrderStatus.APPROVED)
                .orElse(0);
    }

}
