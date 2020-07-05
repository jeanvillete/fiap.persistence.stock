package fiap.stock.portal.address.application;

import fiap.stock.portal.address.domain.usecase.AddressUseCase;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("portal/users/{loginId}/addresses")
public class AddressController {

    private final AddressUseCase addressUseCase;

    public AddressController(AddressUseCase addressUseCase) {
        this.addressUseCase = addressUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressUseCase.AddressPayload insertNewClientAddress(
            @PathVariable("loginId") String loginId,
            @RequestBody AddressUseCase.AddressPayload addressPayload)
            throws InvalidSuppliedDataException {

        return addressUseCase.insertNewClientAddress(loginId, addressPayload);
    }

}
