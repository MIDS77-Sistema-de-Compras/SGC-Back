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
                .doesNotContain("modules.delivery.infrastructure.persistence.DeliveryRepository");
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
