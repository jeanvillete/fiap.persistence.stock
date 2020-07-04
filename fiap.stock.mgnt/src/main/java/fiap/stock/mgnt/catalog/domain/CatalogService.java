package fiap.stock.mgnt.catalog.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;

public interface CatalogService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validDescription(String description) throws InvalidSuppliedDataException;

    void insert(Catalog catalog);

}
