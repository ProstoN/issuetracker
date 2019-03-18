package com.axmor;

import com.google.gson.Gson;

import java.util.List;

import static spark.Spark.*;


class WebConfig {
    private static boolean isSignUp = false;

    static void startToWork(boolean isNeed) {
        ListOfQuery.isNeedToCreate(isNeed);

        port(80);
        get("/", (request, response) -> {
            if (!isSignUp) {
                return Main.class.getClassLoader().getResourceAsStream("login.html");
            } else {
                response.redirect("/welcome");
                return null;
            }
        });

        get("/logout", (request, response) -> {
            isSignUp = false;
            response.redirect("/");
            return null;
        });

        get("/welcome", (request, response) -> {
            if (Registration.userIsSignIn(request, response)){
                isSignUp = true;
                return Main.class.getClassLoader().getResourceAsStream("index.html");
            } else {
                response.redirect("/");
                return null;
            }

        });

        post("/login", (request, response) -> {
            Registration.signUp(request, response);
            return null;
        });
        get("/issues", (request, response) -> {
            Registration.userIsSignIn(request, response);
            CommandManager im = new CommandManager();
            im.getIssues();
            return Main.class.getClassLoader().getResourceAsStream("issues.html");
        });

        get("/getIssue", (request, response) -> {
            Registration.userIsSignIn(request, response);
            Gson gson = new Gson();
            return gson.toJson(CommandManager.getIssue(request.headers("referer").split("=")[1]));
        });

        get("/getIssues", "application/x-www-form-urlencoded", (request, response) -> {
            Registration.userIsSignIn(request, response);
            CommandManager im = new CommandManager();
            List<Issue> issues = im.getIssues();
            Gson gson = new Gson();
            return gson.toJson(issues);
        });

        get("/getComments", "application/x-www-form-urlencoded", (request, response) -> {
            Registration.userIsSignIn(request, response);
            CommandManager im = new CommandManager();
            List<Comment> comments = im.getComments(request.headers("referer").split("=")[1]);
            Gson gson = new Gson();
            return gson.toJson(comments);
        });

        get("/edit", (request, response) -> {
            Registration.userIsSignIn(request, response);
            return Main.class.getClassLoader().getResourceAsStream("edit.html");
        });

        get("/create", (request, response) -> {
            Registration.userIsSignIn(request, response);
            return Main.class.getClassLoader().getResourceAsStream("create.html");
        });

        post("/createIssue", (request, response) -> {
            CommandManager im = new CommandManager();
            im.createIssue(request.queryParams("name"), request.queryParams("comment"), request.queryParams("status"),
                    request.queryParams("author"));
            response.redirect("/issues");
            return null;
        });

        post("/addComment", (request, response) -> {
            Registration.userIsSignIn(request, response);
            CommandManager im = new CommandManager();
            im.addComment(request.queryParams("statusComment"), request.queryParams("authorComment"), request.queryParams("comment"),
                    request.headers("referer").split("=")[1]);
            response.redirect(request.headers("referer"));
            return null;
        });
    }
}
