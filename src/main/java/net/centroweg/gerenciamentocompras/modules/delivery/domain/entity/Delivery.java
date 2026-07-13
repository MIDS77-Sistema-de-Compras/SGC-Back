package net.centroweg.gerenciamentocompras.modules.delivery.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "delivery")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime expectedDeliveryAt;

    private LocalDateTime deliveredAt;

    @Column(nullable = false)
    private String deliveryLocation;

    private String description;

    private String proofUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @BatchSize(size = 30)
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryReceiver> receivers = new ArrayList<>();

    @BatchSize(size = 30)
    @ManyToMany
    @JoinTable(
            name = "delivery_item_request_product",
            joinColumns = @JoinColumn(name = "delivery_id"),
            inverseJoinColumns = @JoinColumn(name = "item_request_product_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_delivery_product_item",
                    columnNames = {"delivery_id", "item_request_product_id"}
            )
    )
    private Set<ItemRequestProduct> productItems = new LinkedHashSet<>();

    @BatchSize(size = 30)
    @ManyToMany
    @JoinTable(
            name = "delivery_item_request_service",
            joinColumns = @JoinColumn(name = "delivery_id"),
            inverseJoinColumns = @JoinColumn(name = "item_request_service_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_delivery_service_item",
                    columnNames = {"delivery_id", "item_request_service_id"}
            )
    )
    private Set<ItemRequestProvision> provisionItems = new LinkedHashSet<>();

    public void addReceiver(User user) {
        DeliveryReceiver receiver = new DeliveryReceiver(this, user);
        receivers.add(receiver);
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
