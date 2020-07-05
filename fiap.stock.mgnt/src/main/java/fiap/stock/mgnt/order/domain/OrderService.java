package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.product.domain.Product;

public interface OrderService {

    Integer sumApprovedOrdersForSpecificProduct(Product product);

}
