package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryAlreadyInactiveException;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.UpdateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.delivery.service.validator.DeliveryReceiverValidator;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateDeliveryServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final StatusRepository statusRepository;
    private final DeliveryReceiverValidator deliveryReceiverValidator;
    private final DeliveryMapper deliveryMapper;

    @Transactional
    public DeliveryResponse update(Long id, UpdateDeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(DeliveryNotFoundException::new);
        ensureActive(delivery);

        Status status = statusRepository.findById(request.statusId())
                .orElseThrow(StatusNotFoundException::new);
        List<User> receivers = deliveryReceiverValidator.validateAndFindReceivers(request.receiverIds());

        delivery.setStatus(status);
        delivery.setExpectedDeliveryAt(request.expectedDeliveryAt());
        delivery.setDeliveredAt(request.deliveredAt());
        delivery.setDeliveryLocation(request.deliveryLocation());
        delivery.setDescription(request.description());
        delivery.setProofUrl(request.proofUrl());
        replaceReceivers(delivery, receivers);

        return deliveryMapper.toDTO(deliveryRepository.save(delivery));
    }

    private void ensureActive(Delivery delivery) {
        if (!Boolean.TRUE.equals(delivery.getActive())) {
            throw new DeliveryAlreadyInactiveException();
        }
    }

    private void replaceReceivers(Delivery delivery, List<User> newReceivers) {
        Set<Long> newReceiverIds = newReceivers.stream()
                .map(User::getId)
                .collect(Collectors.toCollection(HashSet::new));

        delivery.getReceivers().removeIf(receiver -> !newReceiverIds.contains(receiver.getUser().getId()));

        Set<Long> existingReceiverIds = delivery.getReceivers()
                .stream()
                .map(receiver -> receiver.getUser().getId())
                .collect(Collectors.toSet());

        newReceivers.stream()
                .filter(user -> !existingReceiverIds.contains(user.getId()))
                .forEach(delivery::addReceiver);
    }
}
