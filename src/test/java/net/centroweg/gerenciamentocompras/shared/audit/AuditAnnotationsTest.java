package net.centroweg.gerenciamentocompras.shared.audit;

import net.centroweg.gerenciamentocompras.modules.auth.presentation.controller.AuthenticationController;
import net.centroweg.gerenciamentocompras.modules.request.presentation.controller.ItemRequestProductController;
import net.centroweg.gerenciamentocompras.modules.request.presentation.controller.ItemRequestProvisionController;
import net.centroweg.gerenciamentocompras.modules.request.presentation.controller.RequestController;
import net.centroweg.gerenciamentocompras.modules.user.presentation.controller.UserController;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditParam;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de <b>presença de anotações</b> de auditoria (Spring AOP / AspectJ).
 *
 * <p>Estes testes usam Reflection para validar, de forma isolada e sem subir o contexto do Spring,
 * que as anotações {@link Auditable} e {@link AuditParam} estão aplicadas nos métodos e parâmetros
 * corretos dos Controllers. Eles NÃO exercem o comportamento do {@code AuditLogAspect}; apenas
 * documentam e "travam" o contrato de anotações atualmente presente no código.</p>
 *
 * <p><b>Achados importantes documentados por estes testes</b> (ver comentários em cada bloco):</p>
 * <ul>
 *   <li>Nos fluxos de itens, o {@code @AuditParam("request")} está aplicado sobre o <i>corpo</i>
 *       (DTO) e não sobre um parâmetro {@code Long}. Como o aspecto só extrai IDs de argumentos
 *       {@code instanceof Long} ({@code AuditLogAspect.extractIdByAnnotation}), o vínculo
 *       item &rarr; solicitação-pai NÃO é resolvido em tempo de execução.</li>
 *   <li>{@code ItemRequestProvisionController} possui {@code @Auditable} apenas no POST; PUT e
 *       DELETE não são auditados.</li>
 *   <li>As ações reais divergem de alguns nomes esperados: login = {@code "LOGAR"} (não
 *       "LOGIN_SISTEMA"); reset de senha = {@code "SOLICITAR_ALTERACAO_SENHA"} /
 *       {@code "ALTERAR_SENHA"} (não "REDEFINIR_SENHA").</li>
 * </ul>
 */
@DisplayName("Auditoria - presença de anotações (@Auditable / @AuditParam)")
class AuditAnnotationsTest {

    // ---------------------------------------------------------------------
    // Helpers de reflection
    // ---------------------------------------------------------------------

    /** Localiza um método pelo nome (os nomes são únicos dentro de cada controller aqui testado). */
    private static Method method(Class<?> type, String name) {
        for (Method m : type.getDeclaredMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new AssertionError("Método não encontrado: " + type.getSimpleName() + "#" + name);
    }

    private static Auditable auditable(Class<?> type, String methodName) {
        Auditable a = method(type, methodName).getAnnotation(Auditable.class);
        assertThat(a)
                .as("%s#%s deve estar anotado com @Auditable", type.getSimpleName(), methodName)
                .isNotNull();
        return a;
    }

    private static void assertAuditable(Class<?> type, String methodName, String action, boolean targetFromReturn) {
        Auditable a = auditable(type, methodName);
        assertThat(a.action()).isEqualTo(action);
        assertThat(a.targetFromReturn()).isEqualTo(targetFromReturn);
    }

    private static boolean hasAuditParam(Method m, int index, String value) {
        Parameter p = m.getParameters()[index];
        AuditParam ap = p.getAnnotation(AuditParam.class);
        return ap != null && ap.value().equals(value);
    }

    /** Índice do primeiro parâmetro anotado com {@code @AuditParam(value)}, ou -1. */
    private static int auditParamIndex(Method m, String value) {
        Parameter[] params = m.getParameters();
        for (int i = 0; i < params.length; i++) {
            AuditParam ap = params[i].getAnnotation(AuditParam.class);
            if (ap != null && ap.value().equals(value)) {
                return i;
            }
        }
        return -1;
    }

    // ---------------------------------------------------------------------
    // UserController
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("UserController")
    class UserControllerAnnotations {

        @Test
        @DisplayName("POST createUser -> @Auditable(action=CRIAR_USUARIO, targetFromReturn=true)")
        void createUser() {
            assertAuditable(UserController.class, "createUser", "CRIAR_USUARIO", true);
            // Não há @AuditParam: o alvo é extraído do corpo do ResponseEntity<UserResponse>,
            // via UserResponse.id() (targetFromReturn = true).
            assertThat(auditParamIndex(method(UserController.class, "createUser"), "user")).isEqualTo(-1);
        }

        @Test
        @DisplayName("PUT updateUser -> @Auditable(ATUALIZAR_USUARIO) e @AuditParam(\"user\") no Long userId")
        void updateUser() {
            assertAuditable(UserController.class, "updateUser", "ATUALIZAR_USUARIO", false);
            Method m = method(UserController.class, "updateUser");
            int idx = auditParamIndex(m, "user");
            assertThat(idx).as("updateUser deve ter @AuditParam(\"user\")").isNotEqualTo(-1);
            // O parâmetro anotado é um Long -> o aspecto consegue extrair o ID.
            assertThat(m.getParameters()[idx].getType()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("DELETE deleteUser -> @Auditable(DESATIVAR_USUARIO) e @AuditParam(\"user\") no Long userId")
        void deleteUser() {
            assertAuditable(UserController.class, "deleteUser", "DESATIVAR_USUARIO", false);
            Method m = method(UserController.class, "deleteUser");
            assertThat(hasAuditParam(m, 0, "user")).isTrue();
            assertThat(m.getParameters()[0].getType()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("PATCH updateProfilePicture -> @Auditable(ATUALIZAR_FOTO_DE_PERFIL) e @AuditParam(\"user\") no Long id")
        void updateProfilePicture() {
            assertAuditable(UserController.class, "updateProfilePicture", "ATUALIZAR_FOTO_DE_PERFIL", false);
            Method m = method(UserController.class, "updateProfilePicture");
            assertThat(hasAuditParam(m, 0, "user")).isTrue();
            assertThat(m.getParameters()[0].getType()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("POST changeUserPassword -> @Auditable(ATUALIZAR_SENHA); @AuditParam(\"user\") está no UserPrincipal (não-Long)")
        void changeUserPassword() {
            assertAuditable(UserController.class, "changeUserPassword", "ATUALIZAR_SENHA", false);
            Method m = method(UserController.class, "changeUserPassword");
            int idx = auditParamIndex(m, "user");
            assertThat(idx).isNotEqualTo(-1);
            // ATENÇÃO (achado): aqui o @AuditParam("user") está sobre UserPrincipal, e não sobre um Long.
            // O AuditLogAspect só extrai IDs de argumentos "instanceof Long", portanto este @AuditParam
            // NÃO é utilizável pelo aspecto neste método.
            assertThat(m.getParameters()[idx].getType()).isNotEqualTo(Long.class);
        }
    }

    // ---------------------------------------------------------------------
    // RequestController
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("RequestController")
    class RequestControllerAnnotations {

        @Test
        @DisplayName("POST createRequest -> @Auditable(CRIAR_SOLICITACAO, targetFromReturn=true)")
        void createRequest() {
            assertAuditable(RequestController.class, "createRequest", "CRIAR_SOLICITACAO", true);
            // ATENÇÃO (achado): createRequest tem @AuditParam("request") sobre o corpo (RequestRequest)
            // e @AuditParam("user") sobre o UserPrincipal. Nenhum é Long, então ambos são inertes para o
            // aspecto. Com targetFromReturn=true o alvo cai no fallback extractIdFromReturnObject, que lê
            // RequestResponse.id() -> ou seja, o ID da SOLICITAÇÃO é usado como se fosse um userId
            // (findByUserId). Este é um comportamento potencialmente incorreto e está apenas documentado aqui.
            Method m = method(RequestController.class, "createRequest");
            int reqIdx = auditParamIndex(m, "request");
            assertThat(reqIdx).isNotEqualTo(-1);
            assertThat(m.getParameters()[reqIdx].getType()).isNotEqualTo(Long.class);
        }

        @Test
        @DisplayName("PUT updateRequest -> @Auditable(ATUALIZAR_SOLICITACAO) e @AuditParam(\"request\") no Long id")
        void updateRequest() {
            assertAuditable(RequestController.class, "updateRequest", "ATUALIZAR_SOLICITACAO", false);
            Method m = method(RequestController.class, "updateRequest");
            int idx = auditParamIndex(m, "request");
            assertThat(idx).isNotEqualTo(-1);
            assertThat(m.getParameters()[idx].getType()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("DELETE deleteRequest -> @Auditable(DESATIVAR_SOLICITACAO) e @AuditParam(\"request\") no Long id")
        void deleteRequest() {
            assertAuditable(RequestController.class, "deleteRequest", "DESATIVAR_SOLICITACAO", false);
            Method m = method(RequestController.class, "deleteRequest");
            assertThat(hasAuditParam(m, 0, "request")).isTrue();
            assertThat(m.getParameters()[0].getType()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("PATCH updateFeedback -> @Auditable(ADICIONAR_FEEDBACK) e @AuditParam(\"request\") no Long id")
        void updateFeedback() {
            assertAuditable(RequestController.class, "updateFeedback", "ADICIONAR_FEEDBACK", false);
            Method m = method(RequestController.class, "updateFeedback");
            int idx = auditParamIndex(m, "request");
            assertThat(idx).isNotEqualTo(-1);
            assertThat(m.getParameters()[idx].getType()).isEqualTo(Long.class);
        }

        @Test
        @DisplayName("PATCH updateStatus -> @Auditable(ATUALIZAR_STATUS_SOLICITACAO) e @AuditParam(\"request\") no Long id")
        void updateStatus() {
            assertAuditable(RequestController.class, "updateStatus", "ATUALIZAR_STATUS_SOLICITACAO", false);
            Method m = method(RequestController.class, "updateStatus");
            assertThat(hasAuditParam(m, 0, "request")).isTrue();
            assertThat(m.getParameters()[0].getType()).isEqualTo(Long.class);
        }
    }

    // ---------------------------------------------------------------------
    // AuthenticationController
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("AuthenticationController")
    class AuthControllerAnnotations {

        @Test
        @DisplayName("POST /auth/login -> @Auditable(action=LOGAR)")
        void login() {
            // NOTA: o enunciado esperava "LOGIN_SISTEMA", mas a ação real no código é "LOGAR".
            assertAuditable(AuthenticationController.class, "login", "LOGAR", false);
        }

        @Test
        @DisplayName("POST /auth/recovery -> @Auditable(action=SOLICITAR_ALTERACAO_SENHA)")
        void recovery() {
            // NOTA: relacionado ao reset de senha (esperado no enunciado como "REDEFINIR_SENHA").
            assertAuditable(AuthenticationController.class, "sendEmailWithToken", "SOLICITAR_ALTERACAO_SENHA", false);
        }

        @Test
        @DisplayName("POST /auth/recovery/new -> @Auditable(action=ALTERAR_SENHA)")
        void changePassword() {
            assertAuditable(AuthenticationController.class, "validateAndChangePassword", "ALTERAR_SENHA", false);
        }
    }

    // ---------------------------------------------------------------------
    // ItemRequestProductController  (achados: @AuditParam no corpo, não em Long)
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("ItemRequestProductController (documenta lacuna do vínculo request)")
    class ItemRequestProductAnnotations {

        @Test
        @DisplayName("POST createItemRequestProduct -> @Auditable(ADICIONAR_ITEM_PRODUTO); @AuditParam(\"request\") está no DTO (não-Long)")
        void create() {
            assertAuditable(ItemRequestProductController.class, "createItemRequestProduct", "ADICIONAR_ITEM_PRODUTO", false);
            Method m = method(ItemRequestProductController.class, "createItemRequestProduct");
            int idx = auditParamIndex(m, "request");
            assertThat(idx).as("a anotação @AuditParam(\"request\") existe").isNotEqualTo(-1);
            // ACHADO: o parâmetro anotado é o corpo (ItemRequestProductRequest), NÃO um Long.
            // Logo, AuditLogAspect.extractIdByAnnotation nunca extrai o requestId (filtro instanceof Long),
            // e o AuditLog não é associado à solicitação-pai. Teste "trava" essa realidade atual.
            assertThat(m.getParameters()[idx].getType()).isNotEqualTo(Long.class);
        }

        @Test
        @DisplayName("PUT updateItemRequestProduct -> @Auditable(ATUALIZAR_ITEM_PRODUTO); @AuditParam(\"request\") no DTO, Long id NÃO anotado")
        void update() {
            assertAuditable(ItemRequestProductController.class, "updateItemRequestProduct", "ATUALIZAR_ITEM_PRODUTO", false);
            Method m = method(ItemRequestProductController.class, "updateItemRequestProduct");
            int idx = auditParamIndex(m, "request");
            assertThat(idx).isNotEqualTo(-1);
            // Mesmo achado do POST: a anotação está sobre o DTO, não sobre o Long id (param 0).
            assertThat(m.getParameters()[idx].getType()).isNotEqualTo(Long.class);
            assertThat(hasAuditParam(m, 0, "request")).as("o Long id NÃO está anotado hoje").isFalse();
        }

        @Test
        @DisplayName("DELETE deleteItemRequestProduct -> @Auditable(REMOVER_ITEM_PRODUTO), sem @AuditParam")
        void delete() {
            assertAuditable(ItemRequestProductController.class, "deleteItemRequestProduct", "REMOVER_ITEM_PRODUTO", false);
            Method m = method(ItemRequestProductController.class, "deleteItemRequestProduct");
            assertThat(auditParamIndex(m, "request")).as("hoje não há @AuditParam no delete").isEqualTo(-1);
        }
    }

    // ---------------------------------------------------------------------
    // ItemRequestProvisionController  (achado: DELETE sem @Auditable)
    // ---------------------------------------------------------------------

    @Nested
    @DisplayName("ItemRequestProvisionController (documenta ausência de auditoria no DELETE)")
    class ItemRequestProvisionAnnotations {

        @Test
        @DisplayName("POST addItem -> @Auditable(ADICIONAR_ITEM), porém SEM @AuditParam(\"request\")")
        void create() {
            assertAuditable(ItemRequestProvisionController.class, "addItem", "ADICIONAR_ITEM", false);
            Method m = method(ItemRequestProvisionController.class, "addItem");
            // ACHADO: o requestId vive dentro do DTO e não há @AuditParam algum -> sem vínculo com a solicitação.
            assertThat(auditParamIndex(m, "request")).isEqualTo(-1);
        }

        @Test
        @DisplayName("PUT updateItem -> @Auditable(ATUALIZAR_ITEM_SERVIÇO), com @AuditParam(\"request\") sobre o DTO")
        void update() {
            assertAuditable(ItemRequestProvisionController.class, "updateItem", "ATUALIZAR_ITEM_SERVIÇO", false);
            Method m = method(ItemRequestProvisionController.class, "updateItem");
            int idx = auditParamIndex(m, "request");
            assertThat(idx).isNotEqualTo(-1);
            // Mesmo padrão do ItemRequestProduct: a anotação está sobre o DTO, não sobre o Long itemId (param 0).
            assertThat(m.getParameters()[idx].getType()).isNotEqualTo(Long.class);
            assertThat(hasAuditParam(m, 0, "request")).as("o Long itemId NÃO está anotado hoje").isFalse();
        }

        @Test
        @DisplayName("DELETE deleteItem -> NÃO auditado (sem @Auditable)")
        void delete() {
            assertThat(method(ItemRequestProvisionController.class, "deleteItem").getAnnotation(Auditable.class))
                    .as("hoje deleteItem NÃO tem @Auditable")
                    .isNull();
        }
    }
}
