package br.unitins.exception.mapper;

import br.unitins.exception.ProblemDetail;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final String TYPE = "http://localhost:8080/errors/bad-request";

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        ProblemDetail problemDetail = new ProblemDetail(
            400,
            "Requisição inválida",
            exception.getMessage()
        );
        problemDetail.setType(TYPE);
        if (uriInfo != null) {
            problemDetail.setInstance("/" + uriInfo.getPath());
        }
        return Response.status(400).entity(problemDetail).build();
    }
}
