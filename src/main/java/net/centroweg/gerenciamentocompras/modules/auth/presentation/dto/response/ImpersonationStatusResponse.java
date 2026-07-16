package net.centroweg.gerenciamentocompras.modules.auth.presentation.dto.response;

/**
 * Estado atual de impersonação da sessão.
 *
 * @param impersonating {@code true} se o token atual é de um administrador
 *                      logado na conta de outro usuário
 * @param adminName     nome do administrador original (nulo se não há impersonação)
 * @param userName      nome do usuário da conta em uso (nulo se não há impersonação)
 */
public record ImpersonationStatusResponse(
        boolean impersonating,
        String adminName,
        String userName
) {
}
