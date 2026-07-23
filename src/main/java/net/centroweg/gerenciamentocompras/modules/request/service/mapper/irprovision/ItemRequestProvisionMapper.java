package net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision;

import java.util.List;
import org.springframework.stereotype.Component;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;

/**
 * Componente responsável pela conversão entre a entidade({@link ItemRequestProvision}) e seus DTOs de entrada({@link ItemRequestProvisionRequest}) e saída({@link ItemRequestProvisionResponse}).
 */
@Component
public class ItemRequestProvisionMapper {

    /**
     * Converte um DTO de entrada do item de provisão da solicitação em uma entidade item de provisão da solicitação.
     * @param requestDto dados do item de provisão da solicitação.
     * @param request dados da solicitação.
     * @param provision dados da provisão.
     * @param status dados do status.
     * @return dados convertidos para entidade.
     */
    public ItemRequestProvision toEntity(ItemRequestProvisionRequest requestDto, Request request, Provision provision, Status status){
        return new ItemRequestProvision(request, provision, status, requestDto.additionalInformation());
    }

    /**
     * Converte uma entidade item de provisão da solicitação em um DTO de saída do item de provisão da solicitação.
     * @param itemRequestProvision entidade com os dados do item de provisão da solicitação.
     * @return dados convertidos para DTO de saída.
     */
    public ItemRequestProvisionResponse toResponse(ItemRequestProvision itemRequestProvision){
        return new ItemRequestProvisionResponse(
                itemRequestProvision.getId(),
                itemRequestProvision.getRequest().getId(),
                itemRequestProvision.getProvision().getId(),
                itemRequestProvision.getStatus().getName(),
                itemRequestProvision.getAdditionalInformation()
        );
    }

    /**
     * Converte uma lista de entidades item de provisão da solicitação em uma lista de DTOs de saída do item de provisão da solicitação.
     * @param itemRequestProvisions lista de entidades com os dados do item de provisão da solicitação.
     * @return dados convertido para uma lista de DTOs de saída.
     */
    public List<ItemRequestProvisionResponse> toResponseList(List<ItemRequestProvision> itemRequestProvisions){
        return itemRequestProvisions.stream().map(item -> new ItemRequestProvisionMapper().toResponse(item)).toList();
    }

}
