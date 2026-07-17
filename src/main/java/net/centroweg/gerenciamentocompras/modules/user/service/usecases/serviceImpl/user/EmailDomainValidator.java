package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.exception.InvalidEmailDomainException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Valida se o e-mail do usuário pertence a um dos domínios institucionais permitidos.
 */
@Component
public class EmailDomainValidator {

    private static final List<String> ALLOWED_DOMAINS = List.of(
            "@fiesc.com.br",
            "@edu.sc.senai.br",
            "@sc.senai.br"
    );

    /**
     * Método que valida o domínio do e-mail informado
     * @param email e-mail a ser validado
     * @throws InvalidEmailDomainException se o domínio não for institucional
     */
    public void validate(String email){
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();

        boolean allowed = ALLOWED_DOMAINS.stream().anyMatch(normalizedEmail::endsWith);

        if(!allowed){
            throw new InvalidEmailDomainException(String.join(", ", ALLOWED_DOMAINS));
        }
    }
}
