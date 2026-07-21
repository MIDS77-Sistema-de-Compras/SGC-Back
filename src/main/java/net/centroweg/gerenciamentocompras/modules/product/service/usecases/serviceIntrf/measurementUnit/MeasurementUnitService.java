package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.measurementUnit;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Interface de serviço para operações de gerenciamento da {@link MeasurementUnit}.
 */
public interface MeasurementUnitService {

    /**
     * Cria e persiste uma nova unidade de medida no banco de dados.
     * @param request dados da unidade de medida.
     * @return unidade de medida criada.
     */
    MeasurementUnitResponse createMeasurementUnit(
        MeasurementUnitRequest request
    );

    /**
     * Lista todas as unidades de medida cadastradas no banco de dados.
     * @return lista com todas as unidades de medida encontradas, caso exista.
     */
    List<MeasurementUnitResponse> readMeasurementUnit();

    /**
     * Atualiza uma unidade de medida existente no banco de dados.
     * @param id identificador da unidade de medida.
     * @param request novos dados da unidade de medida.
     * @return unidade de medida já atualizada.
     */
    MeasurementUnitResponse updateMeasurementUnit(
        Long id,
        MeasurementUnitRequest request
    );

    /**
     * Busca uma unidade de medida no banco de dados pelo ID informado.
     * @param id identificador da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    MeasurementUnitResponse findMeasurementUnitById(Long id);

    /**
     * Busca uma unidade de medida no banco de dados pela abreviação(sigla).
     * @param abbreviation abreviação(sigla) da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    MeasurementUnitResponse findMeasurementUnitByAbbreviation(
        String abbreviation
    );
}