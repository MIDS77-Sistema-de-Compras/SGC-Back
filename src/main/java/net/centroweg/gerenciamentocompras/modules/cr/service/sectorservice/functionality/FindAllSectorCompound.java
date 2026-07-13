package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Caso de uso responsável pela listagem de {@link Sector} de forma completa.
 */
@Service
@RequiredArgsConstructor
public class FindAllSectorCompound {

    private final SectorRepository repository;
    private final SectorMapper sectorMapper;

    /**
     * Retorna todos os blocos cadastrados no banco de dados de forma completa.
     * @return lista com todos os blocos encontrados.
     */
    public List<SectorCompoundResponse> findAllCompound(){
        return sectorMapper.toResponseCompoundList(repository.findAll());
    }
}
