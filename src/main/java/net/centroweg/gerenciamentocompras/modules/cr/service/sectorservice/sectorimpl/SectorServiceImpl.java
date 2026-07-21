package net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorimpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.functionality.*;
import net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorinterface.SectorService;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * Classe de serviço do {@link Sector} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link SectorService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class SectorServiceImpl implements SectorService {

    /**
     * Componente responsável pela criação de um bloco.
     */
    private final CreateSector create;

    /**
     * Componente responsável pela listagem de todos os blocos cadastrados de forma simples.
     */
    private final FindAllSectorSimple findAllSimple;

    /**
     * Componente responsável pela listagem de todos os blocos cadastrados de forma completa.
     */
    private final FindAllSectorCompound findAllCompound;

    /**
     * Componente responsável por buscar um bloco pelo ID informado de forma simples.
     */
    private final FindSectorByIdSimple findByIdSimple;

    /**
     * Componente responsável por buscar um bloco pelo ID informado de forma completa.
     */
    private final FindSectorByIdCompound findByIdCompound;

    /**
     * Componente responsável por atualizar um bloco existente.
     */
    private final UpdateSector update;

    /**
     * Componente responsável por remover um bloco.
     */
    private final DeleteSector delete;

    /**
     * Cria e persiste um novo bloco no banco de dados.
     * @param sector dados do bloco.
     * @return bloco criado.
     */
    @Override
    public SectorSimpleResponse createSector(SectorRequest sector){
        return create.createSector(sector);
    }

    /**
     * Lista todos os blocos cadastrados no banco de dados de forma simples.
     * @return lista com todos os blocos encontrados, caso exista.
     */
    @Override
    public List<SectorSimpleResponse> findAllSectorSimple(){
        return findAllSimple.findAllSimple();
    }

    /**
     * Lista todos os blocos cadastrados no banco de dados de forma completa.
     * @return lista com todos os blocos encontrados, caso exista.
     */
    @Override
    public List<SectorCompoundResponse> findAllSectorCompound(){
        return findAllCompound.findAllCompound();
    }

    /**
     * Busca um bloco no banco de dados pelo ID informado de forma simples.
     * @param id identificador do bloco.
     * @return bloco encontrado, caso exista.
     */
    @Override
    public SectorSimpleResponse findSectorByIdSimple(Long id){
        return findByIdSimple.findByIdSimple(id);
    }

    /**
     * Busca um bloco no banco de dados pelo ID informado de forma completa.
     * @param id identificador do bloco.
     * @return bloco encontrado, caso exista.
     */
    @Override
    public SectorCompoundResponse findSectorByIdCompound(Long id){
        return findByIdCompound.findSectorByIdCompound(id);
    }

    /**
     * Atualiza um bloco existente no banco de dados.
     * @param id identificador do bloco.
     * @param sector novos dados do bloco.
     * @return bloco já atualizado.
     */
    @Override
    public SectorSimpleResponse updateSector(Long id, SectorRequest sector){
        return update.updateSector(id, sector);
    }

    /**
     * Remove um bloco do banco de dados.
     * @param id identificador do bloco.
     */
    @Override
    public void deleteSector(Long id){
        delete.deleteSector(id);
    }

}
