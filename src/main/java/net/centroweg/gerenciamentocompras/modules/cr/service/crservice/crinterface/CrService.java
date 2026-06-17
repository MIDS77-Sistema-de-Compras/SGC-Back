package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

public interface CrService {

    CrCompoundResponse create(CrRequest dto);

    List<CrCompoundResponse> listAll();

    CrCompoundResponse listById(Long id);

    CrCompoundResponse update(Long id, CrRequest dto);

    MessageDTO delete(Long id);
}
