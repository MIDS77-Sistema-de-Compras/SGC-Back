package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Caso de uso responsável por deletar um {@link Sector}.
 */
@Service
@RequiredArgsConstructor
public class DeleteSector {

    private final SectorRepository repository;

    /**
     * Deleta um bloco do banco de dados.
     * @param id identificador do bloco.
     */
    public void deleteSector(Long id){
        repository.deleteById(id);
    }
}
