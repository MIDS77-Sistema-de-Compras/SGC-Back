package net.centroweg.gerenciamentocompras.modules.cr.service.crservice;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;

import java.util.List;

public interface CrService {

    CrResponse create(CrRequest dto);

}
