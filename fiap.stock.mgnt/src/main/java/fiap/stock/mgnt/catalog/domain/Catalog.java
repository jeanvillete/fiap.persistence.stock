package fiap.stock.mgnt.catalog.domain;

import javax.persistence.*;

@Entity
@Table(name = "stock_catalog")
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 25, name = "login_id")
    private String loginId;

    @Column(nullable = false, length = 50)
    private String description;

}
