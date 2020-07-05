package fiap.stock.portal.order.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OrderRepository extends MongoRepository<Order, String> {
}
