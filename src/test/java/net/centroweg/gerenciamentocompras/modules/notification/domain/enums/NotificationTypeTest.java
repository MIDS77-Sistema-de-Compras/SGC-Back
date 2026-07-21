package net.centroweg.gerenciamentocompras.modules.notification.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link NotificationType} enum.
 *
 * @since 20/07/2026
 */
class NotificationTypeTest {

    @Nested
    class EnumValues {

        @Test
        @DisplayName("Should contain all expected notification types")
        void shouldContainAllExpectedNotificationTypes() {
            // When
            List<NotificationType> allTypes = Arrays.asList(NotificationType.values());

            // Then
            assertThat(allTypes).containsExactlyInAnyOrder(
                NotificationType.STATUS_ALTERADO,
                NotificationType.ITEM_PARA_RETIRADA,
                NotificationType.ENTREGA_CRIADA,
                NotificationType.SOLICITACAO_VINCULADA_CR,
                NotificationType.NOTIFICACAO_TESTE,
                NotificationType.ALERTA_ADMINISTRATIVO
            );
        }

        @Test
        @DisplayName("Should have exactly 6 notification types")
        void shouldHaveExactlySixNotificationTypes() {
            // When
            int totalTypes = NotificationType.values().length;

            // Then
            assertThat(totalTypes).isEqualTo(6);
        }

        @Test
        @DisplayName("Should not contain duplicate values")
        void shouldNotContainDuplicateValues() {
            // When
            NotificationType[] values = NotificationType.values();
            boolean hasDuplicates = false;

            for (int i = 0; i < values.length; i++) {
                for (int j = i + 1; j < values.length; j++) {
                    if (values[i] == values[j]) {
                        hasDuplicates = true;
                        break;
                    }
                }
                if (hasDuplicates) break;
            }

            // Then
            assertThat(hasDuplicates).isFalse();
        }
    }

    @Nested
    class EnumUsage {

        @Test
        @DisplayName("Should be able to compare enum values using equals")
        void shouldCompareEnumValuesUsingEquals() {
            // Given
            NotificationType statusAlterado = NotificationType.STATUS_ALTERADO;
            NotificationType itemParaRetirada = NotificationType.ITEM_PARA_RETIRADA;

            // When/Then
            assertThat(statusAlterado).isEqualTo(NotificationType.STATUS_ALTERADO);
            assertThat(itemParaRetirada).isEqualTo(NotificationType.ITEM_PARA_RETIRADA);
            assertThat(statusAlterado).isNotEqualTo(itemParaRetirada);
        }

        @Test
        @DisplayName("Should return correct name for each enum constant")
        void shouldReturnCorrectNameForEachEnumConstant() {
            // When/Then
            assertThat(NotificationType.STATUS_ALTERADO.name()).isEqualTo("STATUS_ALTERADO");
            assertThat(NotificationType.ITEM_PARA_RETIRADA.name()).isEqualTo("ITEM_PARA_RETIRADA");
            assertThat(NotificationType.ENTREGA_CRIADA.name()).isEqualTo("ENTREGA_CRIADA");
            assertThat(NotificationType.SOLICITACAO_VINCULADA_CR.name()).isEqualTo("SOLICITACAO_VINCULADA_CR");
            assertThat(NotificationType.NOTIFICACAO_TESTE.name()).isEqualTo("NOTIFICACAO_TESTE");
            assertThat(NotificationType.ALERTA_ADMINISTRATIVO.name()).isEqualTo("ALERTA_ADMINISTRATIVO");
        }

        @Test
        @DisplayName("Should return correct ordinal for each enum constant")
        void shouldReturnCorrectOrdinalForEachEnumConstant() {
            // When/Then
            assertThat(NotificationType.STATUS_ALTERADO.ordinal()).isEqualTo(0);
            assertThat(NotificationType.ITEM_PARA_RETIRADA.ordinal()).isEqualTo(1);
            assertThat(NotificationType.ENTREGA_CRIADA.ordinal()).isEqualTo(2);
            assertThat(NotificationType.SOLICITACAO_VINCULADA_CR.ordinal()).isEqualTo(3);
            assertThat(NotificationType.NOTIFICACAO_TESTE.ordinal()).isEqualTo(4);
            assertThat(NotificationType.ALERTA_ADMINISTRATIVO.ordinal()).isEqualTo(5);
        }
    }

    @Nested
    class UsageInContext {

        @Test
        @DisplayName("Should be usable in notification creation context")
        void shouldBeUsableInNotificationCreationContext() {
            // Given - Simulating how NotificationType is used in services
            NotificationType tipoNotificacao = NotificationType.ITEM_PARA_RETIRADA;

            // When - This mimics the usage in HandleItemStatusChangedNotificationServiceImpl
            boolean isForRetirada = tipoNotificacao == NotificationType.ITEM_PARA_RETIRADA;
            boolean isNotForRetirada = tipoNotificacao != NotificationType.ITEM_PARA_RETIRADA;

            // Then
            assertThat(isForRetirada).isTrue();
            assertThat(isNotForRetirada).isFalse();
        }

        @Test
        @DisplayName("Should be distinguishable in switch statements")
        void shouldBeDistinguishableInSwitchStatements() {
            // When
            String descricao = switch (NotificationType.ENTREGA_CRIADA) {
                case STATUS_ALTERADO -> "Status alterado";
                case ITEM_PARA_RETIRADA -> "Item para retirada";
                case ENTREGA_CRIADA -> "Entrega criada";
                case ALERTA_ADMINISTRATIVO -> "Alerta administrativo";
                case SOLICITACAO_VINCULADA_CR -> "Solicitação vinculada à CR";
                case NOTIFICACAO_TESTE -> "Notificação de teste";
            };

            // Then
            assertThat(descricao).isEqualTo("Entrega criada");
        }

        @Test
        @DisplayName("Should correctly identify notification types in service context")
        void shouldCorrectlyIdentifyNotificationTypesInServiceContext() {
            // Given - Simulating the logic from HandleItemStatusChangedNotificationServiceImpl
            NotificationType tipoNotificacao = NotificationType.ITEM_PARA_RETIRADA;
            boolean isItemParaRetirada = tipoNotificacao == NotificationType.ITEM_PARA_RETIRADA;

            // When
            NotificationType selectedType = isItemParaRetirada
                ? NotificationType.ITEM_PARA_RETIRADA
                : NotificationType.STATUS_ALTERADO;

            // Then
            assertThat(selectedType).isEqualTo(NotificationType.ITEM_PARA_RETIRADA);
        }
    }
}
