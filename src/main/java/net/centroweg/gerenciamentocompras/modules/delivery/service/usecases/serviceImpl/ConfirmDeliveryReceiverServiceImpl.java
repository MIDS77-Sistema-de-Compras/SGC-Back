package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryReceiverNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.strategy.DeliveredStatusImpl;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfirmDeliveryReceiverServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final StatusRepository statusRepository;
    private final CurrentUserService currentUserService;
    private final DeliveryMapper deliveryMapper;

    @Transactional
    public DeliveryResponse confirmReceiver(Long deliveryId, Long userId, ConfirmDeliveryReceiverRequest request) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);
        ensureActive(delivery);

        User currentUser = currentUserService.getCurrentUser();
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new AcessDeniedException();
        }

        DeliveryReceiver receiver = delivery.getReceivers()
                .stream()
                .filter(candidate -> Objects.equals(candidate.getUser().getId(), userId))
                .findFirst()
                .orElseThrow(DeliveryReceiverNotFoundException::new);

        if (!Boolean.TRUE.equals(receiver.getConfirmed())) {
            receiver.setConfirmed(true);
            receiver.setConfirmedAt(LocalDateTime.now());
            receiver.setObservation(request.observation());
        }

        updateStatusWhenAllReceiversConfirmed(delivery);

        return deliveryMapper.toDTO(deliveryRepository.save(delivery));
    }

    private void ensureActive(Delivery delivery) {
        if (!Boolean.TRUE.equals(delivery.getActive())) {
            throw new DeliveryAlreadyInactiveException();
        }
    }

    private void updateStatusWhenAllReceiversConfirmed(Delivery delivery) {
        boolean allConfirmed = delivery.getReceivers()
                .stream()
                .allMatch(receiver -> Boolean.TRUE.equals(receiver.getConfirmed()));

        if (!allConfirmed) {
            return;
        }

        String deliveredStatusName = new DeliveredStatusImpl().getName();
        statusRepository.findByNameIgnoreCase(deliveredStatusName)
                .ifPresent(delivery::setStatus);
    }
}
