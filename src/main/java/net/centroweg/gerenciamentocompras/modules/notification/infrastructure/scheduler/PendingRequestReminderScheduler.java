package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Verifica periodicamente as solicitações que ficaram pendentes por muito tempo
 * e avisa os supervisores responsáveis por e-mail.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PendingRequestReminderScheduler {

    /** Nome do status considerado "pendente" (o status inicial da solicitação). */
    private static final String PENDING_STATUS = "EM_ANDAMENTO";
    /** Quantos dias pendente até disparar o aviso. */
    private static final int PENDING_DAYS_LIMIT = 7;

    private final RequestRepository requestRepository;
    private final NotificationEmailService notificationEmailService;

    // Envia lembrete a cada 7 dias enquanto a solicitação continuar pendente (EM_ANDAMENTO).
    @Scheduled(fixedRate = 604_800_000L)   // 7 dias em ms (7 * 24 * 60 * 60 * 1000)
    @Transactional
    public void notifyPendingRequests() {
        // Só lembra solicitações pendentes há mais de 7 dias.
        LocalDateTime limite = LocalDateTime.now().minusDays(PENDING_DAYS_LIMIT);

        List<Request> pendentes = requestRepository.findPendentesAntigas(PENDING_STATUS, limite);
        log.info("Solicitações pendentes encontradas: {}", pendentes.size());

        // Sem marcar "já avisei": enquanto a solicitação continuar EM_ANDAMENTO, o lembrete
        // se repete a cada execução. Ao aprovar/recusar, o status muda e ela sai da query.
        for (Request request : pendentes) {
            List<User> supervisores = request.getCrBranch().getResponsibleUsers();

            if (supervisores == null || supervisores.isEmpty()) {
                continue; // sem supervisor não há para quem avisar
            }

            for (User supervisor : supervisores) {
                notificationEmailService.sendPendingReminderEmail(
                        supervisor.getName(), supervisor.getEmail(), request.getId());
            }
        }
    }
}
