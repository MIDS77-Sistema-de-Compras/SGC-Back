package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por atualizar os dados de um Centro de Resultado (CR).
 */
@Service
@RequiredArgsConstructor
public class UpdateCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Atualiza nome, código e flag master do CR identificado pelo ID informado.
     *
     * @param id  identificador do CR a ser atualizado
     * @param dto novos dados a serem aplicados
     * @return {@link CrCompoundResponse} com os dados após a atualização
     * @throws CrNotFoundException caso nenhum CR seja encontrado com o ID informado
     */
    public CrCompoundResponse update(Long id, CrRequest dto){
        Cr cr = crRepository.findById(id).orElseThrow(()->new CrNotFoundException(id));
        cr.setName(dto.name());
        cr.setCode(dto.code());
        cr.setMaster(dto.master());

        return crMapper.toCrCompoundResponse(crRepository.save(cr));
    }
}
