package net.centroweg.gerenciamentocompras.modules.notification;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ItemStatusNotificationArchitectureTest {

    private static final Path MAIN = Path.of("src", "main", "java", "net", "centroweg", "gerenciamentocompras");

    @Test
    void shouldNotAccessRepositoriesAcrossModulesInItemNotificationFlow() throws IOException {
        String requestSources = readJavaSources(MAIN.resolve("modules/request"));
        String notificationSources = readJavaSources(MAIN.resolve("modules/notification"));

        assertThat(requestSources)
                .doesNotContain("modules.notification.infrastructure.persistence.NotificationRepository")
                .doesNotContain("modules.delivery.infrastructure.persistence.DeliveryRepository");
        assertThat(notificationSources)
                .doesNotContain("modules.delivery.infrastructure.persistence.DeliveryRepository")
                .doesNotContain("modules.request.infrastructure.persistence.repository.RequestRepository")
                .doesNotContain("modules.user.infrastructure.persistence.UserRepository");
    }

    @Test
    void shouldKeepAllNotificationListenersInInfrastructureAndBehindInterfaces() throws IOException {
        for (String name : new String[]{"ItemStatusChangedEventListener.java", "RequestStatusChangedEventListener.java", "DeliveryCreatedEventListener.java"}) {
            String source = Files.readString(MAIN.resolve("modules/notification/infrastructure/listener").resolve(name));
            assertThat(source).contains("TransactionPhase.AFTER_COMMIT").doesNotContain("ServiceImpl");
        }
        assertThat(readJavaSources(MAIN.resolve("modules/request/service/notification"))).isBlank();
        assertThat(readJavaSources(MAIN.resolve("modules/delivery/service/notification"))).isBlank();
    }

    @Test
    void shouldKeepNotificationRepositoryInsideNotificationModule() throws IOException {
        String requestSources = readJavaSources(MAIN.resolve("modules/request"));
        String deliverySources = readJavaSources(MAIN.resolve("modules/delivery"));
        assertThat(requestSources).doesNotContain("NotificationRepository");
        assertThat(deliverySources).doesNotContain("NotificationRepository");
    }

    @Test
    void deliveryShouldUseStatusPublicApiAndOwnExceptions() throws IOException {
        String deliverySources = readJavaSources(MAIN.resolve("modules/delivery"));
        assertThat(deliverySources)
                .contains("Status")
                .doesNotContain("modules.request.infrastructure.persistence.repository.StatusRepository")
                .doesNotContain("DeliveredStatusImpl")
                .doesNotContain("modules.request.domain.exception.AcessDeniedException");
    }

    @Test
    void confirmationShouldUsePessimisticLockAndAudit() throws IOException {
        String repository = Files.readString(MAIN.resolve("modules/delivery/infrastructure/persistence/DeliveryRepository.java"));
        String controller = Files.readString(MAIN.resolve("modules/delivery/presentation/controller/DeliveryController.java"));
        assertThat(repository).contains("LockModeType.PESSIMISTIC_WRITE", "findByIdForUpdate");
        assertThat(controller).contains("CONFIRMAR_RECEBIMENTO_ENTREGA", "@AuditParam(\"user\")");
    }

    @Test
    void shouldKeepListenerInNotificationInfrastructureAndDependingOnInterface() throws IOException {
        Path listener = MAIN.resolve("modules/notification/infrastructure/listener/ItemStatusChangedEventListener.java");
        String source = Files.readString(listener);

        assertThat(source)
                .contains("HandleItemStatusChangedNotificationUseCase")
                .contains("phase = TransactionPhase.AFTER_COMMIT")
                .doesNotContain("ServiceImpl");
        assertThat(MAIN.resolve("modules/request/service/notification/ItemStatusChangedEventListener.java"))
                .doesNotExist();
    }

    private String readJavaSources(Path root) throws IOException {
        if (Files.notExists(root)) {
            return "";
        }
        try (var files = Files.walk(root)) {
            return files.filter(path -> path.toString().endsWith(".java"))
                    .map(this::readUnchecked)
                    .reduce("", (first, second) -> first + '\n' + second);
        }
    }

    private String readUnchecked(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
