package net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados da entidade {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca um produto pelo seu código identificador único.
     * @param code código do produto.
     * @return produto encontrado, caso exista.
     */
    Optional<Product> findByCode(String code);

    /**
     * Busca uma lista de produtos cujo nome contenha o trecho informado, sem distinção entre maiúsculas e minúsculas.
     * @param name nome do produto, não necessariamente completo.
     * @return lista com todos os produtos encontrados, caso exista.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Busca um produto pelo nome.
     * @param name nome do produto.
     * @return produto encontrado, caso exista.
     */
    Optional<Product> findByNameIgnoreCase(String name);
}