package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrInstructorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

@Service
@RequiredArgsConstructor
public class DeleteCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    
    public MessageDTO delete(Long id){
        if(!crInstructorRepository.existsById(id)){
            throw new CrInstructorNotFoundException("Nenhum supervisor vinculado à filial foi encontrado.");
        }
        crInstructorRepository.deleteById(id);

        return new MessageDTO("Supervisor desvinculado da filial.");
    }

}
