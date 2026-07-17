package net.centroweg.gerenciamentocompras.modules.request.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Classe listener que altera o status da solicitação dinâmicamente
 * conforme seus dados produtos são alterados.
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
     * Quando o status de um produto é atualizado, o método verifica o restante dos produtos e, 
     * caso eles todos tenham os mesmos status, atualiza o status da solicitação para seu equivalente.
     * <p>
     * i.e.
     * todos os itens "reprovado" -> requisição "reprovado" ou semelhante
     * todos os itens "entregue" -> requisição "entregue" ou semelhante
     * misturado -> requisição "parcial" ou semelhante

     * @param event O evento escutado pelo método
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
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