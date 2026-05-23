package net.centroweg.gerenciamentocompras.modules.cr.service.crservice;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    public CrResponse update(long id, CrRequest dto){
        Cr cr = crRepository.findById(id).orElseThrow(()->new RuntimeException("CR não existe!"));
        cr.setName(dto.name());
        cr.setCode(dto.code());
        cr.setMaster(dto.master());

        return crMapper.toResponse(crRepository.save(cr));
    }
}
