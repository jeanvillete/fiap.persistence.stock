package fiap.stock.portal.address.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
class AddressServiceImpl implements AddressService {

    private static int MIN_COMPLEMENT_LENGTH = 10;
    private static int MAX_COMPLEMENT_LENGTH = 50;

    @Override
    public void validZipCode(String zipCode) throws InvalidSuppliedDataException {
        if (Objects.isNull(zipCode)) {
            throw new InvalidSuppliedDataException("Nested argument 'zip_code' is mandatory for address.");
        }

        zipCode = zipCode.trim();

        if (!Pattern.compile("^\\d{5}-\\d{3}$").matcher(zipCode).matches()) {
            throw new InvalidSuppliedDataException("Nested argument 'zip_code' must follow the pattern; 00000-000");
        }
    }

    @Override
    public void validComplement(String complement) throws InvalidSuppliedDataException {
        if (Objects.isNull(complement)) {
            throw new InvalidSuppliedDataException("Nested argument 'complement' is mandatory for address.");
        }

        complement = complement.trim();

        if (complement.length() < MIN_COMPLEMENT_LENGTH || complement.length() > MAX_COMPLEMENT_LENGTH) {
            throw new InvalidSuppliedDataException(
                    "Nested argument 'complement' must be greater than [" + MIN_COMPLEMENT_LENGTH + "]" +
                            " and less than [" + MAX_COMPLEMENT_LENGTH + "]"
            );
        }
    }

    @Override
    public void validCity(String city) throws InvalidSuppliedDataException {
        if (Objects.isNull(city)) {
            throw new InvalidSuppliedDataException("Nested argument 'city' is mandatory for address.");
        }

        city = city.trim();

        if (city.isEmpty()) {
            throw new InvalidSuppliedDataException("Nested argument 'city' is mandatory for address.");
        }
    }

    @Override
    public void validState(String state) throws InvalidSuppliedDataException {
        if (Objects.isNull(state)) {
            throw new InvalidSuppliedDataException("Nested argument 'state' is mandatory for address.");
        }

        state = state.trim();

        if (state.isEmpty()) {
            throw new InvalidSuppliedDataException("Nested argument 'state' is mandatory for address.");
        }
    }

    @Override
    public void validCountry(String country) throws InvalidSuppliedDataException {
        if (Objects.isNull(country)) {
            throw new InvalidSuppliedDataException("Nested argument 'country' is mandatory for address.");
        }

        country = country.trim();

        if (country.isEmpty()) {
            throw new InvalidSuppliedDataException("Nested argument 'country' is mandatory for address.");
        }
    }

}
