package tp_avancee_dev.tp_avancee.api;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature; // <--- Import Important
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature; // <--- Import Important
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tp_avancee_dev.tp_avancee.api.dto.AnnonceRequestDto;
import tp_avancee_dev.tp_avancee.model.Annonce;
import tp_avancee_dev.tp_avancee.model.Category;
import tp_avancee_dev.tp_avancee.model.User;
import tp_avancee_dev.tp_avancee.service.AnnonceService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AnnonceResourceTest extends JerseyTest {

    private AnnonceService annonceServiceMock;

    @Override
    protected Application configure() {
        annonceServiceMock = Mockito.mock(AnnonceService.class);

        return new ResourceConfig()
                .register(new AnnonceResource(annonceServiceMock)) // Notre ressource mockée
                .register(JacksonFeature.class)     // <--- Indispensable pour lire le JSON
                .register(ValidationFeature.class); // <--- Pour @Valid
    }


    @Test
    public void testGetAnnonces_Success() {
        when(annonceServiceMock.listAnnoncesPaginated(1, 10))
                .thenReturn(Collections.emptyList());

        Response response = target("/annonces").request().get();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetAnnonceById_NotFound() {
        when(annonceServiceMock.getAnnonceById(999L)).thenReturn(null);

        Response response = target("/annonces/999").request().get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testCreateAnnonce_Success() {
        AnnonceRequestDto dto = new AnnonceRequestDto();
        dto.setTitle("Vente PC");
        dto.setDescription("Super PC gamer");
        dto.setAdress("Paris");
        dto.setMail("test@test.com");
        dto.setAuthorId(1L);
        dto.setCategoryId(2L);

        Annonce createdAnnonce = new Annonce("Vente PC", "Super PC", "Paris", "mail@test.com", new User(), new Category());
        createdAnnonce.setId(10L);

        when(annonceServiceMock.createAnnonce(any(), any(), any(), any(), anyLong(), anyLong()))
                .thenReturn(createdAnnonce);

        Response response = target("/annonces")
                .request()
                .post(Entity.entity(dto, MediaType.APPLICATION_JSON));

        assertEquals(201, response.getStatus());
        assertTrue(response.getLocation().toString().contains("/annonces/10"));
    }
}