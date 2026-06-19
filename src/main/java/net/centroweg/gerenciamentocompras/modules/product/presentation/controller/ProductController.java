package net.centroweg.gerenciamentocompras.modules.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST responsável pelos endpoints de gerenciamento de produtos.
 *
 * <p>Expõe operações de criação, listagem, busca, atualização e remoção para a entidade
 * {@link Product},
 * acessíveis sob o caminho base {@code /products}.</p>
 *
 * <p>As requisições de escrita utilizam {@link CreateProductRequest} e
 * {@link UpdateProductRequest} com validação automática via {@code @Valid}.
 * Todas as respostas são encapsuladas em {@link ProductResponse}.</p>
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 * @see IProductService
 */

@Tag(name = "ENDPOINTS da entidade PRODUCT")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    /**
     * Serviço responsável pela lógica de negócio dos produtos.
     *
     * <p>Injetado automaticamente pelo Spring via construtor gerado pelo Lombok
     * ({@code @RequiredArgsConstructor}).</p>
     */
    private final IProductService productService;

    /**
     * Cria um novo produto.
     *
     * <p><b>HTTP:</b> {@code POST /products}</p>
     *
     * <p>O corpo da requisição é validado automaticamente. Caso a validação
     * falhe, a API retorna {@code 400 Bad Request}.</p>
     *
     * @param request objeto contendo os dados do produto a ser criado;
     *                não deve ser {@code null} e deve ser válido conforme as
     *                restrições definidas em {@link CreateProductRequest}
     * @return {@link ResponseEntity} com status {@code 201 Created} e o
     *         {@link ProductResponse} do produto criado no corpo
     */
    @Operation(description = "ENDPOINT responsável pela criação de Product")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    /**
     * Busca um produto pelo seu identificador único.
     *
     * <p><b>HTTP:</b> {@code GET /products/{id}}</p>
     *
     * <p>Caso nenhum produto seja encontrado para o {@code id} informado,
     * o serviço deve lançar uma exceção adequada, resultando em
     * {@code 404 Not Found}.</p>
     *
     * @param id identificador único do produto; não deve ser {@code null}
     * @return {@link ResponseEntity} com status {@code 200 OK} e o
     *         {@link ProductResponse} correspondente no corpo
     */
    @Operation(description = "ENDPOINT responsável pela listagem de Product por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * Lista todos os produtos cadastrados, com filtro opcional por nome.
     *
     * <p><b>HTTP:</b> {@code GET /products} ou {@code GET /products?name={name}}</p>
     *
     * <p>Quando o parâmetro {@code name} é informado, a busca é realizada de forma
     * parcial e case-insensitive. Quando omitido, todos os produtos são retornados.</p>
     *
     * <p>Exemplos de requisição:</p>
     * <pre>{@code
     * GET /products               // retorna todos os produtos
     * GET /products?name=parafuso // retorna produtos cujo nome contenha "parafuso"
     * }</pre>
     *
     * @param name trecho do nome a ser filtrado; pode ser {@code null},
     *             neste caso todos os produtos são retornados
     * @return {@link ResponseEntity} com status {@code 200 OK} e uma {@link List}
     *         de {@link ProductResponse}; retorna lista vazia caso nenhum
     *         produto corresponda ao filtro
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.findAll(name));
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * <p><b>HTTP:</b> {@code PUT /products/{id}}</p>
     *
     * <p>O corpo da requisição é validado automaticamente. Caso o {@code id}
     * não corresponda a nenhum registro existente, o serviço deve lançar uma
     * exceção adequada, resultando em {@code 404 Not Found}.</p>
     *
     * @param id      identificador único do produto a ser atualizado;
     *                não deve ser {@code null}
     * @param request objeto contendo os novos dados do produto;
     *                não deve ser {@code null} e deve ser válido conforme as
     *                restrições definidas em {@link UpdateProductRequest}
     * @return {@link ResponseEntity} com status {@code 200 OK} e o
     *         {@link ProductResponse} atualizado no corpo
     */
    @Operation(description = "ENDPOINT responsável pela atualização de Product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    /**
     * Remove um produto pelo seu identificador único.
     *
     * <p><b>HTTP:</b> {@code DELETE /products/{id}}</p>
     *
     * <p>Caso nenhum produto seja encontrado para o {@code id} informado,
     * o serviço deve lançar uma exceção adequada, resultando em
     * {@code 404 Not Found}. A operação não retorna corpo na resposta.</p>
     *
     * @param id identificador único do produto a ser removido; não deve ser {@code null}
     * @return {@link ResponseEntity} com status {@code 204 No Content} e corpo vazio
     */
    @Operation(description = "ENDPOINT responsável pelo delete de Product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
