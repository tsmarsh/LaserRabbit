package com.tailoredshapes.laser;

import com.tailoredshapes.stash.Stash;
import spark.ResponseTransformer;

import static spark.Spark.halt;

public class Destash implements ResponseTransformer {
    @Override
    public String render(Object result) throws Exception {
        if (result instanceof Stash) {
            return ((Stash) result).toJSONString();
        } else {
            halt(404);
            return "{}";
        }
    }
}
