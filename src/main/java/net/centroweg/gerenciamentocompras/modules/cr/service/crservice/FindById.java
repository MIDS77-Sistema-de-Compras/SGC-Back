package net.centroweg.gerenciamentocompras.modules.cr.service.crservice;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindById {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    public CrResponse listById(long id){
        Cr cr = crRepository.findById(id).orElseThrow(() -> new CrNotFoundException(id));
        return crMapper.toResponse(cr);
    }
}
