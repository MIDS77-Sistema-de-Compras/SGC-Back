package net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Caso de uso responsável por remover um {@link Provision}.
 */
@Service
@RequiredArgsConstructor
public class DeleteProvisionService {
    
    private final ProvisionRepository provisionRepository;

    /**
     * Remove um serviço do banco de dados.
     * @param id identificador do serviço.
     * @throws ProvisionNotFoundException caso nenhum serviço seja encontrado.
     */
    public void  deleteProvisionById(Long id){
        if(!provisionRepository.existsById(id)){
            throw new ProvisionNotFoundException();
        }

        provisionRepository.deleteById(id);
    }

}
