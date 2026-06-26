package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCrBranchTest {

    @Mock
    private CrBranchRepository crBranchRepository;

    @Mock
    private CrBranchMapper crBranchMapper;

    @InjectMocks
    private FindAllCrBranch findAllCrBranch;

    @Test
    @DisplayName("Deve enviar Specification ao repository e mapear os resultados")
    void shouldSendSpecificationToRepositoryAndMapResults() {
        CrBranch firstCrBranch = new CrBranch();
        CrBranch secondCrBranch = new CrBranch();
        CrBranchResponse firstResponse = new CrBranchResponse(1L, "Filial Centro", "Compras", "123456", List.of("Ana Silva"));
        CrBranchResponse secondResponse = new CrBranchResponse(2L, "Filial Norte", "Engenharia", "987654", List.of());

        when(crBranchRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(firstCrBranch, secondCrBranch));
        when(crBranchMapper.toResponse(firstCrBranch)).thenReturn(firstResponse);
        when(crBranchMapper.toResponse(secondCrBranch)).thenReturn(secondResponse);

        List<CrBranchResponse> responses = findAllCrBranch.findAll(
                new CrBranchFilterRequest("123", "compras", List.of("ana"))
        );

        ArgumentCaptor<Specification<CrBranch>> specificationCaptor =
                ArgumentCaptor.forClass(Specification.class);
        verify(crBranchRepository, times(1)).findAll(specificationCaptor.capture());
        verify(crBranchMapper, times(1)).toResponse(firstCrBranch);
        verify(crBranchMapper, times(1)).toResponse(secondCrBranch);

        assertNotNull(specificationCaptor.getValue());
        assertEquals(List.of(firstResponse, secondResponse), responses);
    }

    @Test
    @DisplayName("Deve aceitar filtros vazios ou nulos sem erro")
    void shouldAcceptEmptyOrNullFilters() {
        when(crBranchRepository.findAll(any(Specification.class)))
                .thenReturn(List.of());

        List<CrBranchResponse> emptyFieldsResponse = findAllCrBranch.findAll(
                new CrBranchFilterRequest(null, "", List.of(" "))
        );
        List<CrBranchResponse> nullFilterResponse = findAllCrBranch.findAll(null);

        verify(crBranchRepository, times(2)).findAll(any(Specification.class));
        assertNotNull(emptyFieldsResponse);
        assertNotNull(nullFilterResponse);
        assertEquals(0, emptyFieldsResponse.size());
        assertEquals(0, nullFilterResponse.size());
    }
}
