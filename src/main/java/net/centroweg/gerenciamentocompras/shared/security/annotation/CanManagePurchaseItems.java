package net.centroweg.gerenciamentocompras.shared.security.annotation;

import net.centroweg.gerenciamentocompras.shared.security.authority.AuthorizationExpressions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize(AuthorizationExpressions.CAN_MANAGE_PURCHASE_ITEMS)
public @interface CanManagePurchaseItems {
}
