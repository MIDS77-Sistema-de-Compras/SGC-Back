package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.specification;

import lombok.NoArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

import static io.micrometer.common.util.StringUtils.isBlank;

@NoArgsConstructor
public final class CrBranchSpecifications {

    public Specification<CrBranch> crCodeContain(String crCode){
        if(isBlank(crCode)){
            Specification.unrestricted();
        }

        String pattern = containsIgnoreCase(crCode);
    }

    private static String containsIgnoreCase(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
