package net.centroweg.gerenciamentocompras.modules.notification.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade NOTIFICATION")
@RequiredArgsConstructor
@RequestMapping("/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(description = "ENDPOINT responsável pela listagem de Notification por usuário")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findNotificationsByUser(userId));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Notification do próprio usuário logado")
    @GetMapping("/me")
    public ResponseEntity<Page<NotificationResponse>> findByOwnUser(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findByOwnUser(userPrincipal, pageable));
    }

    @Operation(description = "ENDPOINT responsável por marcar Notification como visualizada")
    @PatchMapping("/{id}/viewed")
    public ResponseEntity<NotificationResponse> markAsViewed(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.markAsViewed(id));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Notification não visualizadas por usuário")
    @GetMapping("user/{userId}/unviewed")
    public ResponseEntity<List<NotificationResponse>> findUnviewedByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findUnviewedNotificationsByUser(userId));
    }
}
