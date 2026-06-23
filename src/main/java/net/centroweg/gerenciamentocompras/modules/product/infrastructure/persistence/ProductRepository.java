package net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Repositório JPA para a entidade {@link Product}.
 *
 * <p>Estende {@link JpaRepository}, fornecendo operações CRUD padrão
 * e paginação para a entidade {@code Product}, além de consultas
 * personalizadas definidas nesta interface.</p>
 *
 * <p>Esta interface é gerenciada pelo Spring Data JPA, que gera
 * automaticamente a implementação em tempo de execução.
 * A anotação {@link Repository} garante que exceções de acesso
 * a dados sejam traduzidas para a hierarquia de exceções do Spring.</p>
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 * @see Product
 * @see JpaRepository
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Busca um produto pelo seu código identificador único.
     *
     * <p>Realiza uma consulta na tabela {@code product} filtrando pelo campo
     * {@code code}, que possui restrição de unicidade no banco de dados.</p>
     *
     * <p>Exemplo de uso:</p>
     * <pre>{@code
     * Optional<Product> product = repository.findByCode("PRD-00123");
     * product.ifPresent(p -> System.out.println(p.getName()));
     * }</pre>
     *
     * @param code o código único do produto a ser buscado;
     *             não deve ser {@code null}
     * @return um {@link Optional} contendo o {@link Product} encontrado,
     *         ou {@link Optional#empty()} caso nenhum produto corresponda ao código informado
     */
    Optional<Product> findByCode(String code);
    /**
     * Busca uma lista de produtos cujo nome contenha o trecho informado,
     * sem distinção entre maiúsculas e minúsculas.
     *
     * <p>Realiza uma consulta na tabela {@code product} utilizando
     * {@code LIKE %name%} de forma case-insensitive, sendo útil para
     * funcionalidades de busca e filtro na interface do usuário.</p>
     *
     * <p>Exemplo de uso:</p>
     * <pre>{@code
     * List<Product> products = repository.findByNameContainingIgnoreCase("parafuso");
     * // retorna produtos como "Parafuso M8", "PARAFUSO M10", "parafuso sextavado"
     * }</pre>
     *
     * @param name o trecho do nome a ser pesquisado; não deve ser {@code null}.
     *             Uma string vazia {@code ""} retornará todos os produtos.
     * @return uma {@link List} contendo os {@link Product}s cujo nome corresponda
     *         ao critério de busca; retorna uma lista vazia caso nenhum seja encontrado
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    Optional<Product> findByNameIgnoreCase(String name);
}