package net.centroweg.gerenciamentocompras.shared.email.components;

import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;

/**
 * @deprecated O componente será implementado diretamente no corpo padrão do email assim que possível.
 * 
 * Contém o HTML necessário para criação do footer estilizado
 * 
 * Deve sempre ser utilizado por questões de padronização
 * 
 * @author gabrielEFagundes
 * @version 0.1.0
 */
public record EmailFooter() implements EmailBuilder {
    @Override
    public String render(){
        return """
                <tr>
                    <td align="center" style="background-color: #103D85; padding: 30px 20px; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;">
                        
                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="margin-bottom: 20px;">
                            <tr>
                                <td align="center" style="padding: 0 15px;">
                                    <img src="cid:logoSenai" alt="SENAI" width="120" style="display: block; border: 0; max-width: 120px; height: auto;">
                                </td>
                                <td align="center" style="padding: 0 15px;">
                                    <img src="cid:logoSgs" alt="SGS" width="80" style="display: block; border: 0; max-width: 80px; height: auto;">
                                </td>
                            </tr>
                        </table>
                        
                        <p style="margin: 0; color: #ffffff; font-size: 12px; font-family: Arial, sans-serif;">
                            SENAI | SGS &copy; 2026
                        </p>
                        
                    </td>
                </tr>
                """;
    }
}
