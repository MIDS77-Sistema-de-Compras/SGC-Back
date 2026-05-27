package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;


public record RequestItemResponse(

                Long id,
                        Long productId,
                        String productName,
                        Long measurementUnitId,
                        String measurementUnitAbbreviation,
                        Double quantity,
                        String additionalInformation

) {}
