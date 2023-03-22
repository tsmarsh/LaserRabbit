package com.tailoredshapes.laser;

import com.tailoredshapes.stash.Stash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Route;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.function.Function;

import static com.tailoredshapes.underbar.IO.slurp;
import static spark.Spark.*;

public interface Router {
    Logger log = LoggerFactory.getLogger(Router.class);

    static String reqToId(Request req) {
        return req.attribute("user");
    }

    static Route allForms(MetaRepository<String, String, Stash> rf, Function<Request, String> ider) {
        return (req, res) -> {
            Repository<String, Stash> repo = rf.repositoryFor(ider.apply(req));
            res.type("application/json");
            return repo.read();
        };
    }

    static Route createForm(MetaRepository<String, String, Stash> rf, Function<Request, String> ider) {
        return (req, res) -> {
            Repository<String, Stash> repo = rf.repositoryFor(ider.apply(req));
            Stash form = Stash.parseJSON(req.body());
            res.type("application/json");
            return repo.create(form);
        };
    }

    static Route readForm(MetaRepository<String, String, Stash> rf, Function<Request, String> ider) {
        return (req, res) -> {
            Repository<String, Stash> repo = rf.repositoryFor(ider.apply(req));
            res.type("application/json");
            return repo.read(req.params(":id"));
        };
    }

    static void route(MetaRepository<String, String, Stash> rf) {
        Delist delist = new Delist();
        Destash destash = new Destash();

        before((req, res) -> {
            String jwt = req.headers("Authorization");
            if (jwt != null) {
                String[] parts = jwt.substring(7).split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();
                var body = Stash.parseJSON(slurp(new ByteArrayInputStream(decoder.decode(parts[1]))));
                req.attribute("user", body.get("sub"));
            } else {
                halt(401, "Unauthorized");
            }
        });

        get("/forms", "application/json", allForms(rf, Router::reqToId), delist);

        post("/forms", "application/json", createForm(rf, Router::reqToId), destash);

        get("/form/:id", "application/json", readForm(rf, Router::reqToId), destash);
    }
}
