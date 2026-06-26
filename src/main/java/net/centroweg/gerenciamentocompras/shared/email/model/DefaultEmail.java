package net.centroweg.gerenciamentocompras.shared.email.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Entidade padrão utilizada para envio de email
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DefaultEmail{

    /**
     * Sobre o que é o email?
     */
    @NonNull
    private String subject;

    /**
     * Para qual email será enviado?
     */
    @NonNull
    private String sendTo;

}