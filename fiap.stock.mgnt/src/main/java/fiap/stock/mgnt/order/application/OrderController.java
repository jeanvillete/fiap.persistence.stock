package fiap.stock.mgnt.order.application;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.order.domain.usecase.OrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stock/users/{loginId}/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderUseCase.OrderPayload insertNewOrder(
            @PathVariable("loginId") String loginId,
            @RequestBody OrderUseCase.OrderPayload orderPayload)
            throws OrderConflictException, InvalidSuppliedDataException {

        orderPayload.setLoginId(loginId);

        return orderUseCase.insertNewOrder(orderPayload);
    }
}
