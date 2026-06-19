package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestAttachmentRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.shared.cloudinary.CloudinaryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class RequestAttachmentIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RequestAttachmentRepository requestAttachmentRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CrBranchRepository crBranchRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CrRepository crRepository;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    private MockMvc mockMvc;
    private Request request;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        cleanDatabase();

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        CrBranch crBranch = crBranchRepository.save(new CrBranch(branch, cr, null));
        Status status = statusRepository.save(new Status("EM_ANDAMENTO", "Solicitacao aguardando aprovacao"));

        request = new Request(crBranch, status);
        request.setRequestDate(LocalDateTime.of(2026, 6, 18, 10, 0));
        request.setUpdatedAt(LocalDateTime.of(2026, 6, 18, 10, 0));
        request.setActive(true);
        request = requestRepository.save(request);
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("POST /requests/{id}/attachments deve retornar 201 e dados do anexo")
    void shouldUploadAttachmentToExistingRequest() throws Exception {
        byte[] content = "conteudo fake do pdf".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "files",
                "teste.pdf",
                "application/pdf",
                content
        );

        Map<?, ?> cloudinaryResult = Map.of(
                "secure_url", "https://res.cloudinary.com/demo/raw/upload/request-attachments/teste.pdf",
                "public_id", "request-attachments/teste_abc123",
                "resource_type", "raw"
        );

        doReturn(cloudinaryResult)
                .when(cloudinaryService)
                .uploadFile(any(MultipartFile.class));

        mockMvc.perform(multipart("/requests/{id}/attachments", request.getId())
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].originalName").value("teste.pdf"))
                .andExpect(jsonPath("$[0].url").value("https://res.cloudinary.com/demo/raw/upload/request-attachments/teste.pdf"))
                .andExpect(jsonPath("$[0].contentType").value("application/pdf"))
                .andExpect(jsonPath("$[0].size").value(content.length));
    }

    private void cleanDatabase() {
        requestAttachmentRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
    }
}
