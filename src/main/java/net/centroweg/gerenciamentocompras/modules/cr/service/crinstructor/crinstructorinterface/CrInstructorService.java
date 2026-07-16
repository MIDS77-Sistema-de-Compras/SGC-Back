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
     * Cria e persiste um novo vínculo CR-instrutor no banco de dados.
     * @param request dados do CR-instrutor.
     * @return vínculo criado.
     */
    CrInstructorResponse create(CrInstructorRequest request);

    /**
     * Lista todos os vínculos CR-instrutor cadastrados no banco de dados.
     * @return lista com todos os vínculos encontrados.
     */
    List<CrInstructorResponse> findAll();

    /**
     * Busca um vínculo CR-instrutor no banco de dados pelo ID informado.
     * @param id identificador do CR-instrutor.
     * @return vínculo encontrado.
     */
    CrInstructorResponse findById(Long id);

    /**
     * Atualiza um vínculo CR-instrutor existente no banco de dados.
     * @param id identificador do CR-instrutor.
     * @param request novos dados do CR-instrutor.
     * @return vínculo atualizado.
     */
    CrInstructorResponse update(Long id, CrInstructorRequest request);

    /**
     * Remove um vínculo CR-instrutor do banco de dados.
     * @param id identificador do CR-instrutor.
     * @return mensagem de confirmação da remoção.
     */
    MessageDTO delete(Long id);
    
}
