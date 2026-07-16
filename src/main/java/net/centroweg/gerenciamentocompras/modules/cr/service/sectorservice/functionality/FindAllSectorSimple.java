package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Caso de uso responsável pela listagem de um {@link Sector} de forma simples.
 */
@Service
@RequiredArgsConstructor
public class FindAllSectorSimple {

    private final SectorRepository repository;
    private final SectorMapper sectorMapper;

    /**
     * Retorna todos os blocos cadastrados no banco de dados de forma simples.
     * @return lista com todos os blocos encontrados.
     */
    public List<SectorSimpleResponse> findAllSimple(){
        return sectorMapper.toResponseSimpleList(repository.findAll());
    }
}
