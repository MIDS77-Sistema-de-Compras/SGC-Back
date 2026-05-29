package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import org.springframework.stereotype.Service;

/**
 * Responsável pela funcionalidade de criação de Centros de Responsabilidade (CR).
 */
@Service
@RequiredArgsConstructor
public class CreateCr{

    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Realiza a criação de um Centro de Responsabilidade (CR).
     * Os dados recebidos são convertidos para entidade, persistidos
     * no banco de dados e retornados como objeto de resposta.
     *
     * @param dto Objeto que contem os dados necessarios para a criação do CR
     * @return {@link CrResponse} com os dados do CR persistido
     * */
    public CrResponse create(CrRequest dto){
        return crMapper.toResponse(crRepository.save(crMapper.toEntity(dto)));
    }
}
