package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrInstructorMapper;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * Caso de uso responsável pela listagem de todos os {@link CrInstructor}.
 */
@Service
@RequiredArgsConstructor
public class GetAllCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    private final CrInstructorMapper crInstructorMapper;

    /**
     * Lista todos os CR-instrutores cadastrados no banco de dados.
     * @return lista com todos os vínculos encontrados, caso exista.
     */
    public List<CrInstructorResponse> getAll(){
        return crInstructorMapper.toResponseList(crInstructorRepository.findAll());
    }

}
