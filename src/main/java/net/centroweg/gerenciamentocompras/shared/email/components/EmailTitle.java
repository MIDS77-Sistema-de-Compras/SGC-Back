package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * Contém o HTML necessário para criação de um título no email.
 * 
 * Um pouco mais estilizado que um parágrafo, prefirir isto do que um parágrafo estilizado como título.
 * 
 * @param text Conteúdo do título
 * 
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public record EmailTitle(String text) implements EmailBuilder {
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
