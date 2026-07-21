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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateProvisionServiceTest {

    @Mock
    private ProvisionRepository provisionRepository;

    @Mock
    private ProvisionMapper provisionMapper;

    @InjectMocks
    private UpdateProvisionService updateProvisionService;

    @Test
    void mustUpdateProvisionWithNormalizedName() {
        Provision provision = new Provision("Old service", 100.0, "old description");
        provision.setId(1L);
        ProvisionRequest request = new ProvisionRequest("  New   service  ", 200.0, "new description");
        ProvisionResponse expected = new ProvisionResponse(1L, "New service", 200.0, "new description");

        when(provisionRepository.findById(1L)).thenReturn(Optional.of(provision));
        when(provisionRepository.existsByNameIgnoreCaseAndIdNot("New service", 1L)).thenReturn(false);
        when(provisionRepository.save(provision)).thenReturn(provision);
        when(provisionMapper.toResponse(provision)).thenReturn(expected);

        ProvisionResponse response = updateProvisionService.updateProvision(1L, request);

        assertEquals("New service", provision.getName());
        assertEquals(expected, response);
    }

    @Test
    void mustNotUpdateProvisionWithNameFromAnotherProvision() {
        Provision provision = new Provision("Old service", 100.0, "old description");
        provision.setId(1L);
        ProvisionRequest request = new ProvisionRequest("Existing service", 200.0, "new description");

        when(provisionRepository.findById(1L)).thenReturn(Optional.of(provision));
        when(provisionRepository.existsByNameIgnoreCaseAndIdNot("Existing service", 1L)).thenReturn(true);

        assertThrows(ProvisionAlreadyExistsException.class, () -> updateProvisionService.updateProvision(1L, request));

        verify(provisionRepository, never()).save(any());
    }
}
