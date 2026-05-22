package net.centroweg.gerenciamentocompras.modules.product.service.api;

import net.centroweg.gerenciamentocompras.modules.product.service.api.dto.ProductData;

public interface IProductPublicAPI {

        ProductData findById(Long id);

    }

