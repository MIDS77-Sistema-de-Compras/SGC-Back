package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

// I've made this a generic so you can parse strings, numbers, whatever you want to format into the email
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
