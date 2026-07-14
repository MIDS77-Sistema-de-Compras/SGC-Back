package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrInstructorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrInstructorMapper;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * Caso de uso responsável por buscar um {@link CrInstructor} pelo ID.
 */
@Service
@RequiredArgsConstructor
public class GetByIdCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    private final CrInstructorMapper crInstructorMapper;

    /**
     * Busca um CR-instrutor no banco de dados pelo ID informado.
     * @param id identificador do CR-instrutor.
     * @return vínculo encontrado.
     */
    public CrInstructorResponse getById(Long id){
        return crInstructorMapper.toResponse(
            crInstructorRepository.findById(id).orElseThrow(() -> new CrInstructorNotFoundException("Nenhum supervisor vinculado à filial foi encontrado."))
        );
    }

}
