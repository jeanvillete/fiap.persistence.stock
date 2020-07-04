package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.catalog.domain.Catalog;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25, name = "login_id")
    private String loginId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "stock_catalog_id")
    private Catalog catalog;

    @Column(nullable = false, length = 11)
    private String code;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "entry_time")
    private LocalDateTime entryTime;

}
