package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.irprovision;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.service.api.ProvisionPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddItemToRequestProvisionServiceImplTest {

    @Mock private ItemRequestProvisionMapper mapper;
    @Mock private ItemRequestProvisionRepository itemRepository;
    @Mock private RequestRepository requestRepository;
    @Mock private ProvisionPublicApi provisionPublicApi;
    @Mock private StatusRepository statusRepository;

    @Test
    void shouldRejectProvisionAlreadyAddedToTheSameRequestBeforeSaving() {
        AddItemToRequestProvisionServiceImpl service = new AddItemToRequestProvisionServiceImpl(
                mapper, itemRepository, requestRepository, provisionPublicApi, statusRepository
        );
        Request request = new Request();
        request.setId(1L);
        Provision provision = new Provision("Instalação", 100.0, "Descrição");
        provision.setId(2L);
        ItemRequestProvisionRequest dto = new ItemRequestProvisionRequest(1L, 2L, 3L, "Obs");
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(provisionPublicApi.findById(2L)).thenReturn(Optional.of(provision));
        when(itemRepository.existsByRequestIdAndProvisionId(1L, 2L)).thenReturn(true);

        assertThrows(ItemRequestProvisionAlreadyExistsException.class, () -> service.addItem(dto));

        verify(itemRepository, never()).save(any());
        verify(statusRepository, never()).findById(any());
    }
}
