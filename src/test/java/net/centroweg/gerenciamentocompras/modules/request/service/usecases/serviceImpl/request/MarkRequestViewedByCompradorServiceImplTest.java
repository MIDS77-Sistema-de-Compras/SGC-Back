package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotApprovedForCompradorException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.CompradorRequestAccessValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkRequestViewedByCompradorServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private CompradorRequestAccessValidator compradorRequestAccessValidator;

    @InjectMocks
    private MarkRequestViewedByCompradorServiceImpl service;

    @Test
    @DisplayName("Deve marcar como visualizado quando ainda nao foi visto")
    void deveMarcarComoVisualizadoQuandoAindaNaoFoiVisto() {
        Request request = new Request();
        request.setId(1L);
        request.setViewedByComprador(false);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toDTO(request)).thenReturn(mock(RequestResponse.class));

        service.markViewed(1L);

        assertTrue(request.getViewedByComprador());
        verify(requestRepository).save(request);
    }

    @Test
    @DisplayName("Deve ser idempotente e nao regravar quando ja foi visto")
    void deveSerIdempotenteQuandoJaFoiVisto() {
        Request request = new Request();
        request.setId(1L);
        request.setViewedByComprador(true);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestMapper.toDTO(request)).thenReturn(mock(RequestResponse.class));

        service.markViewed(1L);

        verify(requestRepository, never()).save(request);
    }

    @Test
    @DisplayName("Deve lancar RequestNotFoundException quando id nao existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> service.markViewed(1L));
    }

    @Test
    @DisplayName("Deve lancar RequestNotApprovedForCompradorException quando status nao permitido")
    void deveLancarExcecaoQuandoStatusNaoPermitido() {
        Request request = new Request();
        request.setId(1L);
        request.setViewedByComprador(false);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        doThrow(new RequestNotApprovedForCompradorException())
                .when(compradorRequestAccessValidator).validate(request);

        assertThrows(RequestNotApprovedForCompradorException.class, () -> service.markViewed(1L));
    }
}
