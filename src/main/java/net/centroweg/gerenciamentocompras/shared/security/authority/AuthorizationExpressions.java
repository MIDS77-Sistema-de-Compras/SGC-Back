package net.centroweg.gerenciamentocompras.shared.security.authority;

public final class AuthorizationExpressions {

    private AuthorizationExpressions() {
        throw new IllegalStateException(
                "Classe utilitária não deve ser instanciada"
        );
    }

    public static final String ADMIN_ONLY =
            "hasAuthority('" + Authorities.ADMIN + "')";

    public static final String SUPERVISOR_ONLY =
            "hasAuthority('" + Authorities.SUPERVISOR + "')";

    public static final String CAN_CREATE_REQUEST =
            "hasAnyAuthority(" +
                    "'" + Authorities.DOCENTE + "'," +
                    "'" + Authorities.SUPERVISOR + "'," +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_APPROVE_REQUEST =
            "hasAnyAuthority(" +
                    "'" + Authorities.SUPERVISOR + "'," +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";


    public static final String CAN_MANAGE_USERS =
            "hasAnyAuthority(" +
                    "'" + Authorities.SUPERVISOR + "'," +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_MANAGE_CR =
            "hasAnyAuthority(" +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_VIEW_REPORTS =
            "hasAnyAuthority(" +
                    "'" + Authorities.COMPRADOR + "'," +
                    "'" + Authorities.SUPERVISOR + "'," +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_UPDATE_REQUEST_STATUS =
            "hasAnyAuthority(" +
                    "'" + Authorities.COMPRADOR + "'," +
                    "'" + Authorities.SUPERVISOR + "'," +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_VIEW_ANALYTICS =
            "hasAnyAuthority(" +
                    "'" + Authorities.COORDENADOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_MANAGE_PURCHASE_ITEMS =
            "hasAuthority('" + Authorities.COMPRADOR + "')";

    public static final String CAN_MANAGE_STATUS =
            "hasAuthority(" +
                    "'" + Authorities.COMPRADOR + "'," +
                    "'" + Authorities.SUPERVISOR + "'," +
                    "'" + Authorities.ADMIN + "'" +
                    ")";

    public static final String CAN_MANAGE_MEASUREMENT_UNIT =
            CAN_MANAGE_PURCHASE_ITEMS;
}
