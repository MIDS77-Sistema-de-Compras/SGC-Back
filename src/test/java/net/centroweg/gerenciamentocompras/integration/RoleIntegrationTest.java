package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.RoleIntrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // usa application-test.properties com H2
@Transactional          // reverte o banco após cada teste
class RoleIntegrationTest {

    @Autowired
    private RoleIntrf roleService;

    @Autowired
    private RoleRepository roleRepository;

    // Limpa o banco antes de cada teste para garantir isolamento
    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("[Integração] Deve criar e persistir uma role no banco")
    void shouldCreateAndPersistRole() {
        CreateRole request = new CreateRole("ADMIN");

        RoleResponse result = roleService.createRole(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("ADMIN", result.name());

        // Confirma que realmente foi ao banco
        assertTrue(roleRepository.findById(result.id()).isPresent());
    }

    @Test
    @DisplayName("[Integração] Deve listar todas as roles persistidas")
    void shouldListAllPersistedRoles() {
        roleService.createRole(new CreateRole("ADMIN"));
        roleService.createRole(new CreateRole("USER"));
        roleService.createRole(new CreateRole("MANAGER"));

        List<RoleResponse> result = roleService.listRole();

        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("[Integração] Deve buscar role por ID existente")
    void shouldFindRoleByExistingId() {
        RoleResponse created = roleService.createRole(new CreateRole("ADMIN"));

        RoleResponse found = roleService.findRoleById(created.id());

        assertNotNull(found);
        assertEquals("ADMIN", found.name());
        assertEquals(created.id(), found.id());
    }

    @Test
    @DisplayName("[Integração] Deve lançar RoleNotFoundException ao buscar ID inexistente")
    void shouldThrowExceptionWhenFindingNonExistentId() {
        assertThrows(RoleNotFoundException.class, () ->
                roleService.findRoleById(9999L)
        );
    }

    @Test
    @DisplayName("[Integração] Deve buscar role por nome ignorando maiúsculas/minúsculas")
    void shouldFindRoleByNameIgnoringCase() {
        roleService.createRole(new CreateRole("ADMIN"));

        RoleResponse resultLower = roleService.findRoleByName("admin");
        RoleResponse resultUpper = roleService.findRoleByName("ADMIN");
        RoleResponse resultMixed = roleService.findRoleByName("Admin");

        assertNotNull(resultLower);
        assertNotNull(resultUpper);
        assertNotNull(resultMixed);
        assertEquals("ADMIN", resultLower.name());
        assertEquals("ADMIN", resultUpper.name());
        assertEquals("ADMIN", resultMixed.name());
    }

    @Test
    @DisplayName("[Integração] Deve lançar RoleNotFoundException ao buscar nome inexistente")
    void shouldThrowExceptionWhenFindingNonExistentName() {
        assertThrows(RoleNotFoundException.class, () ->
                roleService.findRoleByName("INEXISTENTE")
        );
    }

    @Test
    @DisplayName("[Integração] Deve atualizar o nome de uma role existente")
    void shouldUpdateExistingRole() {
        RoleResponse created = roleService.createRole(new CreateRole("ADMIN"));

        RoleResponse updated = roleService.updateRole(created.id(), new CreateRole("SUPER_ADMIN"));

        assertNotNull(updated);
        assertEquals("SUPER_ADMIN", updated.name());
        assertEquals(created.id(), updated.id());

        // Confirma que foi persistido no banco
        Role inDb = roleRepository.findById(created.id()).orElseThrow();
        assertEquals("SUPER_ADMIN", inDb.getName());
    }

    @Test
    @DisplayName("[Integração] Deve lançar RoleNotFoundException ao atualizar ID inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentRole() {
        assertThrows(RoleNotFoundException.class, () ->
                roleService.updateRole(9999L, new CreateRole("QUALQUER"))
        );
    }

    @Test
    @DisplayName("[Integração] Deve deletar uma role existente")
    void shouldDeleteExistingRole() {
        RoleResponse created = roleService.createRole(new CreateRole("TEMP"));

        roleService.deleteRole(created.id());

        assertFalse(roleRepository.findById(created.id()).isPresent());
    }
}