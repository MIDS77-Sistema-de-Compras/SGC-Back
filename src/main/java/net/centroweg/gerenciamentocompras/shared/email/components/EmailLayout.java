package net.centroweg.gerenciamentocompras.shared.email.components;

import java.util.List;
import java.util.stream.Collectors;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * Contém o HTML necessário para a criação do corpo padrão do email.
 * 
 * @param title Título do email (que aparece no nome da aba)
 * @param components Lista de componentes do email. Precisa implementar EmailBuilder para ser um componente
 * 
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public record EmailLayout(String title, List<EmailBuilder> components) {
    public String buildHtml(){
        String bodyContent = components.stream()
            .map(EmailBuilder::render)
            .collect(Collectors.joining());

        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                </head>
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif; -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%;">
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #f4f4f4; padding: 40px 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 500px; background-color: #ffffff; border-radius: 8px; border-top: 5px solid #103D85; border-collapse: collapse; box-shadow: 0 4px 8px rgba(0,0,0,0.05);">
                                    
                                    %s
                                    
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(title, bodyContent);
    }
}
