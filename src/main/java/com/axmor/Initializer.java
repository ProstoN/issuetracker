package com.axmor;

import java.security.InvalidParameterException;

class Initializer {
    void initialize(String[] args) {
        if (args.length < 5) {
            throw new InvalidParameterException("You must enter at least 5 arguments.");
        }
        String isNeedToCreateDatabase = args[0];
        String dbUser = args[1].substring(1);
        String dbPassword = args[2].substring(1);
        String database = args[3].substring(1);
        String dbHost = args[4].substring(1);
        boolean isNeeded = false;

        switch (dbHost) {
            case "localhost":
                dbHost += ":3306";
                break;
            case "sql7.freemysqlhosting.net":
                dbHost += ":3306";
                break;
            default:
                throw new InvalidParameterException("Incorrect database name must be -localhost or -sql7.freemysqlhosting.net");
        }

        switch (isNeedToCreateDatabase) {
            case "-yes":
                isNeeded = true;
                break;
            case "-no":
                break;
            default:
                throw new InvalidParameterException("Error entering data type. Valid: '-yes' - create, '-no' - do not create");
        }

        ListOfQuery.initializeConnectionVariables(dbUser, dbPassword, database, dbHost);
        WebConfig.startToWork(isNeeded);

    }
}
