package fiap.stock.mgnt.catalog.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.mgnt.catalog.domain.Catalog;
import fiap.stock.mgnt.catalog.domain.CatalogService;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Component;

@Component
public class CatalogUseCase {

    public static class CatalogPayload {

        @JsonProperty("login_id")
        String loginId;

        Integer id;
        String description;

        public CatalogPayload() {
        }

        public CatalogPayload(Integer id, String loginId, String description) {
            this.id = id;
            this.loginId = loginId;
            this.description = description;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private final CatalogService catalogService;

    public CatalogUseCase(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public CatalogPayload insertNewCatalogItem(CatalogPayload catalogPayload) throws InvalidSuppliedDataException {
        this.catalogService.validLoginId(catalogPayload.loginId);

        this.catalogService.validDescription(catalogPayload.description);

        Catalog catalog = new Catalog(
                catalogPayload.getLoginId(),
                catalogPayload.getDescription()
        );

        this.catalogService.insert(catalog);

        return new CatalogPayload(
                catalog.getId(),
                catalog.getLoginId(),
                catalog.getDescription()
        );
    }

}
