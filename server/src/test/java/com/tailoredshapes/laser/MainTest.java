package com.tailoredshapes.laser;

import com.tailoredshapes.laserrabbit.api.client.DefaultApi;
import com.tailoredshapes.laserrabbit.api.model.Form;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static com.tailoredshapes.underbar.Die.*;


class MainTest {

    static DefaultApi api;

    @BeforeAll
    static void beforeAll() {
        api = new DefaultApi();
        Server.start();
    }

    @Test
    void shouldReturnAnEmptyList() {
        DefaultApi api = new DefaultApi();
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