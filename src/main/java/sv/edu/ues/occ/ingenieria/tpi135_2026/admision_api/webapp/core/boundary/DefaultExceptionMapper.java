package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import static sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.util.ExceptionUtils.getRootCause;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable e) {
        
        final String detail = getRootCause(e).toString();

        Rfc9457 rfc9457 = new Rfc9457.Builder()
            .title("Ha ocurrido un error en el servidor")
            .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
            .detail(detail)
            .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(rfc9457)
            .build();
    }


    
}
