package net.centroweg.gerenciamentocompras.modules.request.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RequestStatusNamesTest {

    @Test
    void shouldNormalizeCanonicalAndLegacyNamesEqually() {
        assertEquals(
                RequestStatusNames.normalize("AGUARDANDO_APROVACAO"),
                RequestStatusNames.normalize("Aguardando aprovação")
        );
    }

    @Test
    void shouldPresentCanonicalNamesForUsers() {
        assertEquals("Aguardando aprovação", RequestStatusNames.toDisplayName("AGUARDANDO_APROVACAO"));
        assertEquals("Solicitando orçamento", RequestStatusNames.toDisplayName("SOLICITANDO_ORCAMENTO"));
    }
}
