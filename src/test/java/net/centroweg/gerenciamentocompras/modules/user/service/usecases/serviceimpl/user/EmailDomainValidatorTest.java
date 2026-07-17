package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.exception.InvalidEmailDomainException;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user.EmailDomainValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailDomainValidatorTest {

    private final EmailDomainValidator validator = new EmailDomainValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@fiesc.com.br",
            "aluno@edu.sc.senai.br",
            "docente@sc.senai.br",
            "MAIUSCULO@SC.SENAI.BR"
    })
    @DisplayName("Deve aceitar e-mails de domínios institucionais")
    void deveAceitarEmailsInstitucionais(String email) {
        assertDoesNotThrow(() -> validator.validate(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@gmail.com",
            "usuario@teste.com",
            "usuario@falso-sc.senai.br",
            "usuario@fiesc.com.br.fake.com"
    })
    @DisplayName("Deve rejeitar e-mails de domínios não institucionais")
    void deveRejeitarEmailsNaoInstitucionais(String email) {
        assertThrows(InvalidEmailDomainException.class, () -> validator.validate(email));
    }

    @Test
    @DisplayName("Deve rejeitar e-mail nulo")
    void deveRejeitarEmailNulo() {
        assertThrows(InvalidEmailDomainException.class, () -> validator.validate(null));
    }
}
