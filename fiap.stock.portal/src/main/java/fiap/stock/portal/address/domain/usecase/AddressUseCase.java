package fiap.stock.portal.address.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.portal.address.domain.Address;
import fiap.stock.portal.address.domain.AddressService;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Component;

@Component
public class AddressUseCase {

    public static class AddressPayload {
        @JsonProperty
        String code;

        @JsonProperty("zip_code")
        String zipCode;

        @JsonProperty
        String complement;

        @JsonProperty
        String city;

        @JsonProperty
        String state;

        @JsonProperty
        String country;

        public AddressPayload(String code, String zipCode, String complement, String city, String state, String country) {
            this.code = code;
            this.zipCode = zipCode;
            this.complement = complement;
            this.city = city;
            this.state = state;
            this.country = country;
        }
    }

    private final AddressService addressService;

    public AddressUseCase(AddressService addressService) {
        this.addressService = addressService;
    }

    public AddressPayload insertNewClientAddress(String loginId, AddressPayload addressPayload) throws InvalidSuppliedDataException {
        addressService.validLoginId(loginId);

        addressService.validZipCode(addressPayload.zipCode);
        addressService.validComplement(addressPayload.complement);
        addressService.validCity(addressPayload.city);
        addressService.validState(addressPayload.state);
        addressService.validCountry(addressPayload.country);

        Address address = new Address(
                loginId,
                addressPayload.zipCode,
                addressPayload.complement,
                addressPayload.city,
                addressPayload.state,
                addressPayload.country
        );

        addressService.save(address);

        return new AddressPayload(
                address.getId(),
                address.getZipCode(),
                address.getComplement(),
                address.getCity(),
                address.getState(),
                address.getCountry()
        );
    }

}
