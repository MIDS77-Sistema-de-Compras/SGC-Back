package net.centroweg.gerenciamentocompras.modules.request.infrastructure.listener;

import java.util.Set;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
    private static final Set<String> REVIEW_DECISION_STATUSES = Set.of("aprovado", "recusado");

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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution=true)
    public void updateRequestStatus(ItemStatusChangedEvent event){
        // Aprovar/recusar item pertence a etapa de revisao. Nessa etapa, o frontend
        // envia todas as decisoes e depois conclui o status geral uma unica vez.
        // Recalcular aqui encerrava a revisao no primeiro item e fazia a chamada final
        // retornar erro, apesar de as alteracoes dos itens ja terem sido persistidas.
        if (isReviewDecision(event.newStatusName())) {
            return;
        }

        Request request = requestRepository.findById(event.requestId())
                .orElseThrow(RequestNotFoundException::new);

        boolean hasProducts = !request.getItemRequestProducts().isEmpty();

        // Uma solicitacao eh so-produtos ou so-servicos, nunca as duas — por isso da pra
        // decidir qual lista checar pela que nao esta vazia. allMatch numa lista vazia
        // (Java) da "true" por padrao, entao sem o isEmpty() aqui uma solicitacao de
        // servico (produtos vazio) acabava "batendo" mesmo com so 1 item alterado.
        boolean allMatch = hasProducts
                ? request.getItemRequestProducts().stream()
                        .allMatch(item -> item.getStatus_id()
                                           .getName()
                                           .equalsIgnoreCase(event.newStatusName()))
                : !request.getItemRequestProvisions().isEmpty()
                        && request.getItemRequestProvisions().stream()
                                .allMatch(item -> item.getStatus()
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

    private boolean isReviewDecision(String statusName) {
        return statusName != null
                && REVIEW_DECISION_STATUSES.contains(statusName.trim().toLowerCase(Locale.ROOT));
    }
}
