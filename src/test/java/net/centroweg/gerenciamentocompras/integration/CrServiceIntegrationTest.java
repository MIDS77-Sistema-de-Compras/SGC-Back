package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrEditNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface.CrService;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private CrBranchRepository crBranchRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private UserPrincipal coordenador;

    @BeforeEach
    void setUp() {
        crBranchRepository.deleteAll();
        crRepository.deleteAll();
        sectorRepository.deleteAll();
        sectorRepository.save(new Sector("Setor Teste"));

        Role role = new Role("COORDENADOR");
        User user = new User();
        user.setRole(role);
        coordenador = new UserPrincipal(user);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Persiste um coordenador responsável por um CR Master e o autentica,
     * atendendo à regra de que só o coordenador do CR Master edita CRs.
     */
    private User authenticateAsMasterCoordinator(Long masterCrId) {
        Role role = roleRepository.save(new Role("COORDENADOR"));
        User user = new User("Coordenador Master", "52998224725", "coordenador.master@teste.com", "Senha@123", "1234", true);
        user.setRole(role);
        user = userRepository.save(user);

        Branch branch = branchRepository.save(new Branch("Filial Teste"));
        crBranchRepository.save(new CrBranch(branch,
                crRepository.findById(masterCrId).orElseThrow(),
                List.of(user)));

        UserPrincipal principal = new UserPrincipal(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));

        return user;
    }

    @Test
    void shouldCreateCr() {
        CrRequest request = new CrRequest("CR Compras", "1001L", "descrição teste",true, "Setor Teste");

        CrCompoundResponse response = crService.create(request, coordenador);

        assertThat(response.id()).isPositive();
        assertThat(response.name()).isEqualTo("CR Compras");
        assertThat(response.code()).isEqualTo("1001L");
        assertThat(response.master()).isTrue();
        assertThat(crRepository.existsById(response.id())).isTrue();
    }

    @Test
    void shouldListAllCrs() {
        CrCompoundResponse firstCr = crService.create(new CrRequest("CR Compras", "1001L", "descrição teste", true, "Setor Teste"), coordenador);
        CrCompoundResponse secondCr = crService.create(new CrRequest("CR Engenharia", "1002L", "descrição teste",false, "Setor Teste"), coordenador);

        List<CrCompoundResponse> responses = crService.listAll();

        assertThat(responses)
                .hasSize(2)
                .extracting(CrCompoundResponse::id)
                .containsExactlyInAnyOrder(firstCr.id(), secondCr.id());
    }

    @Test
    void shouldFindCrById() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", "descrição teste", true, "Setor Teste"), coordenador);

        CrCompoundResponse response = crService.listById(createdCr.id());

        assertThat(response.id()).isEqualTo(createdCr.id());
        assertThat(response.name()).isEqualTo("CR Compras");
        assertThat(response.code()).isEqualTo("1001L");
        assertThat(response.master()).isTrue();
    }

    @Test
    void shouldUpdateCr() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", "descrição teste", true, "Setor Teste"), coordenador);
        authenticateAsMasterCoordinator(createdCr.id());
        CrRequest updateRequest = new CrRequest("CR Financeiro", "2002L","descrição teste", false, null);

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
    void shouldRejectUpdateWhenUserIsNotMasterCoordinator() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", "descrição teste", false, "Setor Teste"), coordenador);

        Role role = roleRepository.save(new Role("COORDENADOR"));
        User user = new User("Coordenador Comum", "11144477735", "coordenador.comum@teste.com", "Senha@123", "1234", true);
        user.setRole(role);
        user = userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));

        CrRequest updateRequest = new CrRequest("CR Financeiro", "2002L", "descrição teste", false, null);

        assertThatThrownBy(() -> crService.update(createdCr.id(), updateRequest))
                .isInstanceOf(CrEditNotAllowedException.class);
    }

    @Test
    void shouldDeleteCr() {
        CrCompoundResponse createdCr = crService.create(new CrRequest("CR Compras", "1001L", "descrição teste",true, "Setor Teste"), coordenador);

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
