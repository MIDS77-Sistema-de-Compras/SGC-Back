package net.centroweg.gerenciamentocompras.modules.provision.service.interfaces;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

public interface ProvisionService {
    ProvisionResponse createProvision(ProvisionRequest request);
    List<ProvisionResponse> getAllProvisions();
    ProvisionResponse getProvisionById(Long id);
    ProvisionResponse updateProvision(Long id, ProvisionRequest request);
    void deleteProvision(Long id);
}
