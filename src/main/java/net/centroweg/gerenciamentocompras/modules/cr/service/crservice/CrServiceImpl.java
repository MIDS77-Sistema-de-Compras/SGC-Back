package net.centroweg.gerenciamentocompras.modules.cr.service.crservice;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CrServiceImpl implements CrService{

    private final CreateCr createCr;

    @Override
    public CrResponse create(CrRequest dto) {
        return createCr.create(dto);
    }

}
