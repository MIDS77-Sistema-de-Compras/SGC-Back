package net.centroweg.gerenciamentocompras.modules.delivery.service.validator;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.InvalidDeliveryReceiversException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeliveryReceiverValidator {

    private static final int REQUIRED_RECEIVERS = 2;

    private final UserPublicApi userPublicApi;

    public List<User> validateAndFindReceivers(List<Long> receiverIds) {
        if (receiverIds == null || receiverIds.size() != REQUIRED_RECEIVERS) {
            throw new InvalidDeliveryReceiversException("A entrega deve possuir exatamente dois recebedores.");
        }

        Set<Long> uniqueIds = new LinkedHashSet<>(receiverIds);
        if (uniqueIds.size() != REQUIRED_RECEIVERS) {
            throw new InvalidDeliveryReceiversException("Os recebedores da entrega devem ser diferentes.");
        }

        List<User> receivers = userPublicApi.findUsersByIds(List.copyOf(uniqueIds));
        if (receivers.size() != REQUIRED_RECEIVERS) {
            throw new UserNotFoundException();
        }

        boolean hasInactiveUser = receivers.stream()
                .anyMatch(user -> !Boolean.TRUE.equals(user.getActive()));
        if (hasInactiveUser) {
            throw new InvalidDeliveryReceiversException("Todos os recebedores devem estar ativos.");
        }

        return receivers;
    }
}
