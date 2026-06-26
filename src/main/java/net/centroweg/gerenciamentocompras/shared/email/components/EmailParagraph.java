package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * Contém o HTML necessário para criação de um parágrafo no email.
 * 
 * @param content Conteúdo do email
 * @param hexColor Cor do parágrafo
 * @param fontSize Tamanho da fonte do parágrafo
 * 
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public record EmailParagraph(String content, String hexColor, int fontSize) implements EmailBuilder {

    @Override
    public String render(){
        return """
                <tr>
                    <td style="padding: 10px 30px 20px 30px; color: %s; font-size: %dpx; line-height: 1.6;">
                        %s
                    </td>
                <tr>
                """.formatted(hexColor, fontSize, content);
    }
}
