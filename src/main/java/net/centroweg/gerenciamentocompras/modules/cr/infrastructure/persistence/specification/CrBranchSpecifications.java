package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.NoArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;


@NoArgsConstructor
public final class CrBranchSpecifications {

    public static Specification<CrBranch> crCodeContain(String crCode){
        if(isBlank(crCode)){
            return Specification.unrestricted();
        }

        String pattern = containsIgnoreCase(crCode);

        return (root, query, criteriaBuilder) -> {
            Join<CrBranch,Cr> crJoin =
                    root.join("cr", JoinType.INNER);

            Expression<String> code = crJoin.get("code");

            return criteriaBuilder.like(
              code,
              pattern
            );
        };
    }

    public static Specification<CrBranch> crNameContain(String crName){
        if(isBlank(crName)){
            return Specification.unrestricted();
        }

        String pattern = containsIgnoreCase(crName);

        return (root, query, criteriaBuilder) -> {
            Join<CrBranch, Cr> crJoin =
                    root.join("cr", JoinType.INNER);

            Expression<String> name = crJoin.get("name");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(name),
                    pattern
            );
        };
    }

    public static Specification<CrBranch> crResponsibleNameContain(String responsibleName){

        if (isBlank(responsibleName)){
            return Specification.unrestricted();
        }

        String pattern = containsIgnoreCase(responsibleName);

        return (root, query, criteriaBuilder) -> {
            Join<CrBranch, User> responsibleUserJoin =
                    root.join("responsibleUser", JoinType.LEFT);

            Expression<String> name = responsibleUserJoin.get("name");

            return  criteriaBuilder.like(
                    criteriaBuilder.lower(name),
                    pattern
            );
        };
    };



    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String containsIgnoreCase(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
