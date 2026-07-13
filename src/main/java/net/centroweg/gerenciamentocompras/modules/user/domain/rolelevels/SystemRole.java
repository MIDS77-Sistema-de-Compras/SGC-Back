package net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels;

import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;

import java.util.Locale;

public enum SystemRole {

    DOCENTE,
    COMPRADOR,
    SUPERVISOR,
    COORDENADOR,
    ADMIN;

    public static SystemRole from(String value) {
        if (value == null || value.isBlank()) {
            throw new RoleNotFoundException("Role não informada");
        }

        String normalized = value
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace(" ", "_");

        try {
            return SystemRole.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            throw new RoleNotFoundException(value);
        }
    }
}