package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendDeliveryCreatedEmailServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private EmailSenderService emailSenderService;

    private SendDeliveryCreatedEmailServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SendDeliveryCreatedEmailServiceImpl(
                deliveryRepository,
                emailSenderService,
                new DeliveryCreatedEmailMessageFactory()
        );
    }

    @Test
    void shouldSendOneEmailPerDistinctReceiverEmail() throws Exception {
        Delivery delivery = delivery(
                user(1L, "Ana", "ana@teste.com"),
                user(2L, "Ana Clone", "ANA@teste.com")
        );
        when(deliveryRepository.findById(100L)).thenReturn(Optional.of(delivery));

        service.sendEmails(new DeliveryCreatedEvent(100L, 10L, LocalDateTime.now()));

        ArgumentCaptor<DefaultEmail> captor = ArgumentCaptor.forClass(DefaultEmail.class);
        verify(emailSenderService, times(1)).sendEmail(captor.capture(), anyString());

        assertThat(captor.getValue().getSendTo()).isEqualTo("ana@teste.com");
        assertThat(captor.getValue().getSubject()).contains("Solicitacao #10");
    }

    private Delivery delivery(User... users) {
        Request request = new Request();
        request.setId(10L);
        Status status = new Status("EM_ANDAMENTO", "Em andamento");

        Delivery delivery = new Delivery();
        delivery.setId(100L);
        delivery.setRequest(request);
        delivery.setStatus(status);
        delivery.setExpectedDeliveryAt(LocalDateTime.now().plusDays(1));
        delivery.setDeliveryLocation("SENAI - Almoxarifado");
        delivery.setActive(true);
        for (User user : users) {
            delivery.addReceiver(user);
        }
        return delivery;
    }

    private User user(Long id, String name, String email) {
        User user = new User(name, "52998224725", email, "Senha@123", "1234", true);
        user.setId(id);
        return user;
    }
}
