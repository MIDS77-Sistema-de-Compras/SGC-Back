package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestStatusEmailMessageFactoryTest {

    private final RequestStatusEmailMessageFactory factory = new RequestStatusEmailMessageFactory(
            "http://localhost:3000/docente/solicitacoes/{requestId}"
    );

    @Test
    void shouldBuildRefusalEmailWithFormattedDataAndRequesterUrl() {
        RequestStatusEmailContent content = factory.build(event("Recusado", "Sem orçamento"), request(), requester());

        assertThat(content.subject()).isEqualTo("Solicitação #123 atualizada: Recusado");
        assertThat(content.html())
                .contains("13/07/2026 &agrave;s 15:30")
                .contains("Sem or&ccedil;amento")
                .contains("Respons&aacute;vel")
                .contains("http://localhost:3000/docente/solicitacoes/123")
                .doesNotContain("coordenador/solicitacoes");
    }

    @Test
    void shouldEscapeEveryDynamicHtmlValue() {
        User requester = requester();
        requester.setName("<script>alert('nome')</script>");
        Request request = request();
        request.getCrBranch().getCr().setName("<b>CR</b>");
        request.getCrBranch().getBranch().setName("Filial <img src=x>");
        RequestStatusChangedEvent event = new RequestStatusChangedEvent(
                123L,
                "EM_ANALISE<script>",
                "RECUSADO",
                "<script>alert('justificativa')</script>",
                50L,
                "<b>Responsável</b>",
                LocalDateTime.of(2026, 7, 13, 15, 30)
        );

        String html = factory.build(event, request, requester).html();

        assertThat(html)
                .contains("&lt;script&gt;alert(&#39;nome&#39;)&lt;/script&gt;")
                .contains("&lt;b&gt;Respons&aacute;vel&lt;/b&gt;")
                .contains("&lt;b&gt;CR&lt;/b&gt;")
                .contains("Filial &lt;img src=x&gt;")
                .contains("&lt;script&gt;alert(&#39;justificativa&#39;)&lt;/script&gt;")
                .doesNotContain("<script>alert", "<img src=x>");
    }

    @Test
    void shouldFormatUnderscoredStatusNames() {
        assertThat(factory.formatStatusName("  EM_ATENDIMENTO  ")).isEqualTo("Em atendimento");
    }

    private RequestStatusChangedEvent event(String newStatus, String justification) {
        return new RequestStatusChangedEvent(
                123L,
                "EM_ANALISE",
                newStatus,
                justification,
                50L,
                "Responsável",
                LocalDateTime.of(2026, 7, 13, 15, 30)
        );
    }

    private Request request() {
        CrBranch crBranch = new CrBranch(
                new Branch("Filial Centro"),
                new Cr("Tecnologia", "7940", false),
                List.of()
        );
        Request request = new Request(crBranch, new Status("Recusado", "Status"));
        request.setId(123L);
        request.setRequestDate(LocalDateTime.of(2026, 7, 10, 9, 0));
        return request;
    }

    private User requester() {
        User user = new User();
        user.setId(1L);
        user.setName("Maria");
        user.setEmail("maria@teste.com");
        return user;
    }
}
