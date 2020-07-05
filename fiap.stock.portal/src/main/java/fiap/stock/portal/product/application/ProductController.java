package fiap.stock.portal.product.application;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.product.domain.usecase.ProductUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("portal/users/{loginId}/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String insertNewProduct(@PathVariable("loginId") String loginId, @RequestBody ProductUseCase.ProductPayload productPayload) throws InvalidSuppliedDataException {
        return productUseCase.insertNewProduct(loginId, productPayload);
    }

}
