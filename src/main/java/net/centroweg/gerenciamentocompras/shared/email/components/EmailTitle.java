package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * Componente de email que representa um título.
 * @param text conteúdo do título.
 */
public record EmailTitle(String text) implements EmailBuilder {

    /**
     * Gera o HTML do título do email.
     * @return HTML do título já formatado.
     */
    @Override
    public String render(){
        return """
                <tr>
                    <td style="padding: 30px 30px 20px 30px; color: #333333; font-size: 24px; line-height: 1.6;">
                        <b>%s</b>
                    </td>
                <tr>
                """.formatted(text);
    }
}
