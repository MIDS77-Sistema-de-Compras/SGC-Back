package net.centroweg.gerenciamentocompras.modules.provision.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddProvisionServiceTest {

    @Mock
    private ProvisionRepository provisionRepository;

    @Mock
    private ProvisionMapper provisionMapper;

    @InjectMocks
    private AddProvisionService addProvisionService;

    @Test
    void mustCreateProvisionWithNormalizedName() {
        ProvisionRequest request = new ProvisionRequest("  Service   cleaning  ", 100.0, "valid description");
        Provision provision = new Provision("Service cleaning", 100.0, "valid description");
        provision.setId(1L);
        ProvisionResponse expected = new ProvisionResponse(1L, "Service cleaning", 100.0, "valid description");

        when(provisionRepository.findByNameIgnoreCase("Service cleaning")).thenReturn(Optional.empty());
        when(provisionMapper.toEntity(any(ProvisionRequest.class))).thenReturn(provision);
        when(provisionRepository.save(provision)).thenReturn(provision);
        when(provisionMapper.toResponse(provision)).thenReturn(expected);

        ProvisionResponse response = addProvisionService.saveNewProvision(request);

        ArgumentCaptor<ProvisionRequest> requestCaptor = ArgumentCaptor.forClass(ProvisionRequest.class);
        verify(provisionMapper).toEntity(requestCaptor.capture());
        assertEquals("Service cleaning", requestCaptor.getValue().name());
        assertEquals(expected, response);
    }

    @Test
    void mustNotCreateDuplicateProvision() {
        ProvisionRequest request = new ProvisionRequest(" service ", 100.0, "valid description");
        when(provisionRepository.findByNameIgnoreCase("service"))
                .thenReturn(Optional.of(new Provision("Service", 100.0, "valid description")));

        assertThrows(ProvisionAlreadyExistsException.class, () -> addProvisionService.saveNewProvision(request));

        verify(provisionRepository, never()).save(any());
    }
}
