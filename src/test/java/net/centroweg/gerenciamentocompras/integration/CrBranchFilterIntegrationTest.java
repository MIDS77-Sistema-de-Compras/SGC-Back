package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class CrBranchFilterIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CrBranchRepository crBranchRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CrRepository crRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private CrBranch matchingCrBranch;
    private CrBranch nonMatchingCrBranch;
    private CrBranch withoutResponsibleCrBranch;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        crBranchRepository.deleteAll();
        userRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Branch anotherBranch = branchRepository.save(new Branch("Filial Norte"));

        Cr matchingCr = crRepository.save(new Cr("CR Compras Digitais", "123456", false, null));
        Cr nonMatchingCr = crRepository.save(new Cr("CR Engenharia", "987654", false, null));
        Cr withoutResponsibleCr = crRepository.save(new Cr("CR Sem Responsavel", "555000", false, null));

        User ana = userRepository.save(createUser("Ana Silva", "11111111111", "ana.silva@centroweg.com.br"));
        User bruno = userRepository.save(createUser("Bruno Costa", "22222222222", "bruno.costa@centroweg.com.br"));

        matchingCrBranch = crBranchRepository.save(new CrBranch(branch, matchingCr, List.of(ana)));
        nonMatchingCrBranch = crBranchRepository.save(new CrBranch(anotherBranch, nonMatchingCr, List.of(bruno)));
        withoutResponsibleCrBranch = crBranchRepository.save(new CrBranch(branch, withoutResponsibleCr, null));
    }

    @Test
    void withoutParametersShouldReturnAll() throws Exception {
        mockMvc.perform(get("/cr-branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldFilterOnlyByCrCode() throws Exception {
        mockMvc.perform(get("/cr-branches").param("crCode", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()))
                .andExpect(jsonPath("$[0].crCode").value("123456"));
    }

    @Test
    void shouldFilterByPartialCrCode() throws Exception {
        mockMvc.perform(get("/cr-branches").param("crCode", "345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()))
                .andExpect(jsonPath("$[0].crCode").value("123456"));
    }

    @Test
    void shouldFilterOnlyByCrName() throws Exception {
        mockMvc.perform(get("/cr-branches").param("crName", "Compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()))
                .andExpect(jsonPath("$[0].crName").value("CR Compras Digitais"));
    }

    @Test
    void shouldFilterCrNameIgnoringCase() throws Exception {
        mockMvc.perform(get("/cr-branches").param("crName", "compras digitais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()));
    }

    @Test
    void shouldFilterOnlyByResponsibleName() throws Exception {
        mockMvc.perform(get("/cr-branches").param("responsibleName", "Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()))
                .andExpect(jsonPath("$[0].responsibleUsersName[0]").value("Ana Silva"));
    }

    @Test
    void shouldFilterResponsibleNameIgnoringCase() throws Exception {
        mockMvc.perform(get("/cr-branches").param("responsibleName", "ana silva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()));
    }

    @Test
    void shouldCombineAllFiltersUsingAnd() throws Exception {
        mockMvc.perform(get("/cr-branches")
                        .param("crCode", "123")
                        .param("crName", "Compras")
                        .param("responsibleName", "Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(matchingCrBranch.getId()));
    }

    @Test
    void unmatchedFiltersShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/cr-branches")
                        .param("crCode", "123")
                        .param("crName", "Engenharia")
                        .param("responsibleName", "Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void crBranchWithoutResponsibleShouldNotCauseError() throws Exception {
        mockMvc.perform(get("/cr-branches").param("crCode", "555000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(withoutResponsibleCrBranch.getId()))
                .andExpect(jsonPath("$[0].responsibleUsersName").isEmpty());
    }

    private User createUser(String name, String cpf, String email) {
        User user = new User();
        user.setName(name);
        user.setCpf(cpf);
        user.setEmail(email);
        user.setPassword("Senha@123");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
