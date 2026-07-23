package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * Componente de email que representa um parágrafo.
 * @param content conteúdo do parágrafo.
 * @param hexColor cor do parágrafo em hexadecimal.
 * @param fontSize tamanho da fonte do parágrafo.
 */
public record EmailParagraph(String content, String hexColor, int fontSize) implements EmailBuilder {

    /**
     * Gera o HTML do parágrafo do email.
     * @return HTML do parágrafo já formatado.
     */
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
