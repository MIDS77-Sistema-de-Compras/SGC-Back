package net.centroweg.gerenciamentocompras.modules.delivery.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DeliveryReceiverId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long deliveryId;
    private Long userId;
}
