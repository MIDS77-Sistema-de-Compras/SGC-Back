package net.centroweg.gerenciamentocompras.shared.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para limitar as requisições de um endpoint
 * <p>
 *
 * {@code profile()} Perfil de limitação que define a quantidade de requisições, ou tokens, que um endpoint pode receber/consumir antes de ser bloqueado.
 *
 * @see net.centroweg.gerenciamentocompras.config.Bucket4jConfig
 * @author gabrielEFagundes
 * @since 21/07/2026
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String profile() default "";
}
