package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

/**
 * Interface de serviço para operações de gerenciamento de {@link CrInstructor}.
 */
public interface CrInstructorService {

    /**
     * Cria um novo vínculo entre CR-filial e usuário.
     * @param request dados do CR-instrutor.
     * @return vínculo criado.
     */
    CrInstructorResponse create(CrInstructorRequest request);

    /**
     * Lista os vínculos cadastrados.
     * @return lista com todos os vínculos encontrados.
     */
    List<CrInstructorResponse> findAll();

    /**
     * Busca um vínculo pelo ID.
     * @param id identificador do CR-instrutor.
     * @return vínculo encontrado.
     */
    CrInstructorResponse findById(Long id);

    /**
     * Atualiza um vínculo existente.
     * @param id identificador do CR-instrutor.
     * @param request novos dados do CR-instrutor.
     * @return vínculo atualizado.
     */
    CrInstructorResponse update(Long id, CrInstructorRequest request);

    /**
     * Deleta um vínculo.
     * @param id identificador do CR-instrutor.
     * @return mensagem de confirmação da remoção.
     */
    MessageDTO delete(Long id);
    
}
