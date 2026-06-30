package net.centroweg.gerenciamentocompras.shared.audit.aspect;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditParam;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import net.centroweg.gerenciamentocompras.shared.audit.service.api.AuditLogPublicApi;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.IntStream;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogPublicApi auditLogPublicApi;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAction(JoinPoint joinPoint, Auditable auditable, Object result){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth==null) return;

        User agent = auditLogPublicApi.findByUserEmail(auth.getName());
        if(agent == null) return;

        Long requestId = extractIdByAnnotation(joinPoint, "request");
        Long targetUserId = extractIdByAnnotation(joinPoint, "user");

        Request requestSearched = (requestId != null) ? auditLogPublicApi.findByRequestId(requestId) : null;
        User targetUserSearched = (targetUserId != null) ? auditLogPublicApi.findByUserId(targetUserId) : null;

        AuditLog auditLog = new AuditLog(agent, auditable.action());
        auditLog.setRequest(requestSearched);
        auditLog.setUserTarget(targetUserSearched);

        auditLogRepository.save(auditLog);

    }

    private Long extractIdByAnnotation(JoinPoint joinPoint, String targetName){

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        return IntStream.range(0, parameters.length)
                .filter(i -> {
                    AuditParam auditParam = parameters[i].getAnnotation(AuditParam.class);
                    return auditParam != null && targetName.equals(auditParam.value());
                })
                .filter(i -> args[i] instanceof Long)
                .mapToObj(i -> (Long) args[i])
                .findFirst()
                .orElse(null);

    }


}
