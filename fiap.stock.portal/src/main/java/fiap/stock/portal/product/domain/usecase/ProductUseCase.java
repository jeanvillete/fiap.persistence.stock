package fiap.stock.portal.product.domain.usecase;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.product.domain.Product;
import fiap.stock.portal.product.domain.ProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductUseCase {

    public static class ProductPayload {
        String loginId;
        String code;
        String description;
        BigDecimal price;
        Integer quantity;

        public ProductPayload() {
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    private final ProductService productService;

    public ProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public String insertNewProduct(ProductPayload productPayload) throws InvalidSuppliedDataException {
        productService.validLoginId(productPayload.loginId);

        productService.validCode(productPayload.code);
        productService.validDescription(productPayload.description);
        productService.validPrice(productPayload.price);
        productService.validQuantity(productPayload.quantity);

        Product product = productService.findByCode(productPayload.code);

        product.setLoginId(productPayload.loginId);
        product.setCode(productPayload.code);
        product.setDescription(productPayload.description);
        product.setPrice(productPayload.price);
        product.setQuantity(productPayload.quantity);

        productService.save(product);

        return product.getId();
    }

}
