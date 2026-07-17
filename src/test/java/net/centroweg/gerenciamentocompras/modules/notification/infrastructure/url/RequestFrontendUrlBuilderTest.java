package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.url;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class RequestFrontendUrlBuilderTest {

    private static final String REQUESTER_TEMPLATE = "https://sgc-front-nine.vercel.app/solicitacoes/{requestId}";
    private static final String MANAGEMENT_TEMPLATE = "https://sgc-front-nine.vercel.app/solicitacoes/gestao/{requestId}";

    @Test
    void shouldBuildRequesterRequestUrl() {
        // Arrange
        var builder = new RequestFrontendUrlBuilder(REQUESTER_TEMPLATE, MANAGEMENT_TEMPLATE);

        // Act
        String url = builder.buildRequesterRequestUrl(10L);

        // Assert
        assertThat(url).isEqualTo("https://sgc-front-nine.vercel.app/solicitacoes/10");
    }

    @Test
    void shouldBuildManagementRequestUrl() {
        // Arrange
        var builder = new RequestFrontendUrlBuilder(REQUESTER_TEMPLATE, MANAGEMENT_TEMPLATE);

        // Act
        String url = builder.buildManagementRequestUrl(10L);

        // Assert
        assertThat(url).isEqualTo("https://sgc-front-nine.vercel.app/solicitacoes/gestao/10");
    }

    @Test
    void shouldBuildIndependentUrlsForDifferentRequestIds() {
        // Arrange
        var builder = new RequestFrontendUrlBuilder(REQUESTER_TEMPLATE, MANAGEMENT_TEMPLATE);

        // Act
        String firstUrl = builder.buildManagementRequestUrl(10L);
        String secondUrl = builder.buildManagementRequestUrl(25L);

        // Assert
        assertThat(firstUrl).isEqualTo("https://sgc-front-nine.vercel.app/solicitacoes/gestao/10");
        assertThat(secondUrl).isEqualTo("https://sgc-front-nine.vercel.app/solicitacoes/gestao/25");
    }

    @Test
    void shouldRejectRequesterTemplateWithoutRequestIdPlaceholder() {
        // Arrange / Act
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new RequestFrontendUrlBuilder(
                        "https://sgc-front-nine.vercel.app/solicitacoes", MANAGEMENT_TEMPLATE
                ))
                .withMessageContaining("app.frontend.requester-request-url-template", "{requestId}");
    }

    @Test
    void shouldRejectManagementTemplateWithoutRequestIdPlaceholder() {
        // Arrange / Act
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new RequestFrontendUrlBuilder(
                        REQUESTER_TEMPLATE, "https://sgc-front-nine.vercel.app/solicitacoes/gestao"
                ))
                .withMessageContaining("app.frontend.coordinator-request-url-template", "{requestId}");
    }

    @Test
    void shouldRejectBlankTemplate() {
        // Arrange / Act
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new RequestFrontendUrlBuilder("   ", MANAGEMENT_TEMPLATE))
                .withMessageContaining("app.frontend.requester-request-url-template", "{requestId}");
    }

    @Test
    void shouldRejectNullTemplate() {
        // Arrange / Act
        // Assert
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new RequestFrontendUrlBuilder(REQUESTER_TEMPLATE, null))
                .withMessageContaining("app.frontend.coordinator-request-url-template", "{requestId}");
    }

    @Test
    void shouldRejectNullRequestId() {
        // Arrange
        var builder = new RequestFrontendUrlBuilder(REQUESTER_TEMPLATE, MANAGEMENT_TEMPLATE);

        // Act / Assert
        assertThatIllegalArgumentException()
                .isThrownBy(() -> builder.buildRequesterRequestUrl(null))
                .withMessageContaining("requestId");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> builder.buildManagementRequestUrl(null))
                .withMessageContaining("requestId");
    }
}
