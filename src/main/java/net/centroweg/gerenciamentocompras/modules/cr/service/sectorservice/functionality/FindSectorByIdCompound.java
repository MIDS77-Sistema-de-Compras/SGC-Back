package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Caso de uso responsável por buscar um {@link Sector} pelo seu identificador de forma completa.
 */
@Service
@RequiredArgsConstructor
public class FindSectorByIdCompound {

    private final SectorMapper sectorMapper;
    private final SectorRepository repository;

    /**
     * Busca um bloco no banco de dados pelo ID informado de forma completa.
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    public SectorCompoundResponse findSectorByIdCompound(Long id){
        return sectorMapper.toResponseCompound(repository.findById(id)
                .orElseThrow(() -> new SectorNotFoundException()));
    }
}
