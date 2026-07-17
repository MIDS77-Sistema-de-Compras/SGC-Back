package net.centroweg.gerenciamentocompras.modules.request.service.mapper.request;

import net.centroweg.gerenciamentocompras.modules.request.domain.RequestStatusCategory;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Set;

/**
 * Resolve a {@link RequestStatusCategory} de uma solicitação a partir do nome do seu status.
 *
 * <p>{@code Status} é uma entidade de nome livre, sem enum fixo, então a categorização
 * é feita por nome normalizado (sem acento, minúsculo, "_" tratado como espaço) — o mesmo
 * critério já usado em {@code RequestBusinessRuleValidator} para as regras de permissão.
 * Um nome de status cadastrado por um administrador que não conste nas listas abaixo
 * resulta em {@code null} (sem categoria conhecida).</p>
 *
 * @author André
 * @since 1.0
 */
@Component
public class RequestStatusCategoryResolver {

    private static final Set<String> PENDENTE_NAMES = Set.of(
            "em andamento",
            "em analise",
            "aguardando aprovacao",
            "aprovado",
            "auto aprovado",
            "em atendimento"
    );

    private static final Set<String> CONCLUIDA_NAMES = Set.of(
            "recusado",
            "reprovado",
            "cancelado",
            "pedido cancelado",
            "entregue"
    );

    /**
     * Resolve a categoria do status informado.
     *
     * @param statusName nome do status da solicitação
     * @return a categoria conhecida, ou {@code null} se o nome não for reconhecido
     */
    public RequestStatusCategory resolve(String statusName) {
        String normalized = normalize(statusName);

        if (CONCLUIDA_NAMES.contains(normalized)) {
            return RequestStatusCategory.CONCLUIDA;
        }

        if (PENDENTE_NAMES.contains(normalized)) {
            return RequestStatusCategory.PENDENTE;
        }

        return null;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        String withoutAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return withoutAccents.replace("_", " ").trim().toLowerCase();
    }
}
