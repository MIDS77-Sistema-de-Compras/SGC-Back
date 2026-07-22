package net.centroweg.gerenciamentocompras.modules.request.service.util;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Nomes canônicos e normalização dos status de solicitação.
 *
 * <p>Os nomes persistidos usam caixa alta e, em sua maioria, sublinhado. A
 * normalização é usada somente em comparações de regras de negócio; consultas e
 * gravações continuam usando o valor canônico exato do banco.</p>
 */
public final class RequestStatusNames {

    public static final String AGUARDANDO_APROVACAO = "AGUARDANDO_APROVACAO";
    public static final String AUTO_APROVADO = "AUTO_APROVADO";
    public static final String EM_ATENDIMENTO = "EM_ATENDIMENTO";
    public static final String ENTREGUE = "ENTREGUE";
    public static final String PEDIDO_CANCELADO = "PEDIDO CANCELADO";

    private RequestStatusNames() {
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }

        String withoutAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return withoutAccents
                .replace('_', ' ')
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }

    public static String toDisplayName(String value) {
        String normalized = normalize(value);
        if (normalized.isEmpty()) {
            return "Não informado";
        }

        return switch (normalized) {
            case "aguardando aprovacao" -> "Aguardando aprovação";
            case "auto aprovado" -> "Auto-aprovado";
            case "parcialmente aprovada" -> "Parcialmente aprovada";
            case "em atendimento" -> "Em atendimento";
            case "recebimento parcial" -> "Recebimento parcial";
            case "fundo rotativo" -> "Fundo rotativo";
            case "cd central" -> "CD Central";
            case "solicitado portal" -> "Solicitado Portal";
            case "solicitando orcamento" -> "Solicitando orçamento";
            case "parcialmente atendida" -> "Parcialmente atendida";
            case "pedido cancelado" -> "Pedido cancelado";
            default -> normalized.substring(0, 1).toUpperCase(Locale.forLanguageTag("pt-BR"))
                    + normalized.substring(1);
        };
    }
}
