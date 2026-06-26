package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import lombok.NoArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@NoArgsConstructor
public final class RequestSpecification {

    public static Specification<Request> createdByUser(String email) {
        if (isBlank(email)) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Request, User> userJoin =
                    root.join("createdByUsers", JoinType.INNER);
            return criteriaBuilder.equal(userJoin.get("email"), email);
        };
    }

    public static Specification<Request> crCodeContain(String crCode){
        if(isBlank(crCode)){
            return Specification.unrestricted();
        }

        String pattern = contains(crCode);

        return (root, query, criteriaBuilder) -> {
            Join<Request, CrBranch> crBranchJoin =
                    root.join("crBranch", JoinType.INNER);

            Join<CrBranch, Cr> crJoin =
                    crBranchJoin.join("cr", JoinType.INNER);

            Path<String> code = crJoin.get("code");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(code),
                    pattern
            );
        };
    }

    public static Specification<Request> statusNameEquals(String statusName){

        if (isBlank(statusName)){
            return Specification.unrestricted();
        }
        String pattern = normalizeIgnoreCase(statusName);

        return (root, query, criteriaBuilder) -> {
            Join<Request, Status> statusJoin =
                    root.join("status", JoinType.INNER);

            Path<String> name = statusJoin.get("name");

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(name),
                    pattern
            );
        };
    }

    public static Specification<Request> supervisorNameContain(String  supervisorName){

        if(isBlank(supervisorName)){
            return Specification.unrestricted();
        }

        String pattern = containsIgnoreCase(supervisorName);

        return (root, query, criteriaBuilder) -> {
            Join<Request, CrBranch> crBranchJoin =
                    root.join("crBranch", JoinType.INNER);

            Join<CrBranch, User> supervisorJoin =
                    crBranchJoin.join("responsibleUsers", JoinType.LEFT);

            Path<String> name = supervisorJoin.get("name");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(name),
                    pattern
            );
        };
    }

    public static Specification<Request> requestDateBetween(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null && endDate == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) -> {
            Path<LocalDateTime> requestDate =
                    root.get("requestDate");

            if(startDate != null && endDate != null){
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(
                                requestDate,
                                startDateTime
                        ),
                        criteriaBuilder.lessThan(
                                requestDate,
                                endDateTime
                        )
                );
            }

            if(startDate != null){
                LocalDateTime startDateTime = startDate.atStartOfDay();

                return criteriaBuilder.greaterThanOrEqualTo(
                        requestDate,
                        startDateTime
                );
            }

            LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

            return criteriaBuilder.lessThan(
                    requestDate,
                    endDateTime
            );
        };
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
    private static String contains(String value) {
        return "%" + value.trim() + "%";
    }

    private static String containsIgnoreCase(String value) {
        return "%"
                + normalizeIgnoreCase(value)
                + "%";
    }

    private static String normalizeIgnoreCase(String value) {
        return value
                .trim()
                .toLowerCase(Locale.ROOT);

    }
}
