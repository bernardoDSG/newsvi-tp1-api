package br.unitins.exception.mapper;

import br.unitins.exception.ConflictException;
import br.unitins.exception.ProblemDetail;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    private static final String TYPE = "http://localhost:8080/errors/conflict";

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConflictException exception) {
        ProblemDetail problemDetail = new ProblemDetail(
            409,
            "Conflito de negócio",
            exception.getMessage()
        );
        problemDetail.setType(TYPE);
        if (uriInfo != null) {
            problemDetail.setInstance("/" + uriInfo.getPath());
        }
        return Response.status(409).entity(problemDetail).build();
    }
}
