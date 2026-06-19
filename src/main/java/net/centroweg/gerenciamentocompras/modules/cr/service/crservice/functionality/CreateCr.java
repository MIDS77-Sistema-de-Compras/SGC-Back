package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.SectorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import org.springframework.stereotype.Service;

/**
 * Responsável pela funcionalidade de criação de Centros de Responsabilidade (CR).
 */
@Service
@RequiredArgsConstructor
public class CreateCr{

    private final CrRepository crRepository;
    private final SectorRepository sectorRepository;
    private final CrMapper crMapper;

    /**
     * Realiza a criação de um Centro de Responsabilidade (CR).
     * Os dados recebidos são convertidos para entidade, persistidos
     * no banco de dados e retornados como objeto de resposta.
     *
     * @param dto Objeto que contem os dados necessarios para a criação do CR
     * @return {@link CrCompoundResponse} com os dados do CR persistido
     * */


    public CrCompoundResponse create(CrRequest dto, UserPrincipal userPrincipal){

        if(!userPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("COORDENADOR"))){
            throw new  AcessDeniedException();
        }
        Sector sectorSearched = sectorRepository.findByName(dto.sectorName())
                .orElseThrow(() -> new SectorNotFoundException());

        Cr crEntity = crMapper.toEntity(dto, sectorSearched);

        crEntity.setSector(sectorSearched);

        return crMapper.toCrCompoundResponse(crRepository.save(crEntity));
    }
}
