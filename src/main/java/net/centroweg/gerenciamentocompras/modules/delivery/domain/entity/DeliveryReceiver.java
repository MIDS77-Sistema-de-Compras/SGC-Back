package net.centroweg.gerenciamentocompras.modules.delivery.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "delivery_receiver",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_delivery_receiver_delivery_user",
                columnNames = {"delivery_id", "user_id"}
        )
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryReceiver {

    @EmbeddedId
    private DeliveryReceiverId id = new DeliveryReceiverId();

    @MapsId("deliveryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime confirmedAt;

    @Column(nullable = false)
    private Boolean confirmed = false;

    private String observation;

    public DeliveryReceiver(Delivery delivery, User user) {
        this.id = new DeliveryReceiverId(
                delivery == null ? null : delivery.getId(),
                user == null ? null : user.getId()
        );
        this.delivery = delivery;
        this.user = user;
        this.confirmed = false;
    }
}
