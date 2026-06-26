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
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

        when(requestRepository.findAll(anySpecification()))
                .thenReturn(List.of(firstRequest, secondRequest));
        when(requestMapper.toDTO(firstRequest)).thenReturn(firstResponse);
        when(requestMapper.toDTO(secondRequest)).thenReturn(secondResponse);

        List<RequestResponse> response = findAllRequestService.findAllRequest(filter);

        assertEquals(List.of(firstResponse, secondResponse), response);

        verify(requestRepository).findAll(specificationCaptor.capture());
        assertNotNull(specificationCaptor.getValue());

        verify(requestMapper).toDTO(firstRequest);
        verify(requestMapper).toDTO(secondRequest);
        verify(requestRepository, never()).findAll();
        verifyNoMoreInteractions(requestRepository, requestMapper);
    }

    @Test
    @DisplayName("Deve aceitar filtros nulos e continuar usando Specification")
    void shouldAcceptNullFiltersAndKeepUsingSpecification() {
        RequestFilterRequest filter = new RequestFilterRequest(
                null,
                null,
                null,
                null,
                null
        );

        when(requestRepository.findAll(anySpecification()))
                .thenReturn(List.of());

        List<RequestResponse> response = assertDoesNotThrow(
                () -> findAllRequestService.findAllRequest(filter)
        );

        assertEquals(List.of(), response);

        verify(requestRepository).findAll(specificationCaptor.capture());
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
