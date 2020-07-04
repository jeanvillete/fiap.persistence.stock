package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.product.domain.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "stock_order")
public class Order {

    @Entity
    @Table(name = "stock_order_product")
    public static class OrderProduct {

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, updatable = false, name = "stock_order_id")
        private Order order;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, updatable = false, name = "stock_product_id")
        private Product product;

        @Column(nullable = false)
        private Integer quantity;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25, name = "login_id")
    private String loginId;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderProduct> products;

    @Column(nullable = false, length = 11)
    private String code;

    @Column(nullable = false, length = 20, name = "order_status")
    private OrderStatus status;

}
