package fiap.stock.portal.address.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;

public interface AddressService {

    void validZipCode(String zipCode) throws InvalidSuppliedDataException;

    void validComplement(String complement) throws InvalidSuppliedDataException;

    void validCity(String city) throws InvalidSuppliedDataException;

    void validState(String state) throws InvalidSuppliedDataException;

    void validCountry(String country) throws InvalidSuppliedDataException;

}
