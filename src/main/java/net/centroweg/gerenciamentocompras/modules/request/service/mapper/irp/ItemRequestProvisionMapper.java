package net.centroweg.gerenciamentocompras.modules.request.service.mapper.irp;

import java.util.List;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;

@Component
public class ItemRequestProvisionMapper {
    
    public ItemRequestProvision toEntity(ItemRequestProvisionRequest requestDto, Request request, Provision provision, Status status){
        return new ItemRequestProvision(request, provision, status, requestDto.additionalInformation());
    }

    public ItemRequestProvisionResponse toResponse(ItemRequestProvision itemRequestProvision){
        return new ItemRequestProvisionResponse(
                itemRequestProvision.getId(), 
                itemRequestProvision.getRequest(), 
                itemRequestProvision.getProvision(), 
                itemRequestProvision.getStatus(), 
                itemRequestProvision.getAdditionalInformation());
    }

    public List<ItemRequestProvisionResponse> toResponseList(List<ItemRequestProvision> itemRequestProvisions){
        return itemRequestProvisions.stream().map(item -> new ItemRequestProvisionMapper().toResponse(item)).toList();
    }

}
