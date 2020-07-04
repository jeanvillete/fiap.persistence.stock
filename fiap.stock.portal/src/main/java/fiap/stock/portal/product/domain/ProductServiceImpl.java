package fiap.stock.portal.product.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
class ProductServiceImpl implements ProductService {

    private static final String PROD_PREFIX = "PRD-";

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
    public void validCode(String code) throws InvalidSuppliedDataException {
        if (Objects.isNull(code)) {
            throw new InvalidSuppliedDataException("Argument 'code' is mandatory.");
        }

        code = code.trim();

        if (!Pattern.compile("^" + PROD_PREFIX + "[\\d]{7}$").matcher(code).matches()) {
            throw new InvalidSuppliedDataException("Argument 'code' must follow the pattern; " + PROD_PREFIX + "0000000");
        }
    }

    @Override
    public void validDescription(String description) throws InvalidSuppliedDataException {
        if (Objects.isNull(description)) {
            throw new InvalidSuppliedDataException("Argument 'description' is mandatory.");
        }
    }

    @Override
    public void validPrice(BigDecimal price) throws InvalidSuppliedDataException {
        if (Objects.isNull(price)) {
            throw new InvalidSuppliedDataException("Argument 'price' is mandatory.");
        }
    }

    @Override
    public void validQuantity(Integer quantity) throws InvalidSuppliedDataException {
        if (Objects.isNull(quantity)) {
            throw new InvalidSuppliedDataException("Argument 'quantity' is mandatory.");
        }
    }

    @Override
    public Product findByCode(String code) {
        return productRepository.findByCode(code)
            .orElse(new Product());
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

}