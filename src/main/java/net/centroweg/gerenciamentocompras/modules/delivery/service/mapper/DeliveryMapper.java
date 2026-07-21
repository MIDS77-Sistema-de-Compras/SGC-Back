package net.centroweg.gerenciamentocompras.modules.delivery.service.mapper;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryReceiverResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryMapper {

    public DeliveryResponse toDTO(Delivery delivery) {
        List<DeliveryReceiverResponse> receivers = delivery.getReceivers()
                .stream()
                .sorted(Comparator.comparing(receiver -> receiver.getUser().getId()))
                .map(this::toReceiverDTO)
                .toList();

        Status status = delivery.getStatus();

        return new DeliveryResponse(
                delivery.getId(),
                delivery.getRequest().getId(),
                status.getId(),
                status.getName(),
                delivery.getExpectedDeliveryAt(),
                delivery.getDeliveredAt(),
                delivery.getDeliveryLocation(),
                delivery.getDescription(),
                delivery.getProofUrl(),
                delivery.getActive(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                receivers,
                delivery.getProductItems()
                        .stream()
                        .map(item -> item.getId())
                        .toList(),
                delivery.getProvisionItems()
                        .stream()
                        .map(item -> item.getId())
                        .toList()
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
