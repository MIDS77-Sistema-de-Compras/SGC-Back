package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;


import jakarta.validation.constraints.NotNull;

public record ApproveRequestRequest(

                @NotNull(message = "O id da solicitação é obrigatório.")
                        Long requestId

) {}