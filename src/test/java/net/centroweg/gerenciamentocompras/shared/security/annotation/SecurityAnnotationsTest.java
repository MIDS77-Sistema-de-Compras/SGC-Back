package net.centroweg.gerenciamentocompras.shared.security.annotation;

import net.centroweg.gerenciamentocompras.shared.security.authority.AuthorizationExpressions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityAnnotationsTest {

    @ParameterizedTest
    @MethodSource("securityAnnotations")
    @DisplayName("Deve declarar PreAuthorize com a expressao centralizada")
    void shouldDeclarePreAuthorizeExpression(
            Class<? extends Annotation> annotation,
            String expectedExpression
    ) {
        PreAuthorize preAuthorize = annotation.getAnnotation(PreAuthorize.class);

        assertNotNull(preAuthorize);
        assertEquals(expectedExpression, preAuthorize.value());
    }

    @ParameterizedTest
    @MethodSource("securityAnnotations")
    @DisplayName("Deve permitir uso em metodo e tipo com retention runtime")
    void shouldDeclareRuntimeRetentionAndTargets(
            Class<? extends Annotation> annotation,
            String ignoredExpression
    ) {
        Retention retention = annotation.getAnnotation(Retention.class);
        Target target = annotation.getAnnotation(Target.class);

        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
        assertNotNull(target);
        assertTrue(Arrays.asList(target.value()).contains(ElementType.METHOD));
        assertTrue(Arrays.asList(target.value()).contains(ElementType.TYPE));
    }

    private static Stream<Arguments> securityAnnotations() {
        return Stream.of(
                Arguments.of(AdminOnly.class, AuthorizationExpressions.ADMIN_ONLY),
                Arguments.of(CanManageUsers.class, AuthorizationExpressions.CAN_MANAGE_USERS),
                Arguments.of(CanCreateRequest.class, AuthorizationExpressions.CAN_CREATE_REQUEST),
                Arguments.of(CanApproveRequest.class, AuthorizationExpressions.CAN_APPROVE_REQUEST),
                Arguments.of(CanManageCr.class, AuthorizationExpressions.CAN_MANAGE_CR),
                Arguments.of(CanViewReports.class, AuthorizationExpressions.CAN_VIEW_REPORTS),
                Arguments.of(CanViewAnalytics.class, AuthorizationExpressions.CAN_VIEW_ANALYTICS),
                Arguments.of(CanManagePurchaseItems.class, AuthorizationExpressions.CAN_MANAGE_PURCHASE_ITEMS),
                Arguments.of(CanManageStatus.class, AuthorizationExpressions.CAN_MANAGE_STATUS),
                Arguments.of(CanManageMeasurementUnit.class, AuthorizationExpressions.CAN_MANAGE_MEASUREMENT_UNIT)
        );
    }
}
