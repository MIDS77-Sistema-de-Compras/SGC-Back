package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusInternalNotificationContent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleItemStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl.HandleItemStatusChangedNotificationServiceImpl;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.DeliveryPublicApi;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.ItemStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;

@ExtendWith(MockitoExtension.class)
class HandleItemStatusChangedNotificationServiceImplTest {

    @Mock
    private RequestPublicApi requestPublicApi;

    @Mock
    private DeliveryPublicApi deliveryPublicApi;

    @Mock
    private CreateInternalNotificationUseCase createInternalNotificationUseCase;

    @Mock
    private ItemStatusInternalNotificationFactory itemStatusInternalNotificationFactory;

    @Mock
    private ItemStatusEmailContentFactory itemStatusEmailContentFactory;

    @Mock
    private RequestNotificationRecipientDeduplicator requestNotificationRecipientDeduplicator;

    @Mock
    private ItemStatusChangedEmailSender itemStatusChangedEmailSender;

    private HandleItemStatusChangedNotificationServiceImpl itemStatusNotificationService;

    @BeforeEach
    void setUp() {
        itemStatusNotificationService = new HandleItemStatusChangedNotificationServiceImpl(
                requestPublicApi,
                deliveryPublicApi,
                createInternalNotificationUseCase,
                itemStatusInternalNotificationFactory,
                itemStatusEmailContentFactory,
                requestNotificationRecipientDeduplicator,
                itemStatusChangedEmailSender
        );
    }

    @Nested
    class ItemRetrievalScenario {

        @Test
        @DisplayName("Should use ITEM_PARA_RETIRADA type when item is marked for retrieval")
        void shouldUseItemParaRetiradaTypeWhenItemIsForRetrieval() {
            // Given
            Long requestId = 1L;
            Long userId = 1L;
            ItemStatusChangedEvent event = new ItemStatusChangedEvent(
                    requestId,  // requestId
                    10L, // itemId
                    RequestItemType.PRODUCT,
                    "Produto Teste",
                    "PROD001",
                    5.0,
                    "UN",
                    "Em preparo",
                    "Disponível para retirada",
                    "Pedido pronto para coleta",
                    java.time.LocalDateTime.now()
            );

            ItemStatusInternalNotificationContent mockContent = new ItemStatusInternalNotificationContent(
                    "Item disponível para retirada - Solicitação #" + requestId,
                    "Sua solicitação #" + requestId + " contém um item disponível para retirada."
            );

            RequestNotificationRecipient recipient = new RequestNotificationRecipient(userId, "João Silva", "joao@email.com");
            RequestNotificationData requestData = new RequestNotificationData(requestId, List.of(recipient));

            when(requestPublicApi.findNotificationDataById(eq(requestId)))
                    .thenReturn(requestData);
            when(requestNotificationRecipientDeduplicator.distinctUserIds(any()))
                    .thenReturn(List.of(userId));
            when(itemStatusInternalNotificationFactory.isForRetrieval(eq(event)))
                    .thenReturn(true);
            when(itemStatusInternalNotificationFactory.build(eq(event), any()))
                    .thenReturn(mockContent);

            // When
            itemStatusNotificationService.handle(event);

            // Then
            verify(createInternalNotificationUseCase).createNotifications(
                    eq("Item disponível para retirada - Solicitação #" + requestId),
                    eq("Sua solicitação #" + requestId + " contém um item disponível para retirada."),
                    eq(NotificationType.ITEM_PARA_RETIRADA),  // <-- This is what we're testing
                    eq(requestId),
                    eq(List.of(userId))
            );
        }

        @Test
        @DisplayName("Should use STATUS_ALTERADO type when item is NOT for retrieval")
        void shouldUseStatusAlteradoTypeWhenItemIsNotForRetrieval() {
            // Given
            Long requestId = 1L;
            Long userId = 1L;
            ItemStatusChangedEvent event = new ItemStatusChangedEvent(
                    requestId,  // requestId
                    10L, // itemId
                    RequestItemType.PRODUCT,
                    "Produto Teste",
                    "PROD001",
                    5.0,
                    "UN",
                    "Em preparo",
                    "Em transporte", // Not a retrieval status
                    "Item foi enviado para transportadora",
                    java.time.LocalDateTime.now()
            );

            ItemStatusInternalNotificationContent mockContent = new ItemStatusInternalNotificationContent(
                    "Status de item atualizado - Solicitação #" + requestId,
                    "O status do item foi atualizado para 'Em transporte'."
            );

            RequestNotificationRecipient recipient = new RequestNotificationRecipient(userId, "João Silva", "joao@email.com");
            RequestNotificationData requestData = new RequestNotificationData(requestId, List.of(recipient));

            when(requestPublicApi.findNotificationDataById(eq(requestId)))
                    .thenReturn(requestData);
            when(requestNotificationRecipientDeduplicator.distinctUserIds(any()))
                    .thenReturn(List.of(userId));
            when(itemStatusInternalNotificationFactory.isForRetrieval(eq(event)))
                    .thenReturn(false);
            when(itemStatusInternalNotificationFactory.build(eq(event), any()))
                    .thenReturn(mockContent);

            // When
            itemStatusNotificationService.handle(event);

            // Then
            verify(createInternalNotificationUseCase).createNotifications(
                    eq("Status de item atualizado - Solicitação #" + requestId),
                    eq("O status do item foi atualizado para 'Em transporte'."),
                    eq(NotificationType.STATUS_ALTERADO),  // <-- Different type
                    eq(requestId),
                    eq(List.of(userId))
            );
        }
    }
}