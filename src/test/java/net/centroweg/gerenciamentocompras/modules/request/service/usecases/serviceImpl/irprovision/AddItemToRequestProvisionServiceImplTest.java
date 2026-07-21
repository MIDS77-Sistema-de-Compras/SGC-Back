package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.irprovision;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.service.api.ProvisionPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddItemToRequestProvisionServiceImplTest {

    @Mock
    private ItemRequestProvisionMapper itemRequestProvisionMapper;

    @Mock
    private ItemRequestProvisionRepository itemRequestProvisionRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ProvisionPublicApi provisionPublicApi;

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private AddItemToRequestProvisionServiceImpl addItemToRequestProvisionService;

    @Test
    void mustNotAddTheSameProvisionTwiceInTheRequest() {
        Request request = new Request();
        request.setId(10L);
        Provision provision = new Provision("Service", 100.0, "valid description");
        provision.setId(20L);
        ItemRequestProvisionRequest dto = new ItemRequestProvisionRequest(10L, 20L, 1L, "details");

        when(requestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(provisionPublicApi.findById(20L)).thenReturn(Optional.of(provision));
        when(itemRequestProvisionRepository.existsByRequestIdAndProvisionId(10L, 20L)).thenReturn(true);

        assertThrows(net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProvisionAlreadyExistsException.class,
                () -> addItemToRequestProvisionService.addItem(dto));

        org.mockito.Mockito.verify(itemRequestProvisionRepository, never()).save(any());
    }
}
