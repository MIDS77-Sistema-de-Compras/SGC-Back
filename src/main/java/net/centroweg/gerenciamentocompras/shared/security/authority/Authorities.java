package net.centroweg.gerenciamentocompras.shared.security.authority;

public final class Authorities {

    private Authorities() {
        throw new IllegalStateException(
                "Classe utilitária não deve ser instanciada"
        );
    }

    public static final String DOCENTE = "DOCENTE";
    public static final String COMPRADOR = "COMPRADOR";
    public static final String SUPERVISOR = "SUPERVISOR";
    public static final String COORDENADOR = "COORDENADOR";
    public static final String ADMIN = "ADMIN";
}
