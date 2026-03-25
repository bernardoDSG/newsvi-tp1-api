package br.unitins.resource;

import br.unitins.service.AtorPremiadoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/atores-premiados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtorPremiadoResource {
    
    @Inject
    AtorPremiadoService service;

    
}
