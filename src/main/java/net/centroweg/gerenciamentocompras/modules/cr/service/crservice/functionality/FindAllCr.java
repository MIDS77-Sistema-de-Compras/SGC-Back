package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;


import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
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
     * @return lista de {@link CrCompoundResponse}
     */
    public List<CrCompoundResponse> listAll(){
        return crRepository.findAll()
                .stream()
                .map(crMapper::toCrCompoundResponse)
                .toList();
    }


}
