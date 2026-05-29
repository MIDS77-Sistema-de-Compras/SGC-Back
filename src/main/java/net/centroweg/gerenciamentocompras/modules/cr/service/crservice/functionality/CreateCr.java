package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCr{

    private final CrRepository crRepository;
    private final CrMapper crMapper;

    public CrResponse create(CrRequest dto){
        return crMapper.toResponse(crRepository.save(crMapper.toEntity(dto)));
    }
}
