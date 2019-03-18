package com.axmor;

import spark.Request;
import spark.Response;

class Registration {
    static boolean userIsSignIn(Request request, Response response) {
        if (request.session().attribute("currentUser") == null) {
            response.redirect("/");
            return false;
        } else {
            return true;
        }
    }

    static void signUp(Request req, Response res) {
        String login = req.queryParams("user");
        String password = req.queryParams("password");

        if (!login.isEmpty() && !password.isEmpty()) {
            checkUser(req, res, login, password);
        } else {
            res.redirect("/");
        }
    }

    private static void checkUser(Request req, Response res, String login, String password) {
        if (ListOfQuery.userToDataBase(login, password)) {
            req.session().attribute("currentUser", req.queryParams("user"));
            res.redirect("/welcome");
        } else {
            res.redirect("/");
        }
    }
}
