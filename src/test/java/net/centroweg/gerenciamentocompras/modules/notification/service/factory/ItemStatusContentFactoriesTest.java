package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ItemStatusContentFactoriesTest {

    private ItemStatusInternalNotificationFactory internalFactory;
    private ItemStatusEmailContentFactory emailFactory;

    @BeforeEach
    void setUp() {
        ItemStatusContentFormatter formatter = new ItemStatusContentFormatter();
        internalFactory = new ItemStatusInternalNotificationFactory(formatter);
        emailFactory = new ItemStatusEmailContentFactory(formatter);
    }

    @Test
    void shouldBuildPlainInternalTextWithAllDeliveryInformation() {
        ItemStatusInternalNotificationContent content = internalFactory.build(event(), Optional.of(delivery()));

        assertThat(content.message())
                .contains(
                        "Solicitacao atualizada: #10",
                        "Item atualizado: Parafuso <especial> (PRD-1)",
                        "Data de chegada/entrega: 13/07/2026 as 11:30",
                        "Local de retirada: SENAI <central>",
                        "Recebedores: Carlos <script>",
                        "- Instalacao"
                )
                .doesNotContain("<b>", "<br>", "<div>", "<table>");
    }

    @Test
    void shouldPreserveEmailHtmlAndEscapeEveryDynamicValue() {
        ItemStatusEmailContent content = emailFactory.build(event(), Optional.of(delivery()));

        assertThat(content.html())
                .contains(
                        "<b>Solicitacao atualizada:</b>",
                        "Parafuso &lt;especial&gt;",
                        "SENAI &lt;central&gt;",
                        "Carlos &lt;script&gt;",
                        "Observacao &lt;img onerror=alert(1)&gt;",
                        "Entrega &lt;insegura&gt;",
                        "Instalacao"
                )
                .doesNotContain("Parafuso <especial>", "<img onerror", "Carlos <script>");
    }

    @Test
    void shouldBuildDeliveredContentWithoutDeliveryAndNeverRenderNull() {
        ItemStatusInternalNotificationContent internal = internalFactory.build(event(), Optional.empty());
        ItemStatusEmailContent email = emailFactory.build(event(), Optional.empty());

        assertThat(internal.message()).contains("dados de entrega ainda nao vinculados").doesNotContain("null");
        assertThat(email.html()).contains("dados de entrega ainda nao vinculados").doesNotContain("null");
    }

    private ItemStatusChangedEvent event() {
        return new ItemStatusChangedEvent(
                10L, 99L, RequestItemType.PRODUCT, "Parafuso <especial>", "PRD-1", 2.0, "UN",
                "Em atendimento", "Entregue", "Observacao <img onerror=alert(1)>",
                LocalDateTime.of(2026, 7, 13, 10, 30)
        );
    }

    private DeliveryNotificationData delivery() {
        return new DeliveryNotificationData(
                7L,
                LocalDateTime.of(2026, 7, 13, 11, 30),
                "SENAI <central>",
                "Entrega <insegura>",
                List.of("Carlos <script>"),
                List.of(new DeliveryProductNotificationData("Parafuso <especial>", "PRD-1", 2.0, "UN")),
                List.of("Instalacao")
        );
    }
}
