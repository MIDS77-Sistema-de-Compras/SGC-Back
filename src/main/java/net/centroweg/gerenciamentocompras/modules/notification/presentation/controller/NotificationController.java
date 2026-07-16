package net.centroweg.gerenciamentocompras.modules.notification.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Notification}.
 */
@Tag(name = "ENDPOINTS da entidade de notificações")
@RequiredArgsConstructor
@RequestMapping("/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Lista todas as notificações cadastradas pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista com todas as notificações encontradas.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de notificações por usuário")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findNotificationsByUser(userId));
    }

    /**
     * Lista todas as notificações cadastradas do usuário logado.
     * @param userPrincipal dados do usuário.
     * @return lista com todas as notificações encontradas.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de notificações do próprio usuário logado")
    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponse>> findByOwnUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findByOwnUser(userPrincipal));
    }

    /**
     * Marca a notificação como visualizada.
     * @param id identificador da notificação.
     * @return notificação já atualizada.
     */
    @Operation(description = "ENDPOINT responsável por marcar notificações como visualizada")
    @PatchMapping("/{id}/viewed")
    public ResponseEntity<NotificationResponse> markAsViewed(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.markAsViewed(id));
    }

    /**
     * Lista todas as notificações não visualizadas por usuário.
     * @param userId identificador do usuário.
     * @return lista com todas as notificações encontradas.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de notificações não visualizadas por usuário")
    @GetMapping("user/{userId}/unviewed")
    public ResponseEntity<List<NotificationResponse>> findUnviewedByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findUnviewedNotificationsByUser(userId));
    }
}
