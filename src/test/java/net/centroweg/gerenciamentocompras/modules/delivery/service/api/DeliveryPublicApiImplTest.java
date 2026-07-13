package net.centroweg.gerenciamentocompras.modules.delivery.service.api;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryPublicApiImplTest {

    @Mock private DeliveryRepository deliveryRepository;

    @Test
    void shouldReturnImmutableNotificationDataForActiveDelivery() {
        Delivery delivery = new Delivery();
        delivery.setId(7L);
        delivery.setDeliveredAt(LocalDateTime.of(2026, 7, 13, 11, 30));
        delivery.setDeliveryLocation("SENAI");
        delivery.setDescription("Entrega parcial");
        when(deliveryRepository.findActiveByRequestIdAndProductItemId(10L, 99L))
                .thenReturn(List.of(delivery));
        when(deliveryRepository.findReceiverNotificationNames(7L)).thenReturn(List.of("Ana"));
        when(deliveryRepository.findProductNotificationData(7L))
                .thenReturn(List.of(new DeliveryProductNotificationData("Parafuso", "P-1", 2.0, "UN")));
        when(deliveryRepository.findProvisionNotificationNames(7L)).thenReturn(List.of("Instalacao"));

        var result = new DeliveryPublicApiImpl(deliveryRepository)
                .findActiveDeliveryByProductItem(10L, 99L);

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().receiverNames()).containsExactly("Ana");
        assertThat(result.orElseThrow().productItems()).hasSize(1);
        assertThat(result.orElseThrow().provisionItemNames()).containsExactly("Instalacao");
    }

    @Test
    void shouldIgnoreInactiveOrMissingDelivery() {
        when(deliveryRepository.findActiveByRequestIdAndProvisionItemId(10L, 99L)).thenReturn(List.of());

        var result = new DeliveryPublicApiImpl(deliveryRepository)
                .findActiveDeliveryByProvisionItem(10L, 99L);

        assertThat(result).isEmpty();
        verify(deliveryRepository, never()).findReceiverNotificationNames(org.mockito.ArgumentMatchers.any());
    }
}
