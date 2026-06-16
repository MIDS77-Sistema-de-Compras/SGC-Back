package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;


import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    public List<CrCompoundResponse> listAll(){
        return crRepository.findAll()
                .stream()
                .map(crMapper::toCrCompoundResponse)
                .toList();
    }


}
