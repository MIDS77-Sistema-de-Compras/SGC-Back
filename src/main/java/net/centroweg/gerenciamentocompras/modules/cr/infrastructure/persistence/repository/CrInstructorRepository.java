package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;

@Repository
public interface CrInstructorRepository extends JpaRepository<CrInstructor, Long> {
    
}
