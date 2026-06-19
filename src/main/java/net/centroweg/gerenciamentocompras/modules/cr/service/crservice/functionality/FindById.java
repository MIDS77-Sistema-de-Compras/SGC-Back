package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;

import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar um Centro de Resultado (CR) pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindById {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Localiza um CR pelo ID e retorna o DTO de resposta.
     *
     * @param id identificador do CR
     * @return {@link CrCompoundResponse} com os dados do CR
     * @throws CrNotFoundException caso nenhum CR seja encontrado com o ID informado
     */
    public CrCompoundResponse listById(Long id){
        Cr cr = crRepository.findById(id).orElseThrow(() -> new CrNotFoundException(id));
        return crMapper.toCrCompoundResponse(cr);
    }
}
