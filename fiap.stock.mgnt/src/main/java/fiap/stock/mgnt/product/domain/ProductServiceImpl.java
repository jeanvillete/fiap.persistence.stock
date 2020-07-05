package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
class ProductServiceImpl implements ProductService {

    private static final String PREFIX_PRODUCT_CODE = "PRD-";

    private final ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void validLoginId(String loginId) throws InvalidSuppliedDataException {
        if (Objects.isNull(loginId)) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }

        loginId = loginId.trim();

        if (loginId.isEmpty()) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }
    }

    @Override
    public void validPrice(BigDecimal price) throws InvalidSuppliedDataException {
        if (Objects.isNull(price)) {
            throw new InvalidSuppliedDataException("Argument 'price' is mandatory.");
        }

        boolean greaterThanZero = price.compareTo(BigDecimal.ZERO) == 1;
        if (!greaterThanZero) {
            throw new InvalidSuppliedDataException("Argument 'price' must be greater than zero.");
        }
    }

    @Override
    public void validQuantity(Integer quantity) throws InvalidSuppliedDataException {
        if (Objects.isNull(quantity)) {
            throw new InvalidSuppliedDataException("Argument 'quantity' is mandatory.");
        }

        if (quantity <= 0) {
            throw new InvalidSuppliedDataException("Argument 'quantity' must be greater than zero.");
        }
    }

    @Override
    public String figureOutNewProductCode() {
        Long currentCountPlusOne = productRepository.count() + 1;
        return PREFIX_PRODUCT_CODE + String.format("%07d", currentCountPlusOne);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

}
