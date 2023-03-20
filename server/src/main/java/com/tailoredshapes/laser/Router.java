package com.tailoredshapes.laser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.tailoredshapes.underbar.UnderBar.list;
import static spark.Spark.get;

public interface Router {
    Logger log = LoggerFactory.getLogger(Router.class);

    static void route(){
        get("/forms", "application/json", (req,res) -> {
            log.debug("allForms");
            res.type("application/json");
            return list();
        }, (result) -> "[]");
    }
}
