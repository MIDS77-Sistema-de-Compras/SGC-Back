package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;

/**
 * Interface que define o contrato para o serviço de autenticação do sistema.
 */
public interface AuthenticationService {

     /**
      * Autentica um usuário com base nas credenciais informadas.
      * @param loginDto objeto contendo as credenciais de acesso do usuário.
      * @return token JWT gerado após a autenticação bem-sucedida.
      */
     String login(LogIn loginDto);
}
