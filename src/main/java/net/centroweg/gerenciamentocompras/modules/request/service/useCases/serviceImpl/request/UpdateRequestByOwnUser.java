package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestAlreadyApprovedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateRequestByOwnUser {

    private final RequestRepository requestRepository;
    private final CrBranchRepository crBranchRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    public RequestResponse updateRequest(RequestRequest requestDTO, Long id, UserPrincipal userPrincipal){
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        boolean isOwner = request.getCreatedByUsers().stream()
                .anyMatch(user -> user.getEmail().equals(userPrincipal.getUsername()));

        if (!isOwner) {
            throw new AcessDeniedException();
        }

        if (request.getStatus().getName().equalsIgnoreCase("Aprovado")) {
            throw new RequestAlreadyApprovedException();
        }

        CrBranch crBranch = crBranchRepository.findById(requestDTO.crBranchId())
                .orElseThrow(() -> new CrBranchNotFoundException(requestDTO.crBranchId()));

        User requester = request.getCreatedByUsers().stream()
                .filter(user -> user.getEmail().equals(userPrincipal.getUsername()))
                .findFirst()
                .orElseThrow(AcessDeniedException::new);

        List<User> assignedUsers = new ArrayList<>();
        assignedUsers.add(requester);
        requestDTO.userIds().forEach(userId ->
                userRepository.findById(userId).ifPresent(assignedUsers::add)
        );

        request.setCrBranch(crBranch);
        request.setCreatedByUsers(assignedUsers);
        request.setUpdatedAt(LocalDateTime.now());

        return requestMapper.toDTO(requestRepository.save(request));
    }
}