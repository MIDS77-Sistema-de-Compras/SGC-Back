package net.centroweg.gerenciamentocompras.shared.security.annotation;

import net.centroweg.gerenciamentocompras.shared.security.authority.AuthorizationExpressions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize(AuthorizationExpressions.CAN_MANAGE_MEASUREMENT_UNIT)
public @interface CanManageMeasurementUnit {
}
