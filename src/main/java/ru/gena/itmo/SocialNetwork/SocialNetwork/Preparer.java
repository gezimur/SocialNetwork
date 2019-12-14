package ru.gena.itmo.SocialNetwork.SocialNetwork;


public class Preparer {
    public String preparer(){
        MySource instance = MySource.getInstance();
        boolean isTryingSuccess = true;
        /*isTryingSuccess = instance
                .executeQuery("DROP TABLE USERS;");
        if (!isTryingSuccess) { return "problem with dropping users"; }*/
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS USERS(" +
                "ID INTEGER PRIMARY KEY, " +
                "USERNAME VARCHAR(20), " +
                "PASSWORD VARCHAR(20), " +
                "FIRSTNAME VARCHAR(20), " +
                "LASTNAME VARCHAR(20));");
        if (!isTryingSuccess) { return "problem with users"; }
        /*isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS VALUES(" +
                        "100000, " +
                        "'admin', " +
                        "'la-la-la', " +
                        "'not', " +
                        "'not');");
        if (!isTryingSuccess) { return "problem with insert values"; }*/
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS CONVERSATIONS(" +
                        "ID INTEGER PRIMARY KEY, " +
                        "NAME VARCHAR(20));");
        if (!isTryingSuccess) { return "problem with conversations"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS USER_CONVERSATIONS(" +
                        "USER INTEGER, " +
                        "CONVERSATION INTEGER, " +
                        "CONSTRAINT FK_USER FOREIGN KEY (USER) REFERENCES USERS (ID), " +
                        "CONSTRAINT FK_CONV FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS (ID));");
        if (!isTryingSuccess) { return "problem with user_conversation"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS MESSAGES(" +
                        "ID INTEGER PRIMARY KEY, " +
                        "CONVERSATION INTEGER, " +
                        "SENDER INTEGER, " +
                        "SENDING DATE, " +
                        "TEXT VARCHAR(20), " +
                        "CONSTRAINT FK_SENDER FOREIGN KEY (SENDER) REFERENCES USERS (ID), " +
                        "CONSTRAINT FK_CONVER FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS (ID));");
        if (!isTryingSuccess) { return "problem with messages"; }

        isTryingSuccess = instance
                .executeQuery("DROP TABLE PATTERNSTREE;");
        if (!isTryingSuccess) { return "problem with dropping patternsTree"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE PATTERNS;");
        if (!isTryingSuccess) { return "problem with dropping patterns"; }

        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS PATTERNS(" +
                        "ID INTEGER PRIMARY KEY, " +
                        "PATTERNSNAME VARCHAR(30), " +
                        "SITESWAP VARCHAR(30), " +
                        "DESCRIPTION VARCHAR(500));");
        if (!isTryingSuccess) { return "problem with patterns";}
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS PATTERNSTREE(" +
                        "PATTERN INTEGER, " +
                        "DESCENDANTS INTEGER, " +
                        "CONSTRAINT FK_PATTERN FOREIGN KEY (PATTERN) REFERENCES PATTERNS (ID), " +
                        "CONSTRAINT FK_DESCENDANT FOREIGN KEY (DESCENDANTS) REFERENCES PATTERNS (ID));");
        if (!isTryingSuccess) { return "problem with patternsTree"; }

        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES(" +
                        "0, " +
                        "'', " +
                        "'', " +
                        "'');");
        if (!isTryingSuccess) { return "problem with insert values1"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES(" +
                        "1, " +
                        "'hand to hand throw', " +
                        "'once 3', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "problem with insert values1"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES(" +
                        "2, " +
                        "'cascade', " +
                        "'3', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "problem with insert values2"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES(" +
                        "3, " +
                        "'up throw', " +
                        "'once 4', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "problem with insert values3"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES(" +
                        "0, " +
                        "1);");
        if (!isTryingSuccess) { return "problem with insert values4"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES(" +
                        "0, " +
                        "3);");
        if (!isTryingSuccess) { return "problem with insert values5"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNSTREE VALUES(" +
                        "1, " +
                        "2);");
        if (!isTryingSuccess) { return "problem with insert values6"; }

        return null;
    }
}
