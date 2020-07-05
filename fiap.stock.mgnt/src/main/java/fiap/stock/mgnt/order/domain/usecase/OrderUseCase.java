package fiap.stock.mgnt.order.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.Order;
import fiap.stock.mgnt.order.domain.OrderProduct;
import fiap.stock.mgnt.order.domain.OrderService;
import fiap.stock.mgnt.order.domain.OrderStatus;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.product.domain.Product;
import fiap.stock.mgnt.product.domain.ProductService;
import fiap.stock.mgnt.product.domain.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OrderUseCase {

    public static class ProductPayload {
        String code;
        Integer quantity;

        public ProductPayload() {
        }

        public ProductPayload(String code, Integer quantity) {
            this.code = code;
            this.quantity = quantity;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class OrderPayload {
        @JsonProperty("login_id")
        String loginId;

        String code;
        List<ProductPayload> products;
        OrderStatus orderStatus;

        public OrderPayload() {
        }

        public OrderPayload(String loginId, String code, List<ProductPayload> products, OrderStatus orderStatus) {
            this.loginId = loginId;
            this.code = code;
            this.products = products;
            this.orderStatus = orderStatus;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<ProductPayload> getProducts() {
            return products;
        }

        public void setProducts(List<ProductPayload> products) {
            this.products = products;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }
    }

    private final OrderService orderService;
    private final ProductService productService;

    public OrderUseCase(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    public OrderPayload insertNewOrder(OrderPayload orderPayload) throws InvalidSuppliedDataException, OrderConflictException {
        orderService.validLoginId(orderPayload.loginId);

        orderService.validCode(orderPayload.code);
        orderService.checkForConflictOnInsert(orderPayload.code);

        List<Product> detachedProductList = Optional.ofNullable(orderPayload.products)
                .map(List::stream)
                .orElse(Stream.empty())
                .map(productPayload -> new Product(productPayload.code, productPayload.quantity))
                .collect(Collectors.toList());
        productService.validProductList(detachedProductList);

        Order order = new Order(
                orderPayload.loginId,
                orderPayload.code,
                OrderStatus.WAITING_FOR_ANSWER
        );
        Set<OrderProduct> mappedOrdersAndProducts = detachedProductList.stream()
                .map(detachedProduct -> {
                    try {
                        Product attachedProduct = productService.findByCode(detachedProduct.getCode());
                        return new OrderProduct(order, attachedProduct, detachedProduct.getQuantity());
                    } catch (ProductNotFoundException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .collect(Collectors.toSet());
        order.setProducts(mappedOrdersAndProducts);
        orderService.save(order);

        List<ProductPayload> productsPayload = order.getProducts()
                .stream()
                .map(orderProduct -> new ProductPayload(
                        orderProduct.getProduct().getCode(),
                        orderProduct.getQuantity()
                ))
                .collect(Collectors.toList());
        return new OrderPayload(
                order.getLoginId(),
                order.getCode(),
                productsPayload,
                order.getStatus()
        );
    }

}
