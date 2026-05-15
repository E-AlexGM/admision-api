package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.util;

public final class ExceptionUtils {

    public static Throwable getRootCause(Throwable e){
        if(e == null) throw new IllegalArgumentException("El argumento no puede ser nulo");

        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }

}
