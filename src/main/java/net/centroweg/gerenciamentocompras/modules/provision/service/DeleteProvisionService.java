package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;

/**
 * Classe responsável por remover serviços do banco de dados.
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionRepository
 */
@Service
@RequiredArgsConstructor
public class DeleteProvisionService {
    
    private final ProvisionRepository provisionRepository;

    /**
     * Método responsável por deletar a entidade {@code Provision} com base em seu ID.
     * @param id O ID da {@code Provision} desejada.
     * @throws ProvisionNotFoundException Se a {@code Provision} não for encontrada no banco de dados.
     */
    public void  deleteProvisionById(Long id){
        if(!provisionRepository.existsById(id)){
            throw new ProvisionNotFoundException();
        }

        provisionRepository.deleteById(id);
    }

}
