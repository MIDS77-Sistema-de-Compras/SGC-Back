package net.centroweg.gerenciamentocompras.modules.request.infrastructure.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;

/**
 * Classe listener que altera o status da solicitacao dinamicamente
 * conforme seus dados produtos sao alterados.
 *
 * @author gabrielEFagundes
 * @since 13/07/2026
 */
@Component
@RequiredArgsConstructor
public class MonitorItemStatusChanged {

    private static final String MIXED_STATUS = "PARCIALMENTE_ATENDIDA";

    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;

    /**
     * Quando o status de um produto eh atualizado, o metodo verifica o restante dos produtos e,
     * caso eles todos tenham os mesmos status, atualiza o status da solicitacao para seu equivalente.
     * <p>
     * i.e.
     * todos os itens "reprovado" -> solicitacao "reprovado" ou semelhante
     * todos os itens "entregue" -> solicitacao "entregue" ou semelhante
     * misturado -> solicitacao "parcial" ou semelhante

     * @param event O evento escutado pelo metodo
     */
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution=true)
    public void updateRequestStatus(ItemStatusChangedEvent event){
        Request request = requestRepository.findById(event.requestId())
                .orElseThrow(RequestNotFoundException::new);

        boolean allMatch = request.getItemRequestProducts().stream()
                .allMatch(item -> item.getStatus_id()
                                   .getName()
                                   .equalsIgnoreCase(event.newStatusName()));

        if (allMatch) {
            request.setStatus(
                    statusRepository.findByNameIgnoreCase(event.newStatusName())
                            .orElseThrow(StatusNotFoundException::new));
        } else {
            request.setStatus(
                    statusRepository.findByNameIgnoreCase(MIXED_STATUS)
                            .orElseThrow(StatusNotFoundException::new));
        }
    }
}