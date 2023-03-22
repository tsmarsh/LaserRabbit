package com.tailoredshapes.laser;

import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        var port = Integer.parseInt(System.getenv().containsKey("port") ? System.getenv("port") : "6969");

        port(port);
        Router.route(new GitRepository());
        System.out.println("*****PORT: " + port);
    }
}
