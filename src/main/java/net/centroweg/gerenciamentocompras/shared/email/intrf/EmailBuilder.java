package net.centroweg.gerenciamentocompras.shared.email.intrf;

/**
 * Interface que define o contrato de renderização dos componentes de email.
 */
public interface EmailBuilder {

    /**
     * Gera o HTML do componente de email.
     * @return HTML do componente já formatado.
     */
    String render();
}