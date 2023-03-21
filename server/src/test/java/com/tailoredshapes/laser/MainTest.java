package com.tailoredshapes.laser;

import com.tailoredshapes.laserrabbit.api.ApiClient;
import com.tailoredshapes.laserrabbit.api.ApiException;
import com.tailoredshapes.laserrabbit.api.Configuration;
import com.tailoredshapes.laserrabbit.api.auth.Authentication;
import com.tailoredshapes.laserrabbit.api.auth.OAuth;
import com.tailoredshapes.laserrabbit.api.client.DefaultApi;
import com.tailoredshapes.laserrabbit.api.model.Form;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.function.Executable;

import java.security.Key;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static com.tailoredshapes.underbar.Die.*;


class MainTest {

    static String jws;

    @BeforeAll
    static void beforeAll() {
        Server.start();

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
    }

    @Test
    void shouldRejectAListRequestWithoutAuth(){
        DefaultApi api = new DefaultApi();

        assertThrows(ApiException.class, () -> api.allForms());
    }

    @Test
    void shouldReturnAnEmptyList() {
        DefaultApi api = new DefaultApi();
        ApiClient client = api.getApiClient();
        OAuth formAuth = (OAuth) client.getAuthentication("bearerAuth");
        formAuth.setAccessToken(jws);

        assertEquals(0, rethrow(api::allForms).size());
    }

//    @Test
//    void shouldSaveAForm() {
//        Form form = new Form();
//        form.firstName("Bob");
//        form.lastName("Bobbertson");
//
//        Form result = rethrow(() -> api.createForm(form));
//        Form readResult = rethrow(() -> api.readForm(result.getId()));
//        List<Form> allResults = rethrow(api::allForms);
//    }

}