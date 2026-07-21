package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * Caso de uso responsável por listar todos os {@link Cr} cadastrados.
 */
@Service
@RequiredArgsConstructor
public class FindAllCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Lista todos os CRs cadastrados no banco de dados.
     * @return lista com todos os CRs encontrados, caso exista.
     */
    public List<CrCompoundResponse> listAll(){
        return crRepository.findAll()
                .stream()
                .map(crMapper::toCrCompoundResponse)
                .toList();
    }


}
