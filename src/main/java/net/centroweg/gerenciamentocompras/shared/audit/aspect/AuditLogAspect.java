package net.centroweg.gerenciamentocompras.shared.audit.aspect;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditParam;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import net.centroweg.gerenciamentocompras.shared.audit.service.api.AuditLogPublicApi;
import net.centroweg.gerenciamentocompras.shared.security.ImpersonationDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        // Auditoria é um interesse transversal e best-effort: nunca deve quebrar a
        // operação de negócio. Sem um agente resolvível (ex.: requisição anônima) não
        // há a quem atribuir a ação, então apenas ignoramos.
        User agent = safeLookup(() -> auditLogPublicApi.findByUserEmail(auth.getName()));
        if(agent == null) return;

        Long requestId = extractIdByAnnotation(joinPoint, "request");
        Long targetUserId = Optional.ofNullable(extractIdByAnnotation(joinPoint, "user"))
                .orElseGet(() -> auditable.targetFromReturn() ? extractIdFromReturnObject(result) : null);

        Request requestSearched = (requestId != null) ? safeLookup(() -> auditLogPublicApi.findByRequestId(requestId)) : null;
        User targetUserSearched = (targetUserId != null) ? safeLookup(() -> auditLogPublicApi.findByUserId(targetUserId)) : null;

        AuditLog auditLog = new AuditLog(agent, auditable.action());
        auditLog.setRequest(requestSearched);
        auditLog.setUserTarget(targetUserSearched);

        // Quando um administrador está logado na conta de outro usuário, a ação é
        // registrada em nome do usuário, mas a descrição deixa claro quem realmente agiu.
        if (auth.getDetails() instanceof ImpersonationDetails impersonation) {
            auditLog.setDescription("Ação realizada pelo administrador " + impersonation.adminName()
                    + " logado na conta de " + agent.getName() + ".");
        }

        auditLogRepository.save(auditLog);

    }

    /**
     * Executa um lookup de entidade tolerando ausência: se a entidade não existir
     * (as APIs de auditoria lançam exceção quando não encontram), retorna null em vez
     * de propagar o erro e derrubar a operação de negócio auditada.
     */
    private <T> T safeLookup(Supplier<T> lookup){
        try {
            return lookup.get();
        } catch (RuntimeException exception) {
            return null;
        }
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

    private Long extractIdFromReturnObject(Object result){
        if (result == null) return null;

        Object actualBody = (result instanceof ResponseEntity<?> responseEntity)
                ? responseEntity.getBody()
                : result;

        if (actualBody == null) return null;

        Class<?> clazz = actualBody.getClass();

        return Stream.of("id", "getId")
                .map(methodName -> {
                    try {
                        return clazz.getMethod(methodName);
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                })
                .filter(method -> method != null && method.getReturnType().equals(Long.class))
                .map(method -> {
                    try {
                        return (Long) method.invoke(actualBody);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(id -> id != null)
                .findFirst()
                .orElse(null);
    }

    private String extractUsernameFromReturnObject(Object result){
        if (result == null) return null;

        Object actualBody = (result instanceof ResponseEntity<?> responseEntity)
                ? responseEntity.getBody()
                : result;

        if (actualBody == null) return null;

        Class<?> clazz = actualBody.getClass();

        return Stream.of("userName")
                .map(methodName -> {
                    try {
                        return clazz.getMethod(methodName);
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                })
                .filter(method -> method != null && method.getReturnType().equals(String.class))
                .map(method -> {
                    try {
                        return (String) method.invoke(actualBody);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(userName -> userName != null)
                .findFirst()
                .orElse(null);
    }



}
