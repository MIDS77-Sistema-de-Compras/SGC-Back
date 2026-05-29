package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;


import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável por listar todos os Centros de Resultado (CR) cadastrados.
 */
@Service
@RequiredArgsConstructor
public class FindAllCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Retorna todos os CRs persistidos, convertidos para o DTO de resposta.
     *
     * @return lista de {@link CrResponse}
     */
    public List<CrResponse> listAll(){
        return crRepository.findAll()
                .stream()
                .map(crMapper::toResponse)
                .toList();
    }


}
