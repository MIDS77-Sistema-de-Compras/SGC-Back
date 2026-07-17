package net.centroweg.gerenciamentocompras.shared.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method parameter (or the method itself) whose value should be stored
 * as the {@code description} column of {@link AuditLog}.
 *
 * <p>If placed on a parameter the aspect reads the argument value.
 * If placed on the method the aspect reads the return value (after unwrapping a
 * possible {@link ResponseEntity}).</p>
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditInfo {
    /**
     * Optional static prefix – useful when you want to add constant text
     * before the dynamic value (e.g. "Novo status: ").
     */
    String value() default "";
}