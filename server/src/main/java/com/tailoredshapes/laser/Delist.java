package com.tailoredshapes.laser;

import com.tailoredshapes.stash.Stash;
import spark.ResponseTransformer;

import java.util.List;

import static com.tailoredshapes.underbar.UnderBar.map;
import static com.tailoredshapes.underbar.UnderString.commaSep;
import static spark.Spark.halt;

public class Delist implements ResponseTransformer {
    @Override
    public String render(Object model) throws Exception {
        if (model instanceof List) {
            List<Object> l = (List<Object>) model;
            List<String> rs = map(l, (Object o) -> {
                String r;
                if (o instanceof Stash) {
                    r = ((Stash) o).toJSONString();
                } else {
                    halt(500, "Server Error");
                    r = ("{}");
                }
                return r;
            });

            return String.format("[%s]", commaSep(rs));
        } else {
            halt(404);
            return "{}";
        }
    }
}
