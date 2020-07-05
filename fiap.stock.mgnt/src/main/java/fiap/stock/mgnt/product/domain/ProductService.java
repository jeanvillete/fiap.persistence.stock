package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;

import java.math.BigDecimal;

public interface ProductService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validPrice(BigDecimal price) throws InvalidSuppliedDataException;

    void validQuantity(Integer quantity) throws InvalidSuppliedDataException;

    String figureOutNewProductCode();

    void save(Product product);

}
