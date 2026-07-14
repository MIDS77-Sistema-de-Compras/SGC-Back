package net.centroweg.gerenciamentocompras.modules.delivery;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinTable;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiverId;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryReceiverRepository;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryHibernatePersistenceArchitectureTest {

    @Test
    void receiverShouldUseSerializableCompositeKeyWithoutGeneratedValue() throws Exception {
        var idField = DeliveryReceiver.class.getDeclaredField("id");

        assertThat(DeliveryReceiverId.class).hasAnnotation(Embeddable.class);
        assertThat(Serializable.class).isAssignableFrom(DeliveryReceiverId.class);
        assertThat(DeliveryReceiverId.class.getDeclaredField("deliveryId").getType()).isEqualTo(Long.class);
        assertThat(DeliveryReceiverId.class.getDeclaredField("userId").getType()).isEqualTo(Long.class);
        assertThat(idField.getType()).isEqualTo(DeliveryReceiverId.class);
        assertThat(idField.isAnnotationPresent(EmbeddedId.class)).isTrue();
        assertThat(idField.isAnnotationPresent(GeneratedValue.class)).isFalse();
        assertThat(DeliveryReceiver.class.getDeclaredFields())
                .noneMatch(field -> field.getType().equals(Long.class) && field.getName().equals("id"));
        assertThat(DeliveryReceiverRepository.class.getGenericInterfaces()[0].getTypeName())
                .contains("DeliveryReceiver", "DeliveryReceiverId")
                .doesNotContain("java.lang.Long");
    }

    @Test
    void deliveryItemLinksShouldBeUniqueHibernateManagedSets() throws Exception {
        assertAssociation("productItems", "delivery_item_request_product", "uk_delivery_product_item");
        assertAssociation("provisionItems", "delivery_item_request_service", "uk_delivery_service_item");
    }

    @Test
    void notificationMessageShouldRemainPlainTextCapableWithoutManualAlter() throws Exception {
        Column column = Notification.class.getDeclaredField("message").getAnnotation(Column.class);
        assertThat(column).isNotNull();
        assertThat(column.length()).isEqualTo(4000);
    }

    @Test
    void featureShouldNotContainManualSqlOrMigrationDependency() throws Exception {
        assertThat(Path.of("sql")).doesNotExist();
        try (var files = Files.walk(Path.of("src"))) {
            assertThat(files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString().toLowerCase())
                    .filter(name -> name.endsWith(".sql")))
                    .isEmpty();
        }
        assertThat(Files.readString(Path.of("pom.xml")).toLowerCase())
                .doesNotContain("flyway", "liquibase");
    }

    private void assertAssociation(String fieldName, String tableName, String constraintName) throws Exception {
        var field = Delivery.class.getDeclaredField(fieldName);
        JoinTable joinTable = field.getAnnotation(JoinTable.class);

        assertThat(field.getType()).isEqualTo(Set.class);
        assertThat(joinTable).isNotNull();
        assertThat(joinTable.name()).isEqualTo(tableName);
        assertThat(joinTable.uniqueConstraints())
                .extracting(constraint -> constraint.name())
                .contains(constraintName);
    }
}
