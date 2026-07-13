package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendItemStatusChangedEmailServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private EmailSenderService emailSenderService;

    private SendItemStatusChangedEmailServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SendItemStatusChangedEmailServiceImpl(
                requestRepository,
                deliveryRepository,
                emailSenderService,
                new ItemStatusEmailMessageFactory()
        );
    }

    @Test
    void shouldSendOneEmailPerDistinctRequesterEmail() throws Exception {
        Request request = requestWithRequesters(
                user(1L, "Ana", "ana@teste.com"),
                user(2L, "Ana Clone", "ANA@teste.com")
        );
        ItemStatusChangedEvent event = statusChangedEvent();

        when(requestRepository.findWithRequestersById(10L)).thenReturn(Optional.of(request));
        when(deliveryRepository.findByRequestIdAndProductItemId(10L, 99L)).thenReturn(List.of());

        service.sendEmails(event);

        ArgumentCaptor<DefaultEmail> captor = ArgumentCaptor.forClass(DefaultEmail.class);
        verify(emailSenderService, times(1)).sendEmail(captor.capture(), anyString());

        assertThat(captor.getValue().getSendTo()).isEqualTo("ana@teste.com");
        assertThat(captor.getValue().getSubject()).contains("Solicitacao #10");
    }

    private ItemStatusChangedEvent statusChangedEvent() {
        return new ItemStatusChangedEvent(
                10L,
                99L,
                RequestItemType.PRODUCT,
                "Parafuso",
                "PRD-1",
                2.0,
                "UN",
                "EM_ANDAMENTO",
                "Entregue",
                "Sem observacao",
                LocalDateTime.now()
        );
    }

    private Request requestWithRequesters(User... users) {
        Request request = new Request();
        request.setId(10L);
        request.getCreatedByUsers().addAll(List.of(users));
        return request;
    }

    private User user(Long id, String name, String email) {
        User user = new User(name, "52998224725", email, "Senha@123", "1234", true);
        user.setId(id);
        return user;
    }
}
