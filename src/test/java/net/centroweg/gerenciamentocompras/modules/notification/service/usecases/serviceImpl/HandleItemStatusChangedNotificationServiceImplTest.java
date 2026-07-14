package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.DeliveryPublicApi;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusContentFormatter;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.ItemStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandleItemStatusChangedNotificationServiceImplTest {

    @Mock private RequestPublicApi requestPublicApi;
    @Mock private DeliveryPublicApi deliveryPublicApi;
    @Mock private CreateInternalNotificationUseCase createInternalNotificationUseCase;
    @Mock private ItemStatusChangedEmailSender emailSender;

    private HandleItemStatusChangedNotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        ItemStatusContentFormatter formatter = new ItemStatusContentFormatter();
        service = new HandleItemStatusChangedNotificationServiceImpl(
                requestPublicApi,
                deliveryPublicApi,
                createInternalNotificationUseCase,
                new ItemStatusInternalNotificationFactory(formatter),
                new ItemStatusEmailContentFactory(formatter),
                new RequestNotificationRecipientDeduplicator(),
                emailSender
        );
    }

    @Test
    void shouldCreateOneInternalNotificationPerUserAndDispatchEmailForProduct() {
        ItemStatusChangedEvent event = event(RequestItemType.PRODUCT, "Entregue");
        List<RequestNotificationRecipient> recipients = List.of(
                new RequestNotificationRecipient(1L, "Ana", "ana@teste.com"),
                new RequestNotificationRecipient(1L, "Ana repetida", "ana@teste.com"),
                new RequestNotificationRecipient(2L, "Bia", "ANA@TESTE.COM"),
                new RequestNotificationRecipient(3L, "Sem e-mail", null)
        );
        when(requestPublicApi.findNotificationDataById(10L))
                .thenReturn(new RequestNotificationData(10L, recipients));
        when(deliveryPublicApi.findActiveDeliveryByProductItem(10L, 99L))
                .thenReturn(Optional.of(delivery()));

        service.handle(event);

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(createInternalNotificationUseCase).createNotifications(
                eq("Item disponivel para retirada - Solicitacao #10"),
                message.capture(),
                eq(10L),
                eq(List.of(1L, 2L, 3L))
        );
        assertThat(message.getValue()).doesNotContain("<b>", "<br>", "<div>", "<table>");
        verify(emailSender).sendEmails(eq(event), eq(recipients), any());
    }

    @Test
    void shouldUseProvisionDeliveryApiForDeliveredProvision() {
        ItemStatusChangedEvent event = event(RequestItemType.PROVISION, "Entregue");
        when(requestPublicApi.findNotificationDataById(10L))
                .thenReturn(new RequestNotificationData(10L, List.of()));
        when(deliveryPublicApi.findActiveDeliveryByProvisionItem(10L, 99L))
                .thenReturn(Optional.empty());

        service.handle(event);

        verify(deliveryPublicApi).findActiveDeliveryByProvisionItem(10L, 99L);
        verify(emailSender).sendEmails(eq(event), eq(List.of()), any());
    }

    @Test
    void shouldNotSearchDeliveryWhenNewStatusIsNotDelivered() {
        ItemStatusChangedEvent event = event(RequestItemType.PRODUCT, "Em atendimento");
        when(requestPublicApi.findNotificationDataById(10L))
                .thenReturn(new RequestNotificationData(10L, List.of()));

        service.handle(event);

        verify(deliveryPublicApi, never()).findActiveDeliveryByProductItem(any(), any());
        verify(deliveryPublicApi, never()).findActiveDeliveryByProvisionItem(any(), any());
    }

    private ItemStatusChangedEvent event(RequestItemType itemType, String newStatus) {
        return new ItemStatusChangedEvent(
                10L, 99L, itemType, "Parafuso", "PRD-1", 2.0, "UN",
                "Em atendimento", newStatus, "Sem observacao", LocalDateTime.of(2026, 7, 13, 10, 30)
        );
    }

    private DeliveryNotificationData delivery() {
        return new DeliveryNotificationData(
                5L, null, "Retirada no SENAI", "Entrega parcial",
                List.of("Carlos"), List.of(), List.of("Instalacao")
        );
    }
}
