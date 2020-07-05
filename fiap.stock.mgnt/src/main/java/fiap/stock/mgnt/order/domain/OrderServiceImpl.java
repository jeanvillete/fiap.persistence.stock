package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.product.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
class OrderServiceImpl implements OrderService {

    private static final String ORD_PREFIX = "ORD-";

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    OrderServiceImpl(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public Integer sumApprovedOrdersForSpecificProduct(Product product) {
        return orderProductRepository.sumApprovedOrdersForSpecificProduct(product, OrderStatus.APPROVED)
                .orElse(0);
    }

    @Override
    public void validLoginId(String loginId) throws InvalidSuppliedDataException {
        if (Objects.isNull(loginId)) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }

        loginId = loginId.trim();

        if (loginId.isEmpty()) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }
    }

    @Override
    public void validCode(String code) throws InvalidSuppliedDataException {
        if (Objects.isNull(code)) {
            throw new InvalidSuppliedDataException("Argument 'code' is mandatory.");
        }

        code = code.trim();

        if (!Pattern.compile("^" + ORD_PREFIX + "[\\d]{7}$").matcher(code).matches()) {
            throw new InvalidSuppliedDataException("Argument 'code' must follow the pattern; " + ORD_PREFIX + "0000000");
        }
    }

    @Override
    public void checkForConflictOnInsert(String code) throws OrderConflictException {
        Integer currentCount = orderRepository.countByCode(code).orElse(0);

        if (currentCount > 0) {
            throw new OrderConflictException("There is already an order recorded for the code [" + code + "]");
        }
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

}
