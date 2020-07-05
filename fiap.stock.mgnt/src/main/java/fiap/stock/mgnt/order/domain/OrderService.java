package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.product.domain.Product;

public interface OrderService {

    Integer sumApprovedOrdersForSpecificProduct(Product product);

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validCode(String code) throws InvalidSuppliedDataException;

    void checkForConflictOnInsert(String code) throws OrderConflictException;

    void save(Order order);

}
