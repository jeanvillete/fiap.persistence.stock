package fiap.stock.mgnt.order.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "stock_order")
public class Order {

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
