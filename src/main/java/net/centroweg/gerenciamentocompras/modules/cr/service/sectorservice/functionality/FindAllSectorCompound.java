package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.SectorMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Caso de uso responsável pela listagem de todos os {@link Sector} cadastrados de forma completa.
 */
@Service
@RequiredArgsConstructor
public class FindAllSectorCompound {

    private final SectorRepository repository;
    private final SectorMapper sectorMapper;

    /**
     * Lista todos os blocos cadastrados no banco de dados de forma completa.
     * @return lista com todos os blocos encontrados, caso exista.
     */
    public List<SectorCompoundResponse> findAllCompound(){
        return sectorMapper.toResponseCompoundList(repository.findAll());
    }
}
