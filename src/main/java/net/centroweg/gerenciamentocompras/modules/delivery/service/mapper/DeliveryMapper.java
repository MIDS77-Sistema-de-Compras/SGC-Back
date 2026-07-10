package net.centroweg.gerenciamentocompras.modules.delivery.service.mapper;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryReceiverResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class DeliveryMapper {

    public DeliveryResponse toDTO(Delivery delivery) {
        List<DeliveryReceiverResponse> receivers = delivery.getReceivers()
                .stream()
                .sorted(Comparator.comparing(receiver -> receiver.getUser().getId()))
                .map(this::toReceiverDTO)
                .toList();

        return new DeliveryResponse(
                delivery.getId(),
                delivery.getRequest().getId(),
                delivery.getStatus().getId(),
                delivery.getStatus().getName(),
                delivery.getExpectedDeliveryAt(),
                delivery.getDeliveredAt(),
                delivery.getDeliveryLocation(),
                delivery.getDescription(),
                delivery.getProofUrl(),
                delivery.getActive(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                receivers
        );
    }

    public DeliveryReceiverResponse toReceiverDTO(DeliveryReceiver receiver) {
        return new DeliveryReceiverResponse(
                receiver.getUser().getId(),
                receiver.getUser().getName(),
                receiver.getUser().getEmail(),
                receiver.getUser().getExtensionNumber(),
                receiver.getConfirmed(),
                receiver.getConfirmedAt(),
                receiver.getObservation()
        );
    }

    public List<DeliveryResponse> toDTOList(List<Delivery> deliveries) {
        return deliveries
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
