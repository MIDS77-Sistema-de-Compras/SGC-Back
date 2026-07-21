package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Caso de uso responsável por buscar um {@link Sector} pelo seu identificador de forma simples.
 */
@Service
@RequiredArgsConstructor
public class FindSectorByIdSimple {

    private final SectorRepository repository;
    private final SectorMapper sectorMapper;

    /**
     * Busca um bloco no banco de dados pelo ID informado de forma simples.
     * @param id identificador do bloco.
     * @return bloco encontrado, caso exista.
     * @throws SectorNotFoundException caso nenhum bloco seja encontrado com o ID informado.
     */
    public SectorSimpleResponse findByIdSimple(Long id){
        return sectorMapper.toResponseSimple(repository.findById(id)
                .orElseThrow(() -> new SectorNotFoundException()));
    }
}
