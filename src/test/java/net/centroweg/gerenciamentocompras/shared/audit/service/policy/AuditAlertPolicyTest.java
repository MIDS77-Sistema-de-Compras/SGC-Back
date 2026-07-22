package net.centroweg.gerenciamentocompras.shared.audit.service.policy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuditAlertPolicyTest {

    private final AuditAlertPolicy policy = new AuditAlertPolicy();

    @Test
    void shouldClassifyAdministrativeAlertActions() {
        assertThat(policy.isAlert("DESATIVAR_USUARIO")).isTrue();
        assertThat(policy.isAlert("EXCLUIR_CR")).isTrue();
        assertThat(policy.isAlert("REMOVER_ITEM_PRODUTO")).isTrue();
        assertThat(policy.isAlert("ATUALIZAR_SOLICITACAO")).isTrue();
        assertThat(policy.isAlert("ALTERAR_STATUS_ATIVACAO_USUARIO")).isTrue();
    }

    @Test
    void shouldIgnoreOrdinaryActions() {
        assertThat(policy.isAlert("CRIAR_SOLICITACAO")).isFalse();
        assertThat(policy.isAlert("LOGAR")).isFalse();
        assertThat(policy.isAlert(null)).isFalse();
        assertThat(policy.isAlert(" ")).isFalse();
    }
}
