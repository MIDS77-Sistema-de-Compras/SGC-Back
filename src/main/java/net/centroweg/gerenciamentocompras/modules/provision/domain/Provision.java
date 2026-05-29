package net.centroweg.gerenciamentocompras.modules.provision.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Classe representante do módulo serviço, mapeada diretamente com o banco de dados.
 * <p>
 * Detém dados para persistencia.
 * @author gabrielEFagundes
 * @version 0.1.0
 */
@Entity
@Table(name="provision")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Provision {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;
    
    @NonNull
    private Double totalValue;

    @NonNull
    private String description;

}
