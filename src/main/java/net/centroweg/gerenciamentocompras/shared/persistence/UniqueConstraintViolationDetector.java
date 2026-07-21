package net.centroweg.gerenciamentocompras.shared.persistence;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Locale;

/**
 * Identifies a database constraint violation without masking unrelated integrity errors.
 */
public final class UniqueConstraintViolationDetector {

    private UniqueConstraintViolationDetector() {
    }

    public static boolean isConstraintViolation(
            DataIntegrityViolationException exception,
            String constraintName
    ) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException violation
                    && violation.getConstraintName() != null) {
                String actualName = violation.getConstraintName().toLowerCase(Locale.ROOT);
                if (actualName.equals(constraintName.toLowerCase(Locale.ROOT))
                        || actualName.contains(constraintName.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
}
