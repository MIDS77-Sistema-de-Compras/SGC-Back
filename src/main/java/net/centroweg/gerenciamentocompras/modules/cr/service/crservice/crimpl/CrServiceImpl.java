package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface.CrService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality.*;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementação de {@link CrService} que delega cada operação
 * ao seu caso de uso específico.
 */
@RequiredArgsConstructor
@Component
public class CrServiceImpl implements CrService {

    private final CreateCr createCr;
    private final FindAllCr findAllCr;
    private final FindById findById;
    private final UpdateCr updateCr;
    private final DeleteCr deleteCr;

    @Override
    public CrResponse create(CrRequest dto) {
        return createCr.create(dto);
    }

    @Override
    public List<CrResponse> listAll(){
        return findAllCr.listAll();
    }

    @Override
    public CrResponse listById(long id){
        return findById.listById(id);
    }

    @Override
    public CrResponse update(long id, CrRequest dto){
        return updateCr.update(id, dto);
    }

    @Override
    public MessageDTO delete(long id){
        return deleteCr.delete(id);
    }
}
