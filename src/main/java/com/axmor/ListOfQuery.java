package com.axmor;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.*;

class ListOfQuery {
    static String dbUser = "";
    static String dbPassword = "";
    static String url = "";
    private static String database = "";
    private static String urlCreate = "";
    private static Connection con;
    private static boolean isCreate;
    private static boolean isLocalhost;

    static void initializeConnectionVariables(String user, String password, String name, String host){
        dbUser = user;
        dbPassword = password;
        database = name;
        if (host.equals("localhost:3306")){
            urlCreate = "jdbc:mysql://" +
                    host + "/" +
                    "?user=" + dbUser +
                    "&password=" + dbPassword +
                    "&verifyServerCertificate=false" +
                    "&useSSL=false" +
                    "&requireSSL=false" +
                    "&useLegacyDatetimeCode=false" +
                    "&amp" +
                    "&serverTimezone=UTC" +
                    "&allowPublicKeyRetrieval=true";
            isLocalhost = true;
        } else if (host.equals("sql7.freemysqlhosting.net:3306")) {
            urlCreate = "jdbc:mysql://" +
                    host + "/" +
                    database +
                    "?verifyServerCertificate=false" +
                    "&useSSL=false" +
                    "&requireSSL=false" +
                    "&useLegacyDatetimeCode=false" +
                    "&amp" +
                    "&serverTimezone=UTC" +
                    "&allowPublicKeyRetrieval=true";
            isLocalhost = false;
        }
        url = "jdbc:mysql://" +
                host + "/" +
                database +
                "?verifyServerCertificate=false" +
                "&useSSL=false" +
                "&requireSSL=false" +
                "&useLegacyDatetimeCode=false" +
                "&amp" +
                "&serverTimezone=UTC" +
                "&allowPublicKeyRetrieval=true";
    }

    static void isNeedToCreate(boolean isNeeded) {
        isCreate = isNeeded;
    }

    private static String dropTable(String table) {
        return "DROP TABLE IF EXISTS `" + table + "`";
    }

    private static String dropDatabase(){
        if (isLocalhost) {
            return "DROP DATABASE IF EXISTS `" + database + "`";
        }
        return "DROP TABLE IF EXISTS `users`, `issues`, `comments`";
    }

    private static void createDataBase(Connection con){
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(dropDatabase());
            if (isLocalhost){
                statement.executeUpdate("CREATE DATABASE `" + database + "`");
            }
        } catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        }

    }

    private static String md5Apache(String st) {
        return DigestUtils.md5Hex(st);
    }

    static boolean userToDataBase(String login, String password) {
        String md5Password = md5Apache(password);
        boolean isExist = false;
        try {
            con = DriverManager.getConnection(url, dbUser, dbPassword);
            if (isCreate) {
                if (isLocalhost){
                    createDataBase(DriverManager.getConnection(urlCreate));
                } else {
                    createDataBase(con);
                }
                createUserTable(con);
                createIssueTable(con);
                createCommentTable(con);
            }
            if (checkLoginExist(con, login)) {
                if (checkUserExist(con, login, md5Password)) {
                    isExist = true;
                }
            } else {
                createNewUser(con, login, md5Password);
                isExist = true;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return isExist;
    }

    private static void createTables(Connection con, String query, String name) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(dropTable(name));
            stmt.executeUpdate(query);

        } catch (SQLException sqlEx) {
            System.out.println("Create table error");
            sqlEx.printStackTrace();
        }
    }

    private static int verificationProcessing(Connection con, String query, String login, String password) {
        PreparedStatement preparedStatement;
        String typeOfQuery = StringUtils.substring(query, 0, 6);
        int response = 0;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, login);
            if (password != null) {
                preparedStatement.setString(2, password);
            }
            if (typeOfQuery.equals("SELECT")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    response = rs.getInt(1);
                }
            } else if (typeOfQuery.equals("INSERT")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlEx) {
            System.out.println("SQLException");
            sqlEx.printStackTrace();
        }
        return response;
    }

    private static boolean checkLoginExist(Connection con, String login) {
        boolean isUserExists;
        String selectQuery = "SELECT EXISTS(select login from users where login = ?)";
        isUserExists = verificationProcessing(con, selectQuery, login, null) == 1;
        return isUserExists;
    }

    private static boolean checkUserExist(Connection con, String login, String password) {
        boolean isUserExists;
        String selectQuery = "SELECT EXISTS(select login, password from users where ((login = ?) and (password = ?)))";
        isUserExists = verificationProcessing(con, selectQuery, login, password) == 1;
        return isUserExists;
    }

    private static void createNewUser(Connection con, String login, String password) {
        String insertQuery = "INSERT INTO " + database + ".users (login, password) values (?,?)";
        verificationProcessing(con, insertQuery, login, password);
    }

    private static void createUserTable(Connection con) {
        String name = "users";
        String createQuery = "CREATE TABLE `" + name + "` (\n" +
                "   `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "   `login` varchar(50) NOT NULL,\n" +
                "   `password` varchar(50) NOT NULL,\n" +
                "   PRIMARY KEY (`id`))";
        createTables(con, createQuery, name);
    }

    private static void createIssueTable(Connection con) {
        String name = "issues";
        String createQuery = "CREATE TABLE `" + name + "` (\n" +
                "`id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "`name` varchar(255) NOT NULL,\n" +
                "`author` varchar(255) NOT NULL,\n" +
                "`description` varchar(255) NOT NULL,\n" +
                "`status` varchar(255) NOT NULL,\n" +
                "`date` varchar(255) NOT NULL, \n" +
                "PRIMARY KEY (`id`))";
        createTables(con, createQuery, name);
    }

    private static void createCommentTable(Connection con) {
        String name = "comments";
        String createQuery = "CREATE TABLE `" + name + "` (\n" +
                "`id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "`text` varchar(255) NOT NULL,\n" +
                "`issueid` int(11) NOT NULL,\n" +
                "`date` varchar(255) NOT NULL,\n" +
                "`author` varchar(255) NOT NULL,\n" +
                "PRIMARY KEY (`id`))";
        createTables(con, createQuery, name);
    }
}
