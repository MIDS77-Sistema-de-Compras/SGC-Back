package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.CreateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import net.centroweg.gerenciamentocompras.modules.delivery.service.validator.DeliveryReceiverValidator;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateDeliveryServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;
    private final DeliveryReceiverValidator deliveryReceiverValidator;
    private final DeliveryMapper deliveryMapper;

    @Transactional
    public DeliveryResponse create(CreateDeliveryRequest request) {
        Request requestEntity = requestRepository.findById(request.requestId())
                .orElseThrow(RequestNotFoundException::new);
        Status status = statusRepository.findById(request.statusId())
                .orElseThrow(StatusNotFoundException::new);
        List<User> receivers = deliveryReceiverValidator.validateAndFindReceivers(request.receiverIds());

        Delivery delivery = new Delivery();
        delivery.setRequest(requestEntity);
        delivery.setStatus(status);
        delivery.setExpectedDeliveryAt(request.expectedDeliveryAt());
        delivery.setDeliveryLocation(request.deliveryLocation());
        delivery.setDescription(request.description());
        delivery.setProofUrl(request.proofUrl());

        receivers.forEach(delivery::addReceiver);

        return deliveryMapper.toDTO(deliveryRepository.save(delivery));
    }
}
