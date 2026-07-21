package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige vínculos legados que mantêm supervisores ou usuários inativos em CRs Master.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RepairCrMasterResponsibles implements ApplicationRunner {

    private final CrBranchRepository crBranchRepository;
    private final ValidateCrBranchResponsibles validator;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<CrBranch> changedBranches = new ArrayList<>();
        int removedResponsibles = 0;

        for (CrBranch crBranch : crBranchRepository.findAllMasterWithResponsibles()) {
            if (crBranch.getResponsibleUsers() == null) {
                continue;
            }

            int previousSize = crBranch.getResponsibleUsers().size();
            crBranch.getResponsibleUsers().removeIf(
                    responsible -> !validator.isEligibleMasterResponsible(responsible)
            );

            int removedFromBranch = previousSize - crBranch.getResponsibleUsers().size();
            if (removedFromBranch > 0) {
                removedResponsibles += removedFromBranch;
                changedBranches.add(crBranch);
            }
        }

        if (!changedBranches.isEmpty()) {
            crBranchRepository.saveAll(changedBranches);
            log.warn(
                    "Removidos {} responsáveis inválidos de {} vínculos de CR Master.",
                    removedResponsibles,
                    changedBranches.size()
            );
        }
    }
}
