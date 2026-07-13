package net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.CreateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.UpdateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

final class DeliveryServiceTestFixtures {

    private DeliveryServiceTestFixtures() {
    }

    static Request request() {
        Request request = new Request();
        request.setId(10L);
        return request;
    }

    static Status status() {
        Status status = new Status("EM_ANDAMENTO", "Em andamento");
        status.setId(20L);
        return status;
    }

    static Status deliveredStatus() {
        Status status = new Status("Entregue", "Entrega concluida");
        status.setId(30L);
        return status;
    }

    static User user(Long id, String name, Boolean active) {
        User user = new User(name, "52998224725", name.toLowerCase() + "@teste.com", "Senha@123", "1234", active);
        user.setId(id);
        return user;
    }

    static Delivery delivery(Request request, Status status, User... receivers) {
        Delivery delivery = new Delivery();
        delivery.setId(100L);
        delivery.setRequest(request);
        delivery.setStatusId(status.getId());
        delivery.setExpectedDeliveryAt(LocalDateTime.now().plusDays(1));
        delivery.setDeliveryLocation("Portaria");
        delivery.setActive(true);
        delivery.setCreatedAt(LocalDateTime.now());
        delivery.setUpdatedAt(LocalDateTime.now());
        for (User receiver : receivers) {
            delivery.addReceiver(receiver);
        }
        return delivery;
    }

    static CreateDeliveryRequest createRequest(List<Long> receiverIds) {
        return new CreateDeliveryRequest(
                10L,
                20L,
                LocalDateTime.now().plusDays(1),
                "Portaria",
                "Entrega parcial",
                "https://example.com/prova.pdf",
                receiverIds
        );
    }

    static UpdateDeliveryRequest updateRequest(List<Long> receiverIds) {
        return new UpdateDeliveryRequest(
                20L,
                LocalDateTime.now().plusDays(2),
                null,
                "Almoxarifado",
                "Entrega atualizada",
                "https://example.com/prova.pdf",
                receiverIds
        );
    }

    static ConfirmDeliveryReceiverRequest confirmRequest() {
        return new ConfirmDeliveryReceiverRequest("ok");
    }

    static DeliveryResponse response() {
        return new DeliveryResponse(
                100L,
                10L,
                20L,
                "EM_ANDAMENTO",
                LocalDateTime.now().plusDays(1),
                null,
                "Portaria",
                null,
                null,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of()
        );
    }
}
