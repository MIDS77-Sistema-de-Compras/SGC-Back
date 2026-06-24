package net.centroweg.gerenciamentocompras.shared.email.intrf;

/**
 * A interface implementada por basicamente todos os componentes de email.
 * 
 * Define se é ou não um componente de email.
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public interface EmailBuilder {

    /**
     * Renderiza o email com o HTML implementado por cada componente de email.
     * @return O HTML do componente
     */
    String render();
}