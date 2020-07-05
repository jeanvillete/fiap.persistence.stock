package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.product.domain.Product;

import javax.persistence.*;

@Entity
@Table(name = "stock_order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false, name = "stock_order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false, name = "stock_product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

}
