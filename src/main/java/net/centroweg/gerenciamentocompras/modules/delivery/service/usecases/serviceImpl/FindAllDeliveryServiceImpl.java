package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.mapper.DeliveryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FindAllDeliveryServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Transactional(readOnly = true)
    public List<DeliveryResponse> findAll(Boolean active, Long requestId, Long receiverId) {
        List<Delivery> deliveries;
        if (requestId != null) {
            deliveries = deliveryRepository.findByRequestId(requestId);
        } else if (receiverId != null) {
            deliveries = deliveryRepository.findByReceiverId(receiverId);
        } else if (Boolean.TRUE.equals(active)) {
            deliveries = deliveryRepository.findByActiveTrue();
        } else {
            deliveries = deliveryRepository.findAll();
        }

        if (active != null && !Boolean.TRUE.equals(active)) {
            deliveries = deliveries.stream()
                    .filter(delivery -> Objects.equals(delivery.getActive(), active))
                    .toList();
        }

        return deliveryMapper.toDTOList(deliveries);
    }
}
