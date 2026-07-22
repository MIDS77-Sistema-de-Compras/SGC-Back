package net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceImpl.CreateDeliveryForApprovedRequestServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener que reage à aprovação de uma solicitação criando
 * automaticamente a entrega correspondente com status "Em atendimento".
 *
 * <p>A criação da entrega é best-effort: uma falha aqui não pode
 * quebrar a operação de aprovação da solicitação.</p>
 */
@Component
@RequiredArgsConstructor
public class RequestApprovedEventListener {

    private static final Logger log = LoggerFactory.getLogger(RequestApprovedEventListener.class);

    private final CreateDeliveryForApprovedRequestServiceImpl createDeliveryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRequestApproved(RequestApprovedEvent event) {
        try {
            createDeliveryService.createForRequest(event.requestId());
        } catch (Exception e) {
            log.error("Falha ao criar entrega automática para a solicitação {}", event.requestId(), e);
        }
    }
}
