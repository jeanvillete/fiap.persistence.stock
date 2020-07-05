package fiap.stock.portal.address.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;

import java.util.List;

public interface AddressService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validZipCode(String zipCode) throws InvalidSuppliedDataException;

    void validComplement(String complement) throws InvalidSuppliedDataException;

    void validCity(String city) throws InvalidSuppliedDataException;

    void validState(String state) throws InvalidSuppliedDataException;

    void validCountry(String country) throws InvalidSuppliedDataException;

    void save(Address address);

    List<Address> findAllByLoginId(String loginId);

}
