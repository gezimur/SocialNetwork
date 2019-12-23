package ru.gena.itmo.SocialNetwork.SocialNetwork;


public class Preparer {
    public String preparer(){
        MySource instance = MySource.getInstance();
        boolean isTryingSuccess = true;
        //===========================================================================
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS MESSAGES;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS USER_CONVERSATIONS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS USERS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS CONVERSATIONS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS USERS (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "USERNAME VARCHAR(20), " +
                        "PASSWORD VARCHAR(20), " +
                        "FIRSTNAME VARCHAR(20), " +
                        "LASTNAME VARCHAR(20));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with users"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS CONVERSATIONS (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "NAME VARCHAR(20));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with conversations"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS MESSAGES (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "CONVERSATION INTEGER, " +
                        "SENDER INTEGER, " +
                        "SENDING DATE, " +
                        "TEXT VARCHAR(20), " +
                        "FOREIGN KEY (SENDER) REFERENCES USERS (ID), " +
                        "FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS (ID));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with messages"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS USERCONVERSATIONS (" +
                        "USER INTEGER, " +
                        "CONVERSATION INTEGER, " +
                        "FOREIGN KEY (USER) REFERENCES USERS (ID), " +
                        "FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS (ID));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with user_conversation"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS VALUES (" +
                        "100000, " +
                        "'admin', " +
                        "'la-la-la', " +
                        "'not', " +
                        "'not');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO CONVERSATIONS VALUES (" +
                        "1, " +
                        "'testConversation');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values1"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERCONVERSATIONS VALUES (" +
                        "100000, " +
                        "1);");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values2"; }
        //===========================================================================
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS PATTERNSTREE;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS PATTERNS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS PATTERNS (" +
                        "ID INTEGER PRIMARY KEY, " +
                        "PATTERNSNAME VARCHAR(30), " +
                        "SITESWAP VARCHAR(30), " +
                        "DESCRIPTION VARCHAR(500));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with patterns";}
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS PATTERNSTREE (" +
                        "PATTERN INTEGER, " +
                        "DESCENDANTS INTEGER, " +
                        "FOREIGN KEY (PATTERN) REFERENCES PATTERNS (ID), " +
                        "FOREIGN KEY (DESCENDANTS) REFERENCES PATTERNS (ID));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with patternsTree"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES (" +
                        "0, " +
                        "'rootPattern', " +
                        "'', " +
                        "'');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values0 into patterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES (" +
                        "1, " +
                        "'hand to hand throw', " +
                        "'once 3', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values1 into patterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES (" +
                        "2, " +
                        "'throw up', " +
                        "'once 4', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values2 into patterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES (" +
                        "3, " +
                        "'cascade', " +
                        "'3', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values3 into patterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES (" +
                        "4, " +
                        "'441', " +
                        "'441', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values4 into patterns"; }

        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES (" +
                        "0, " +
                        "1);");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values01 into patternsTree"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES (" +
                        "0, " +
                        "2);");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values02 into patternsTree"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES (" +
                        "1, " +
                        "3);");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values13 into patternsTree"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES (" +
                        "2, " +
                        "4);");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values24 into patternsTree"; }
        return null;
    }
}
