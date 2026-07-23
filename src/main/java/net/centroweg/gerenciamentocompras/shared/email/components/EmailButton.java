package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * Componente de email que representa um botão estilizado.
 * @param link URL de destino do botão.
 * @param text texto exibido dentro do botão.
 */
public record EmailButton(String link, String text) implements EmailBuilder {
    @Override
    public String render(){
        return """
                <tr>
                    <td style="padding: 0 30px 30px 30px;">
                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%">
                            <tr>
                                <td align="left" style="padding-bottom: 20px;">
                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td align="center" bgcolor="#103D85" style="border-radius: 4px;">
                                                <a href="%s" style="display: inline-block; padding: 12px 24px; color: #ffffff; text-decoration: none; font-weight: bold; font-size: 14px;">%s</a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                """.formatted(link, text);
    }
}
