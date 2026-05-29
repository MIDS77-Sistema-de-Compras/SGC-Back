package net.centroweg.gerenciamentocompras.modules.provision.service.interfaces;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.AddProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.DeleteProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.GetProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.service.UpdateProvisionService;
import org.springframework.stereotype.Service;

/**
 * Classe responsável por implementar os métodos da interface {@code ProvisionService}
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionService
 * @see AddProvisionService
 * @see GetProvisionService
 * @see UpdateProvisionService
 * @see DeleteProvisionService
 */
@Service
@RequiredArgsConstructor
public class ProvisionServiceImpl implements ProvisionService {
    
    private final AddProvisionService addProvisionService;
    private final GetProvisionService getProvisionService;
    private final UpdateProvisionService updateProvisionService;
    private final DeleteProvisionService deleteProvisionService;

    @Override
    public ProvisionResponse createProvision(ProvisionRequest request) {
        return addProvisionService.saveNewProvision(request);
    }

    @Override
    public List<ProvisionResponse> getAllProvisions() {
        return getProvisionService.getAllProvisions();
    }

    @Override
    public ProvisionResponse getProvisionById(Long id) {
        return getProvisionService.getProvisionById(id);
    }

    @Override
    public ProvisionResponse updateProvision(Long id, ProvisionRequest request) {
        return updateProvisionService.updateProvision(id, request);
    }

    @Override
    public void deleteProvision(Long id) {
        deleteProvisionService.deleteProvisionById(id);
    }

}
