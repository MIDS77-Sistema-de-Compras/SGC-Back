package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import net.centroweg.gerenciamentocompras.shared.persistence.UniqueConstraintViolationDetector;
import net.centroweg.gerenciamentocompras.shared.util.NameNormalizer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por atualizar os serviços no banco de dados.
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionRepository
 * @see ProvisionMapper
 */
@Service
@RequiredArgsConstructor
public class UpdateProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Método responsável por atualizar a entidade {@code Provision} por ID.
     * @param id O ID da {@code Provision} desejada.
     * @param request A requisição do usuário.
     * @return ProvisionResponse A entidade atualizada no banco de dados, como DTO de resposta.
     * @throws ProvisionNotFoundException Se a {@code Provision} não for encontrada.
     */
    @Transactional
    public ProvisionResponse updateProvision(Long id, ProvisionRequest request){
        Provision provision = provisionRepository.findById(id).orElseThrow(() -> 
            new ProvisionNotFoundException()
        );

        String normalizedName = NameNormalizer.normalize(request.name());
        if (provisionRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new ProvisionAlreadyExistsException();
        }

        provision.setName(normalizedName);
        provision.setTotalValue(request.totalValue());
        provision.setDescription(request.description());

        try {
            Provision savedProvision = provisionRepository.save(provision);
            provisionRepository.flush();
            return provisionMapper.toResponse(savedProvision);
        } catch (DataIntegrityViolationException exception) {
            if (UniqueConstraintViolationDetector.isConstraintViolation(exception, "ux_provision_normalized_name")) {
                throw new ProvisionAlreadyExistsException();
            }
            throw exception;
        }
    }

}
