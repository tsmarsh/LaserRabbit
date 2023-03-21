package com.tailoredshapes.laser;

import com.google.gson.stream.JsonReader;
import com.tailoredshapes.stash.Stash;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

import static com.tailoredshapes.stash.Stash.stash;
import static com.tailoredshapes.underbar.IO.slurp;
import static com.tailoredshapes.underbar.UnderBar.list;
import static spark.Spark.*;

public interface Router {
    Logger log = LoggerFactory.getLogger(Router.class);

    static void route(){
        before((req, res) -> {
            String jwt = req.headers("Authorization");
            if(jwt != null) {
                String[] parts = jwt.substring(7).split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();
                var body =   Stash.parseJSON(slurp(new ByteArrayInputStream(decoder.decode(parts[1]))));
                req.attribute("user", body.get("sub"));
            }else{
                halt(401, "Unauthorized");
            }
        });

        get("/forms", "application/json", (req,res) -> {
            log.debug("allForms");
            res.type("application/json");
            return list();
        }, (result) -> "[]");
    }
}
