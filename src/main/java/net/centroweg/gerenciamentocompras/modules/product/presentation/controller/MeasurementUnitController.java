package net.centroweg.gerenciamentocompras.modules.product.presentation.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.MeasurementUnitService;
/**
 * Controller REST responsável pelos endpoints de gerenciamento de unidades de medida.
 *
 * <p>Expõe operações de criação, listagem, busca e atualização para a entidade
 * {@link net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit},
 * acessíveis sob o caminho base {@code /measurement-unit}.</p>
 *
 * <p>Todas as respostas são encapsuladas em {@link MeasurementUnitResponse} e
 * as requisições de escrita utilizam {@link MeasurementUnitRequest} com
 * validação automática via {@code @Valid}.</p>
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 * @see MeasurementUnitService
 */
@Tag(name = "ENDPOINTS da entidade MEASUREMENT-UNIT")
@RestController
@RequestMapping("/measurement-unit")
@RequiredArgsConstructor
public class MeasurementUnitController {
    
    private final MeasurementUnitService measurementUnitService;

    /**
     * Serviço responsável pela lógica de negócio das unidades de medida.
     *
     * <p>Injetado automaticamente pelo Spring via construtor gerado pelo Lombok
     * ({@code @RequiredArgsConstructor}).</p>
     */
    private final MeasurementUnitService measurementUnitService;

    /**
     * Cria uma nova unidade de medida.
     *
     * <p><b>HTTP:</b> {@code POST /measurement-unit}</p>
     *
     * <p>O corpo da requisição é validado automaticamente. Caso a validação
     * falhe, a API retorna {@code 400 Bad Request}.</p>
     *
     * @param request objeto contendo os dados da unidade de medida a ser criada;
     *                não deve ser {@code null} e deve ser válido conforme as
     *                restrições definidas em {@link MeasurementUnitRequest}
     * @return {@link ResponseEntity} com status {@code 201 Created} e o
     *         {@link MeasurementUnitResponse} da unidade criada no corpo
     */
    @Operation(description = "ENDPOINT responsável pela criação de Measurement Unit")
    @PostMapping
    public ResponseEntity<MeasurementUnitResponse> createMeasurementUnit(@Valid @RequestBody MeasurementUnitRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(measurementUnitService.createMeasurementUnit(request));
    }
    /**
     * Lista todas as unidades de medida cadastradas.
     *
     * <p><b>HTTP:</b> {@code GET /measurement-unit}</p>
     *
     * @return {@link ResponseEntity} com status {@code 200 OK} e uma {@link List}
     *         de {@link MeasurementUnitResponse}; retorna lista vazia caso não
     *         haja registros cadastrados
     */
    
    @Operation(description = "ENDPOINT responsável pela listagem de todos Measurement Unit")
    @GetMapping
    public ResponseEntity<List<MeasurementUnitResponse>> readMeasurementUnit(){
        return ResponseEntity.ok(measurementUnitService.readMeasurementUnit());
    }
    /**
     * Busca uma unidade de medida pelo seu identificador único.
     *
     * <p><b>HTTP:</b> {@code GET /measurement-unit/{id}}</p>
     *
     * <p>Caso nenhuma unidade seja encontrada para o {@code id} informado,
     * o serviço deve lançar uma exceção adequada, resultando em
     * {@code 404 Not Found}.</p>
     *
     * @param id identificador único da unidade de medida; não deve ser {@code null}
     * @return {@link ResponseEntity} com status {@code 200 OK} e o
     *         {@link MeasurementUnitResponse} correspondente no corpo
     */
    @Operation(description = "ENDPOINT responsável pela listagem de Measurement Unit por id")
    @GetMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> findMeasurementUnitById(@PathVariable Long id){
        return ResponseEntity.ok(measurementUnitService.findMeasurementUnitById(id));
    }
    /**
     * Busca uma unidade de medida pela sua abreviação.
     *
     * <p><b>HTTP:</b> {@code GET /measurement-unit/search?abbreviation={abbreviation}}</p>
     *
     * <p>A busca é realizada de forma exata conforme a configuração do banco de dados.
     * Caso nenhuma unidade seja encontrada, o serviço deve lançar uma exceção
     * adequada, resultando em {@code 404 Not Found}.</p>
     *
     * <p>Exemplo de requisição:</p>
     * <pre>{@code
     * GET /measurement-unit/search?abbreviation=kg
     * }</pre>
     *
     * @param abbreviation a abreviação da unidade de medida a ser pesquisada;
     *                     não deve ser {@code null}
     * @return {@link ResponseEntity} com status {@code 200 OK} e o
     *         {@link MeasurementUnitResponse} correspondente no corpo
     */
    @Operation(description = "ENDPOINT responsável pela busca de Measurement Unit por abreviação")
    @GetMapping("/search")
    public ResponseEntity<MeasurementUnitResponse> findMeasurementUnitByAbbreviation(@RequestParam String abbreviation){
        return ResponseEntity.ok(measurementUnitService.findMeasurementUnitByAbbreviation(abbreviation));
    }
    /**
     * Atualiza os dados de uma unidade de medida existente.
     *
     * <p><b>HTTP:</b> {@code PUT /measurement-unit/{id}}</p>
     *
     * <p>O corpo da requisição é validado automaticamente. Caso o {@code id}
     * não corresponda a nenhum registro existente, o serviço deve lançar uma
     * exceção adequada, resultando em {@code 404 Not Found}.</p>
     *
     * @param id      identificador único da unidade de medida a ser atualizada;
     *                não deve ser {@code null}
     * @param request objeto contendo os novos dados da unidade de medida;
     *                não deve ser {@code null} e deve ser válido conforme as
     *                restrições definidas em {@link MeasurementUnitRequest}
     * @return {@link ResponseEntity} com status {@code 200 OK} e o
     *         {@link MeasurementUnitResponse} atualizado no corpo
     */
    @Operation(description = "ENDPOINT responsável pela atualização de Measurement Unit")
    @PutMapping("/{id}")
    public ResponseEntity<MeasurementUnitResponse> updateMeasurementUnit( @PathVariable Long id, @Valid @RequestBody MeasurementUnitRequest request){
        return ResponseEntity.ok(measurementUnitService.updateMeasurementUnit(id, request));
    }



}
