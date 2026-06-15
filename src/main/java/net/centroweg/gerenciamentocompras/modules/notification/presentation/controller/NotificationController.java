package net.centroweg.gerenciamentocompras.modules.notification.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findNotificationsByUser(userId));
    }

    @PatchMapping("/{id}/viewed")
    public ResponseEntity<NotificationResponse> markAsViewed(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.markAsViewed(id));
    }

    @GetMapping("user/{userId}/unviewed")
    public ResponseEntity<List<NotificationResponse>> findUnviewedByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findUnviewedNotificationsByUser(userId));
    }
}
