package tp_avancee_dev.tp_avancee.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp_avancee_dev.tp_avancee.api.dto.LoginRequestDto;
import tp_avancee_dev.tp_avancee.api.dto.LoginResponseDto;
import tp_avancee_dev.tp_avancee.service.AuthService;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;

    public AuthResource() {
        this.authService = new AuthService();
    }

    @POST
    public Response login(@Valid LoginRequestDto request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return Response.ok(new LoginResponseDto(token)).build();
    }
}