package net.centroweg.gerenciamentocompras.modules.provision.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A classe responsável por implementar os métodos da interface {@code ProvisionPublicApi},
 * encapsulando o acesso ao {@link ProvisionRepository} para os demais módulos.
 *
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionPublicApi
 */
@Repository
@RequiredArgsConstructor
public class ProvisionPublicApiImpl implements ProvisionPublicApi {

    private final ProvisionRepository provisionRepository;

    @Override
    public Optional<Provision> findById(Long id) {
        return provisionRepository.findById(id);
    }

    @Override
    public Optional<Provision> findByNameIgnoreCase(String name) {
        return provisionRepository.findFirstByNameIgnoreCase(name);
    }

    @Override
    public Provision createProvision(String name, Double totalValue, String description) {
        return provisionRepository.save(new Provision(name, totalValue, description));
    }

}
