package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapperImpl;
import net.centroweg.gerenciamentocompras.shared.persistence.UniqueConstraintViolationDetector;
import net.centroweg.gerenciamentocompras.shared.util.NameNormalizer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por adicionar um serviço ao banco de dados.
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionRepository
 * @see ProvisionMapperImpl
 */
@Service
@RequiredArgsConstructor
public class AddProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Método responsável por salvar a entidade {@code Provision} fornecida.
     * @param request A requisição do usuário.
     * @return ProvisionResponse A entidade persistida no banco de dados, como DTO de resposta.
     * @see ProvisionMapper#toResponse(Provision)
     * @see ProvisionMapper#toEntity(ProvisionRequest)
     */
    @Transactional
    public ProvisionResponse saveNewProvision(ProvisionRequest request){
        String normalizedName = NameNormalizer.normalize(request.name());
        if (provisionRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ProvisionAlreadyExistsException();
        }

        Provision provision = provisionMapper.toEntity(new ProvisionRequest(
                normalizedName,
                request.totalValue(),
                request.description()
        ));

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
