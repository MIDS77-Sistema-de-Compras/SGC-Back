package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;

@Entity
@Table(name = "item_request_service")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemRequestProvision {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Provision provision;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Status status;

    private String additionalInformation;

    public ItemRequestProvision(@NonNull Request request, @NonNull Provision provision, @NonNull Status status, String additionalInformation) {
        this.request = request;
        this.provision = provision;
        this.status = status;
        this.additionalInformation = additionalInformation;
    }
}
