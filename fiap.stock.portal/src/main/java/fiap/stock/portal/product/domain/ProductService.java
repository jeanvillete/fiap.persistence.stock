package fiap.stock.portal.product.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;

import java.math.BigDecimal;

public interface ProductService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validCode(String code) throws InvalidSuppliedDataException;

    void validDescription(String description) throws InvalidSuppliedDataException;

    void validPrice(BigDecimal price) throws InvalidSuppliedDataException;

    void validQuantity(Integer quantity) throws InvalidSuppliedDataException;

    Product findByCode(String code);

    void save(Product product);

}
