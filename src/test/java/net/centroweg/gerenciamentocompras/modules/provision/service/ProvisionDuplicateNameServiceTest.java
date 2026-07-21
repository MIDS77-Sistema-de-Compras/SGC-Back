package net.centroweg.gerenciamentocompras.modules.provision.service;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProvisionDuplicateNameServiceTest {

    @Mock private ProvisionRepository provisionRepository;
    @Mock private ProvisionMapper provisionMapper;

    @Test
    void shouldNormalizeNameBeforeCreatingProvision() {
        AddProvisionService service = new AddProvisionService(provisionRepository, provisionMapper);
        ProvisionRequest request = new ProvisionRequest("  Serviço   Elétrico  ", 100.0, "Descrição");
        Provision provision = new Provision("Serviço Elétrico", 100.0, "Descrição");
        ProvisionResponse response = new ProvisionResponse(1L, "Serviço Elétrico", 100.0, "Descrição");
        when(provisionMapper.toEntity(any())).thenReturn(provision);
        when(provisionRepository.save(provision)).thenReturn(provision);
        when(provisionMapper.toResponse(provision)).thenReturn(response);

        assertEquals(response, service.saveNewProvision(request));

        ArgumentCaptor<ProvisionRequest> normalizedRequest = ArgumentCaptor.forClass(ProvisionRequest.class);
        verify(provisionMapper).toEntity(normalizedRequest.capture());
        assertEquals("Serviço Elétrico", normalizedRequest.getValue().name());
    }

    @Test
    void shouldRejectDuplicateProvisionWithoutSaving() {
        AddProvisionService service = new AddProvisionService(provisionRepository, provisionMapper);
        ProvisionRequest request = new ProvisionRequest("  servico   novo ", 100.0, "Descrição");
        when(provisionRepository.existsByNameIgnoreCase("servico novo")).thenReturn(true);

        assertThrows(ProvisionAlreadyExistsException.class, () -> service.saveNewProvision(request));

        verify(provisionRepository, never()).save(any());
    }

    @Test
    void shouldAllowKeepingTheProvisionOwnNameOnUpdate() {
        UpdateProvisionService service = new UpdateProvisionService(provisionRepository, provisionMapper);
        Provision provision = new Provision("Serviço Elétrico", 100.0, "Descrição");
        provision.setId(1L);
        ProvisionRequest request = new ProvisionRequest("  serviço   elétrico  ", 150.0, "Nova descrição");
        when(provisionRepository.findById(1L)).thenReturn(Optional.of(provision));
        when(provisionRepository.save(provision)).thenReturn(provision);
        when(provisionMapper.toResponse(provision)).thenReturn(
                new ProvisionResponse(1L, "serviço elétrico", 150.0, "Nova descrição")
        );

        service.updateProvision(1L, request);

        verify(provisionRepository).existsByNameIgnoreCaseAndIdNot("serviço elétrico", 1L);
        assertEquals("serviço elétrico", provision.getName());
    }

    @Test
    void shouldRejectUpdateToAnotherProvisionNameWithoutSaving() {
        UpdateProvisionService service = new UpdateProvisionService(provisionRepository, provisionMapper);
        Provision provision = new Provision("Serviço A", 100.0, "Descrição");
        provision.setId(1L);
        ProvisionRequest request = new ProvisionRequest("serviço b", 100.0, "Descrição");
        when(provisionRepository.findById(1L)).thenReturn(Optional.of(provision));
        when(provisionRepository.existsByNameIgnoreCaseAndIdNot("serviço b", 1L)).thenReturn(true);

        assertThrows(ProvisionAlreadyExistsException.class, () -> service.updateProvision(1L, request));

        verify(provisionRepository, never()).save(any());
    }
}
