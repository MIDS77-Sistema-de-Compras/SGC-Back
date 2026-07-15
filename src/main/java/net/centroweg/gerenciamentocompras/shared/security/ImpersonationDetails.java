package net.centroweg.gerenciamentocompras.shared.security;

/**
 * Dados do administrador que está logado na conta de outro usuário
 * (impersonação). Anexado como "details" da autenticação pelo
 * SecurityFilter quando o token JWT possui as claims de impersonação.
 *
 * @param adminEmail e-mail do administrador que iniciou a impersonação
 * @param adminName  nome do administrador que iniciou a impersonação
 */
public record ImpersonationDetails(String adminEmail, String adminName) {
}
