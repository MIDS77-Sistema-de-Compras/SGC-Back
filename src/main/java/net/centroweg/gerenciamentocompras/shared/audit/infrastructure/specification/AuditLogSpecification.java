package net.centroweg.gerenciamentocompras.shared.audit.infrastructure.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import lombok.NoArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Component
public class AuditLogSpecification {

    public Specification<AuditLog> typeActionEquals(String typeAction){

        if(typeAction == null || typeAction.isBlank()){
            return Specification.unrestricted();
        }

        return ( (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("typeAction"), typeAction)
        );

    }

    public Specification<AuditLog> agentUserEmailEquals(String agentUser){
        if(agentUser == null || agentUser.isBlank()){
            return Specification.unrestricted();
        }

        String pattern = "%" + agentUser + "%";

        return ( (root, query, criteriaBuilder) ->{

            Join<AuditLog, User> userJoin = root.join("user", JoinType.INNER);

            Path<String> userName = userJoin.get("name");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(userName), pattern
            );
        }
        );
    }

    public Specification<AuditLog> auditLogDateBetween(
            LocalDate startDate,
            LocalDate endDate
    ){
        if (startDate == null && endDate == null) {
            return Specification.unrestricted();
        }


        return ((root, query, criteriaBuilder) -> {

            Path<LocalDateTime> logDate = root.get("timestamp");

            if(startDate != null && endDate != null){
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(
                                logDate,
                                startDateTime
                        ),
                        criteriaBuilder.lessThan(
                                logDate,
                                endDateTime
                        )
                );
            }

            if(startDate != null){
                LocalDateTime startDateTime = startDate.atStartOfDay();

                return criteriaBuilder.greaterThanOrEqualTo(
                        logDate,
                        startDateTime
                );
            }

            LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

            return criteriaBuilder.lessThan(
                    logDate,
                    endDateTime
            );
        });
    }
}
