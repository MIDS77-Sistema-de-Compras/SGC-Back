package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorimpl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface.CrInstructorService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.CreateCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.DeleteCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.GetAllCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.GetByIdCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.UpdateCrInstructor;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

/**
 * Classe de serviço do {@link CrInstructor} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link CrInstructorService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class CrInstructorServiceImpl implements CrInstructorService {

    /**
     * Componente responsável pela criação de um vínculo CR-instrutor.
     */
    private final CreateCrInstructor createCrInstructor;

    /**
     * Componente responsável pela listagem de todos os vínculos CR-instrurores cadastrados.
     */
    private final GetAllCrInstructor getAllCrInstructor;

    /**
     * Componente responsável pela busca de um vínculo CR-instrutor pelo ID informado.
     */
    private final GetByIdCrInstructor getByIdCrInstructor;

    /**
     * Componente responsável por atualizar um vínculo CR-instrutor.
     */
    private final UpdateCrInstructor updateCrInstructor;

    /**
     * Componente responsável por remover um vínculo CR-instrutor.
     */
    private final DeleteCrInstructor deleteCrInstructor;

    /**
     * Cria e persiste um novo vínculo CR-instrutor no banco de dados.
     * @param request dados do vínculo.
     * @return vínculo criado.
     */
    @Override
    public CrInstructorResponse create(CrInstructorRequest request) {
        return createCrInstructor.addCrInstructor(request);
    }

    /**
     * Lista todos os vínculos CR-instrutores cadastrados no banco de dados.
     * @return lista com todos os vínculos encontrados, caso exista.
     */
    @Override
    public List<CrInstructorResponse> findAll() {
        return getAllCrInstructor.getAll();
    }

    /**
     * Busca um vínculo CR-instrutor no banco de dados pelo ID informado.
     * @param id identificador do vínculo.
     * @return vínculo encontrado, caso exista.
     */
    @Override
    public CrInstructorResponse findById(Long id) {
        return getByIdCrInstructor.getById(id);
    }

    /**
     * Atualiza um vínculo CR-instrutor existente no banco de dados.
     * @param id identificador do vínculo.
     * @param request novos dados do vínculo.
     * @return vínculo já atualizado.
     */
    @Override
    public CrInstructorResponse update(Long id, CrInstructorRequest request) {
        return updateCrInstructor.update(id, request);
    }

    /**
     * Remove um vínculo CR-instrutor do banco de dados.
     * @param id identificador do vínculo.
     * @return mensagem de confirmação da remoção.
     */
    @Override
    public MessageDTO delete(Long id) {
        return deleteCrInstructor.delete(id);
    }
    
}
