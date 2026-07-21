package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAccessDeniedException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryReceiverNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryStatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.api.StatusPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ConfirmDeliveryReceiverServiceImpl {

    private static final String DELIVERED_STATUS = "Entregue";

    private final DeliveryRepository deliveryRepository;
    private final StatusPublicApi statusPublicApi;
    private final CurrentUserService currentUserService;
    private final DeliveryMapper deliveryMapper;
    private final CompleteRequestOnDeliveryStatusServiceImpl completeRequestOnDeliveryStatusService;

    @Transactional
    public DeliveryResponse confirmReceiver(Long deliveryId, Long userId, ConfirmDeliveryReceiverRequest request) {
        Delivery delivery = deliveryRepository.findByIdForUpdate(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);
        ensureActive(delivery);

        User currentUser = currentUserService.getCurrentUser();
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new DeliveryAccessDeniedException();
        }

        DeliveryReceiver receiver = delivery.getReceivers()
                .stream()
                .filter(candidate -> Objects.equals(candidate.getUser().getId(), userId))
                .findFirst()
                .orElseThrow(DeliveryReceiverNotFoundException::new);

        if (Boolean.TRUE.equals(receiver.getConfirmed())) {
            return deliveryMapper.toDTO(delivery);
        }

        boolean completesDelivery = delivery.getReceivers().stream()
                .filter(candidate -> candidate != receiver)
                .allMatch(candidate -> Boolean.TRUE.equals(candidate.getConfirmed()));
        Status deliveredStatus = completesDelivery
                ? statusPublicApi.findByName(DELIVERED_STATUS)
                        .orElseThrow(DeliveryStatusNotFoundException::new)
                : null;
        LocalDateTime confirmationTime = LocalDateTime.now();

        receiver.setConfirmed(true);
        receiver.setConfirmedAt(confirmationTime);
        receiver.setObservation(normalizeObservation(request.observation()));

        if (completesDelivery) {
            delivery.setStatus(deliveredStatus);
            if (delivery.getDeliveredAt() == null) {
                delivery.setDeliveredAt(confirmationTime);
            }
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);
        completeRequestOnDeliveryStatusService.apply(savedDelivery);
        return deliveryMapper.toDTO(savedDelivery);
    }

    private void ensureActive(Delivery delivery) {
        if (!Boolean.TRUE.equals(delivery.getActive())) {
            throw new DeliveryAlreadyInactiveException();
        }
    }

    private String normalizeObservation(String observation) {
        return StringUtils.hasText(observation) ? observation.trim() : null;
    }
}
