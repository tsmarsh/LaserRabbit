package com.tailoredshapes.laser;

import com.tailoredshapes.laserrabbit.api.ApiClient;
import com.tailoredshapes.laserrabbit.api.ApiException;
import com.tailoredshapes.laserrabbit.api.auth.OAuth;
import com.tailoredshapes.laserrabbit.api.client.DefaultApi;
import com.tailoredshapes.laserrabbit.api.model.Form;
import com.tailoredshapes.stash.Stash;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.security.Key;
import java.util.List;
import java.util.UUID;

import static com.tailoredshapes.stash.Stash.stash;
import static com.tailoredshapes.underbar.UnderBar.list;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MainTest {

    static UUID uuid = UUID.randomUUID();
    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    static String jws = Jwts.builder().setSubject(uuid.toString()).signWith(key).compact();
    ;

    @Mock
    MetaRepository<String, String, Stash> metaRepository = mock(MetaRepository.class);

    @BeforeEach
    void before() {
        Server.start(metaRepository);
    }

    @Test
    void shouldRejectAListRequestWithoutAuth() {
        DefaultApi api = new DefaultApi();

        assertThrows(ApiException.class, () -> api.allForms());
    }

    @Test
    void shouldReturnAnEmptyList() throws Exception {
        Repository<String, Stash> repository = mock(Repository.class);

        when(metaRepository.repositoryFor(uuid.toString())).thenReturn(repository);
        when(repository.read()).thenReturn(list());

        DefaultApi api = new DefaultApi();
        ApiClient client = api.getApiClient();
        OAuth formAuth = (OAuth) client.getAuthentication("bearerAuth");
        formAuth.setAccessToken(jws);

        assertEquals(0, api.allForms().size());
    }

    @Test
    void shouldSaveAForm() throws Exception {
        Stash fakeForm = stash("firstName", "Bob", "lastName", "Bobbertson", "id", "twifty");
        Repository<String, Stash> repository = mock(Repository.class);

        when(metaRepository.repositoryFor(uuid.toString())).thenReturn(repository);
        when(repository.read()).thenReturn(list(fakeForm));
        when(repository.create(any())).thenReturn(fakeForm);
        when(repository.read("twifty")).thenReturn(fakeForm);

        DefaultApi api = new DefaultApi();
        ApiClient client = api.getApiClient();
        OAuth formAuth = (OAuth) client.getAuthentication("bearerAuth");
        formAuth.setAccessToken(jws);

        Form form = new Form();
        form.firstName("Bob");
        form.lastName("Bobbertson");

        Form result = api.createForm(form);
        assertEquals(form.getFirstName(), result.getFirstName());
        Form readResult = api.readForm(result.getId());
        assertEquals(form.getFirstName(), readResult.getFirstName());
        List<Form> allResults = api.allForms();
        assertEquals(1, allResults.size());
    }

}