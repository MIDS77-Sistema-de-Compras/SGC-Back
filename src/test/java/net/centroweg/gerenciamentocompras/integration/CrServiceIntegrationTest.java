package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface.CrService;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CrServiceIntegrationTest {

    @Autowired
    private CrService crService;

    @Autowired
    private CrRepository crRepository;

    @Autowired
    private SectorRepository sectorRepository;

    private UserPrincipal coordenador;

    @BeforeEach
    void setUp() {
        crRepository.deleteAll();
        sectorRepository.deleteAll();
        sectorRepository.save(new Sector("Setor Teste"));

        Role role = new Role("COORDENADOR");
        User user = new User();
        user.setRole(role);
        coordenador = new UserPrincipal(user);
    }

    @Test
    void shouldCreateCr() {
        CrRequest request = new CrRequest("CR Compras", "1001L", true, "Setor Teste");

        CrCompoundResponse response = crService.create(request, coordenador);

        assertThat(response.id()).isPositive();
        assertThat(response.name()).isEqualTo("CR Compras");
        assertThat(response.code()).isEqualTo("1001L");
        assertThat(response.master()).isTrue();
        assertThat(crRepository.existsById(response.id())).isTrue();
    }

    @Test
    void shouldListAllCrs() {
        CrCompoundResponse firstCr = crService.create(new CrRequest("CR Compras", "1001L", true, "Setor Teste"), coordenador);
        CrCompoundResponse secondCr = crService.create(new CrRequest("CR Engenharia", "1002L", false, "Setor Teste"), coordenador);

        List<CrCompoundResponse> responses = crService.listAll();

        assertThat(responses)
                .hasSize(2)
                .extracting(CrCompoundResponse::id)
                .containsExactlyInAnyOrder(firstCr.id(), secondCr.id());
    }

    @Test
    void shouldFindCrById() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", true, "Setor Teste"), coordenador);

        CrCompoundResponse response = crService.listById(createdCr.id());

        assertThat(response.id()).isEqualTo(createdCr.id());
        assertThat(response.name()).isEqualTo("CR Compras");
        assertThat(response.code()).isEqualTo("1001L");
        assertThat(response.master()).isTrue();
    }

    @Test
    void shouldUpdateCr() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", true, "Setor Teste"), coordenador);
        CrRequest updateRequest = new CrRequest("CR Financeiro", "2002L", false, null);

        CrCompoundResponse response = crService.update(createdCr.id(), updateRequest);

        assertThat(response.id()).isEqualTo(createdCr.id());
        assertThat(response.name()).isEqualTo("CR Financeiro");
        assertThat(response.code()).isEqualTo("2002L");
        assertThat(response.master()).isFalse();

        CrCompoundResponse persistedCr = crService.listById(createdCr.id());
        assertThat(persistedCr.name()).isEqualTo("CR Financeiro");
        assertThat(persistedCr.code()).isEqualTo("2002L");
        assertThat(persistedCr.master()).isFalse();
    }

    @Test
    void shouldDeleteCr() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", true, "Setor Teste"), coordenador);

        MessageDTO response = crService.delete(createdCr.id());

        assertThat(response.text()).isEqualTo("CR Deletado com sucesso!");
        assertThat(crRepository.existsById(createdCr.id())).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenCrDoesNotExist() {
        assertThatThrownBy(() -> crService.listById(999L))
                .isInstanceOf(CrNotFoundException.class)
                .hasMessage("CR com id " + 999L + " não encontrado");
    }
}
