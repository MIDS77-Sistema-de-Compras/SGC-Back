package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllRequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private FindAllRequestServiceImpl findAllRequestService;

    @Captor
    private ArgumentCaptor<Specification<Request>> specificationCaptor;

    @Test
    @DisplayName("Deve buscar solicitações usando Specification e mapear o resultado")
    void shouldFindAllUsingSpecificationAndMapResults() {
        Request firstRequest = new Request();
        Request secondRequest = new Request();

        RequestResponse firstResponse = buildResponse(1L, "APROVADO");
        RequestResponse secondResponse = buildResponse(2L, "EM_ANDAMENTO");

        RequestFilterRequest filter = new RequestFilterRequest(
                "79",
                "APROVADO",
                "joao",
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 30)
        );

        Pageable pageable = Pageable.unpaged();
        Page<Request> requestPage = new PageImpl<>(List.of(firstRequest, secondRequest));

        when(requestRepository.findAll(anySpecification(), any(Pageable.class))).thenReturn(requestPage);
        when(requestMapper.toDTO(firstRequest)).thenReturn(firstResponse);
        when(requestMapper.toDTO(secondRequest)).thenReturn(secondResponse);

        Page<RequestResponse> response = findAllRequestService.findAllRequest(filter, pageable);

        assertNotNull(response);
        assertEquals(2, response.getTotalElements());

        verify(requestRepository).findAll(specificationCaptor.capture(), any(Pageable.class));
        assertNotNull(specificationCaptor.getValue());

        verify(requestMapper).toDTO(firstRequest);
        verify(requestMapper).toDTO(secondRequest);
        verify(requestRepository, never()).findAll();
        verifyNoMoreInteractions(requestRepository, requestMapper);
    }

    @Test
    @DisplayName("Deve aceitar filtros nulos e continuar usando Specification")
    void shouldAcceptNullFiltersAndKeepUsingSpecification() {
        RequestFilterRequest filter = new RequestFilterRequest(null, null, null, null, null);
        Pageable pageable = Pageable.unpaged();

        when(requestRepository.findAll(anySpecification(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        Page<RequestResponse> response = assertDoesNotThrow(
                () -> findAllRequestService.findAllRequest(filter, pageable)
        );

        assertTrue(response.isEmpty());

        verify(requestRepository).findAll(specificationCaptor.capture(), any(Pageable.class));
        assertNotNull(specificationCaptor.getValue());
        verify(requestRepository, never()).findAll();
        verifyNoMoreInteractions(requestRepository, requestMapper);
    }

    private RequestResponse buildResponse(Long id, String statusName) {
        LocalDateTime dateTime = LocalDateTime.of(2026, 6, 15, 10, 0);

        return new RequestResponse(
                id,
                dateTime,
                dateTime,
                id,
                statusName,
                null,
                null,
                null,
                null
        );
    }

    private Specification<Request> anySpecification() {
        return any();
    }
}
