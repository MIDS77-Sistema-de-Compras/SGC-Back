package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSector {

    private final SectorRepository repository;

    public void deleteSector(Long id){
        repository.deleteById(id);
    }
}
