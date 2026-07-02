package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.NoArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Locale;

/**
 * Classe utilitária responsável por fornecer Specifications utilizadas na construção dinâmica de consultas da entidade {@link CrBranch}.
 */
@NoArgsConstructor
public final class CrBranchSpecifications {

    /**
     * Cria uma Specification para filtrar filial de CR pelo código do CR.
     * @param crCode código do CR utilizado na pesquisa.
     * @return Specification correspondente ao filtro informado ou unrestricted caso o parâmetro seja nulo ou vazio.
     */
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
                    criteriaBuilder.lower(code),
                    pattern
            );
        };
    }

    /**
     * Cria uma Specification para filtrar filial de CR pelo nome do CR.
     * @param crName nome do CR utilizado na pesquisa.
     * @return Specification correspondente ao filtro informado ou unrestricted caso o parâmetro seja nulo ou vazio.
     */
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

    /**
     * Cria uma Specification para filtrar filial de CR pelos responsáveis.
     * @param responsibleNames lista de nomes dos responsáveis.
     * @return Specification correspondente ao filtro informado ou unrestricted caso a lista seja nulo ou vazio.
     */
    public static Specification<CrBranch> crResponsibleNameIn(List<String> responsibleNames){

        if (responsibleNames == null || responsibleNames.isEmpty()){
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) -> {
            Join<CrBranch, User> responsibleUserJoin =
                    root.join("responsibleUsers", JoinType.LEFT);

            query.distinct(true);

            jakarta.persistence.criteria.Predicate[] predicates = responsibleNames.stream()
                    .filter(name -> name != null && !name.isBlank())
                    .map(name -> criteriaBuilder.like(
                            criteriaBuilder.lower(responsibleUserJoin.get("name")),
                            "%" + name.trim().toLowerCase(Locale.ROOT) + "%"
                    ))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new);

            if (predicates.length == 0) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.or(predicates);
        };
    }

    /**
     * Verifica se a String é nula ou contém apenas espaços em branco.
     * @param value valor a ser verificado.
     * @return booleano, caso seja vazio ou nulo(true) e se tiver algo escrito(false).
     */
    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Converte um texto para o padrão utilizado em consultas do tipo {@code LIKE}, ignorando diferenças em relação ao tamanho das letras.
     * @param value texto informado pelo usuário.
     * @return texto formatado para pesquisas utilizando o {@code LIKE}.
     */
    private static String containsIgnoreCase(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
