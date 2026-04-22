package br.unitins.exception.mapper;

import br.unitins.exception.ProblemDetail;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final String TYPE = "http://localhost:8080/errors/internal-server-error";

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        ProblemDetail problemDetail = new ProblemDetail(
            500,
            "Erro interno",
            "Ocorreu um erro interno no processamento da requisição."
        );
        problemDetail.setType(TYPE);
        if (uriInfo != null) {
            problemDetail.setInstance("/" + uriInfo.getPath());
        }
        return Response.status(500).entity(problemDetail).build();
    }
}
