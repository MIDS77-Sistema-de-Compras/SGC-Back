package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;

/**
 * Interface responsável pelo contrato de operações relacionadas
 * às unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
public interface MeasurementUnitService {

    /**
     * Registra uma nova unidade de medida.
     *
     * @param request Dados da unidade de medida a ser cadastrada.
     * @return Dados da unidade de medida cadastrada.
     */
    MeasurementUnitResponse createMeasurementUnit(
        MeasurementUnitRequest request
    );

    /**
     * Recupera todas as unidades de medida cadastradas.
     *
     * @return Lista contendo todas as unidades de medida encontradas.
     */
    List<MeasurementUnitResponse> readMeasurementUnit();

    /**
     * Atualiza uma unidade de medida existente.
     *
     * @param id Identificador único da unidade de medida.
     * @param request Novos dados da unidade de medida.
     * @return Dados atualizados da unidade de medida.
     */
    MeasurementUnitResponse updateMeasurementUnit(
        Long id,
        MeasurementUnitRequest request
    );

    /**
     * Busca uma unidade de medida pelo ID.
     *
     * @param id Identificador único da unidade de medida.
     * @return Dados da unidade de medida encontrada.
     */
    MeasurementUnitResponse findMeasurementUnitById(Long id);

    /**
     * Busca uma unidade de medida pela abreviação.
     *
     * @param abbreviation Sigla da unidade de medida
     * (ex.: "KG", "UN").
     * @return Dados da unidade de medida encontrada.
     */
    MeasurementUnitResponse findMeasurementUnitByAbbreviation(
        String abbreviation
    );
}