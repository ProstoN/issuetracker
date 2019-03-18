package com.axmor;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

class CommandManager {

    private static Connection getDBConnection() {
        Connection dbConnection;
        try {
            dbConnection = DriverManager.getConnection(ListOfQuery.url, ListOfQuery.dbUser, ListOfQuery.dbPassword);
            return dbConnection;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    static Issue getIssue(String id) {
        Issue output = new Issue();
        try (Connection con = getDBConnection()) {
            PreparedStatement stmt = Objects.requireNonNull(con).prepareStatement("select * from issues where id=(?)");
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                output.setId(rs.getInt("id"));
                output.setName(rs.getString("name"));
                output.setStatus(rs.getString("status"));
                output.setAuthor(rs.getString("author"));
                output.setDescription(rs.getString("description"));
                output.setDate(rs.getString("date"));
            }
            stmt.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return output;
    }

    List<Issue> getIssues() {
        List<Issue> setColumns = new ArrayList<>();
        try (Connection con = getDBConnection()) {
            Statement stmt = Objects.requireNonNull(con).createStatement();
            ResultSet rs = stmt.executeQuery("select * from issues");
            while (rs.next()) {
                Issue issue = new Issue();
                issue.setId(rs.getInt("id"));
                issue.setName(rs.getString("name"));
                issue.setStatus(rs.getString("status"));
                issue.setAuthor(rs.getString("author"));
                issue.setDescription(rs.getString("description"));
                issue.setDate(rs.getString("date"));
                setColumns.add(issue);
            }
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return setColumns;
    }

    List<Comment> getComments(String id) {
        List<Comment> output = new ArrayList<Comment>();
        try (Connection con = getDBConnection()) {
            PreparedStatement stmt = Objects.requireNonNull(con).prepareStatement("select * from comments where issueid=(?)");
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment tempComm = new Comment();
                tempComm.setId(rs.getInt("id"));
                tempComm.setIssueId(rs.getInt("issueid"));
                tempComm.setText(rs.getString("text"));
                tempComm.setDate(rs.getString("date"));
                tempComm.setAuthor(rs.getString("author"));
                output.add(tempComm);
            }
            stmt.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return output;
    }

    void addComment(String status, String author, String comment, String issueId) {
        try (Connection con = getDBConnection()) {
            PreparedStatement stmt = Objects.requireNonNull(con)
                    .prepareStatement("insert into comments (author,text, issueid, date) values (?,?,?,?)");
            stmt.setString(1, author);
            stmt.setString(2, comment);
            stmt.setInt(3, Integer.parseInt(issueId));
            stmt.setString(4, new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
            stmt.execute();
            stmt.close();
            updateIssueStat(status, Integer.parseInt(issueId));
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    private void updateIssueStat(String status, int id) {
        try (Connection con = getDBConnection()) {
            PreparedStatement stmt = Objects.requireNonNull(con).prepareStatement("update issues set status=(?) where id=(?)");
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    void createIssue(String name, String comment, String status, String author) {
        try (Connection con = getDBConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "insert into issues (name, author, description, status, date) values (?,?,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, author);
            stmt.setString(3, comment);
            stmt.setString(4, status);
            stmt.setString(5, new SimpleDateFormat("DD.MM.YYYY").format(Calendar.getInstance().getTime()));
            stmt.execute();
            stmt.close();
        } catch (SQLException sqlEx) {
           sqlEx.printStackTrace();
        }

    }

}
