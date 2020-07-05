package fiap.stock.portal.address.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AddressRepository extends MongoRepository<Address, String> {
}
