package br.unitins.exception.mapper;

import br.unitins.exception.ProblemDetail;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private static final String TYPE = "http://localhost:8080/errors/not-found";

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        ProblemDetail problemDetail = new ProblemDetail(
            404,
            "Recurso não encontrado",
            exception.getMessage()
        );
        problemDetail.setType(TYPE);
        if (uriInfo != null) {
            problemDetail.setInstance("/" + uriInfo.getPath());
        }
        return Response.status(404).entity(problemDetail).build();
    }
}
