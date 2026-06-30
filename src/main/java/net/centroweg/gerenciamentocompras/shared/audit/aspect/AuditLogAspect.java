package net.centroweg.gerenciamentocompras.shared.audit.aspect;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.filter.SecurityFilter;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.infrastructure.persistence.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAction(JoinPoint joinPoint, Auditable auditable, Object result){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actor = (auth != null) ? auth.getName() : "SISTEMA";
        String role = (auth != null) ? auth.getAuthorities().toString() : "N/A";

        Long affectedRequestId = extractIdFromArgs(joinPoint.getArgs());


    }

    private Long extractIdFromArgs(Object[] args){
        return Arrays.stream(args)
                .filter(arg -> arg instanceof Long)
                .map(arg -> (Long) arg)
                .findFirst()
                .orElse(null);

    }
}
