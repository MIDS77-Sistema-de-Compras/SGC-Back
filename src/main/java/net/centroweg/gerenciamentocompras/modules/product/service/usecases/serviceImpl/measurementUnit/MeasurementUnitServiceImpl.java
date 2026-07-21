package net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceImpl.measurementUnit;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.measurementUnit.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.MeasurementUnitResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.measurementUnit.MeasurementUnitService;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * Classe de serviço da {@link MeasurementUnit} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link MeasurementUnitService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */

@RequiredArgsConstructor
@Service
public class MeasurementUnitServiceImpl implements MeasurementUnitService {

    /**
     * Componente responsável pela criação de uma unidade de medida.
     */
    private final CreateMeasurementUnit create;

    /**
     * Componente responsável pela listagem das unidades de medida cadastradas.
     */
    private final FindAllMeasurementUnit read;

    /**
     * Componente responsável pela atualização de uma unidade de medida existente.
     */
    private final UpdateMeasurementUnit update;

    /**
     * Componente responsável por buscar uma unidade de medida pelo ID informado.
     */
    private final FindMeasurementUnitById findById;

    /**
     * Componente responsável por buscar uma unidade de medida pela abreviação(sigla).
     */
    private final FindMeasurementUnitByAbbreviation findByAbbreviation;

    /**
     * Cria e persiste uma nova unidade de medida no banco de dados.
     * @param request dados da unidade de medida.
     * @return unidade de medida criada.
     */
    @Override
    public MeasurementUnitResponse createMeasurementUnit(MeasurementUnitRequest request) {
        return create.createMeasurementUnit(request);
    }

    /**
     * Lista todas as unidades de medida cadastradas no banco de dados.
     * @return lista com todas as unidades de medida encontradas, caso exista.
     */
    @Override
    public List<MeasurementUnitResponse> readMeasurementUnit() {
        return read.readMeasurementUnit();
    }

    /**
     * Atualiza uma unidade de medida existente no banco de dados.
     * @param id identificador da unidade de medida.
     * @param request novos dados da unidade de medida.
     * @return unidade de medida já atualizada.
     */
    @Override
    public MeasurementUnitResponse updateMeasurementUnit(Long id, MeasurementUnitRequest request) {
        return update.updateMeasurementUnit(id, request);
    }

    /**
     * Busca uma unidade de medida no banco de dados pelo ID informado.
     * @param id identificador da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    @Override
    public MeasurementUnitResponse findMeasurementUnitById(Long id) {
        return findById.findMeasurementUnitById(id);
    }

    /**
     * Busca uma unidade de medida no banco de dados pela abreviação(sigla).
     * @param abbreviation abreviação(sigla) da unidade de medida.
     * @return unidade de medida encontrada, caso exista.
     */
    @Override
    public MeasurementUnitResponse findMeasurementUnitByAbbreviation(String abbreviation) {
        return findByAbbreviation.findMeasurementUnitByAbbreviation(abbreviation);
    }
}