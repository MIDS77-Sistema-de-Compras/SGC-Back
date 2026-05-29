package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

import java.util.List;

public interface CrService {

    CrResponse create(CrRequest dto);

    List<CrResponse> listAll();

    CrResponse listById(long id);

    CrResponse update(long id, CrRequest dto);

    MessageDTO delete(long id);
}
