package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.MeasurementUnitService;

/**
 * Implementação do serviço de gerenciamento de unidades de medida.
 * Atua como camada de orquestração entre os casos de uso da aplicação,
 * promovendo baixo acoplamento e alta coesão.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */

@RequiredArgsConstructor
@Service
public class MeasurementUnitServiceImpl implements MeasurementUnitService {

    private final CreateMeasurementUnit create;
    private final ReadMeasurementUnit read;
    private final UpdateMeasurementUnit update;
    private final FindMeasurementUnitById findById;
    private final FindMeasurementUnitByAbbreviation findByAbbreviation;

    /**
     * Registra uma nova unidade de medida.
     *
     * @param request Dados da nova unidade de medida.
     * @return Dados da unidade de medida persistida.
     */
    @Override
    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request) {
        return create.createMeasurementUnit(request);
    }

    /**
     * Recupera todas as unidades de medida cadastradas no sistema.
     *
     * @return Lista contendo todas as unidades de medida encontradas.
     */
    @Override
    public List<MeasurementUnitResponse> readMeasurementUnit() {
        return read.readMeasurementUnit();
    }

    /**
     * Atualiza uma unidade de medida existente.
     *
     * @param id Identificador único da unidade de medida.
     * @param request Novos dados da unidade de medida.
     * @return Dados atualizados da unidade de medida.
     * @throws ResourceNotFoundException Caso nenhuma unidade seja encontrada
     * com o ID informado.
     */
    @Override
    public MeasurementUnitResponse updateMeasurementUnit(Long id, MeasurementUnitRequest request) {
        return update.updateMeasurementUnit(id, request);
    }

    /**
     * Busca uma unidade de medida pelo ID.
     *
     * @param id Identificador único da unidade de medida.
     * @return Dados da unidade de medida encontrada.
     * @throws ResourceNotFoundException Caso nenhuma unidade seja encontrada
     * com o ID informado.
     */
    @Override
    public MeasurementUnitResponse findMeasurementUnitById(Long id) {
        return findById.findMeasurementUnitById(id);
    }

    /**
     * Busca uma unidade de medida pela abreviação.
     *
     * @param abbreviation Sigla da unidade de medida
     * (ex.: "KG", "UN").
     * @return Dados da unidade de medida encontrada.
     * @throws ResourceNotFoundException Caso nenhuma unidade seja encontrada
     * com a abreviação informada.
     */
    @Override
    public MeasurementUnitResponse findMeasurementUnitByAbbreviation(String abbreviation) {
        return findByAbbreviation.findMeasurementUnitByAbbreviation(abbreviation);
    }
}