package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RejectRequestRequest(

                @NotNull(message = "O id da solicitação é obrigatório.")
                        Long requestId,

                        @NotBlank(message = "O motivo da reprovação é obrigatório.")
                        @Size(min = 10, max = 255, message = "O motivo deve ter entre 10 e 255 caracteres.")
                        String reason

) {}
