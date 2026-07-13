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
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.strategy.DeliveredStatusImpl;
import net.centroweg.gerenciamentocompras.modules.request.domain.strategy.PartiallyFulfilledStatusImpl;
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

        String finalStatusName = hasRefusedItems(delivery.getRequest())
                ? new PartiallyFulfilledStatusImpl().getName()
                : new DeliveredStatusImpl().getName();

        statusRepository.findByNameIgnoreCase(finalStatusName)
                .ifPresent(delivery::setStatus);
    }

    /**
     * Verifica se algum item (produto ou serviço) da solicitação foi recusado.
     */
    private boolean hasRefusedItems(Request request) {
        boolean refusedProduct = request.getItemRequestProducts()
                .stream()
                .anyMatch(item -> isRefused(item.getStatus_id()));

        boolean refusedProvision = request.getItemRequestProvisions()
                .stream()
                .anyMatch(item -> isRefused(item.getStatus()));

        return refusedProduct || refusedProvision;
    }

    private boolean isRefused(Status status) {
        return status != null
                && status.getName() != null
                && status.getName().trim().equalsIgnoreCase("Recusado");
    }
}
