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
                .executeQuery("DROP TABLE IF EXISTS USERS_CONVERSATIONS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS USERS_PATTERNS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping users patterns"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS LEARNEDPATTERNS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping learnedPatterns"; }
        isTryingSuccess = instance
                .executeQuery("DROP TABLE IF EXISTS PATTERNSINPROCESS;");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with dropping patternsInProcess"; }
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
                        "USERSSTATUS VARCHAR(10), " +
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
                        "TEXT VARCHAR(20), " +
                        "FOREIGN KEY (SENDER) REFERENCES USERS (ID), " +
                        "FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS (ID));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with messages"; }
        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS USERS_CONVERSATIONS (" +
                        "MEMBER INTEGER, " +
                        "CONVERSATION INTEGER, " +
                        "FOREIGN KEY (MEMBER) REFERENCES USERS (ID), " +
                        "FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS (ID));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with user_conversation"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS VALUES (" +
                        "100000, " +
                        "'admin', " +
                        "'la-la-la', " +
                        "'admin', " +
                        "'not', " +
                        "'not');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS VALUES (" +
                        "100001, " +
                        "'user1', " +
                        "'1', " +
                        "'user', " +
                        "'name', " +
                        "'surname');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values 2"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS VALUES (" +
                        "100002, " +
                        "'user2', " +
                        "'1', " +
                        "'user', " +
                        "'name2', " +
                        "'surname2');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values 2"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO CONVERSATIONS VALUES (" +
                        "1, " +
                        "'testConversation');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values1"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS_CONVERSATIONS VALUES (" +
                        "100001, " +
                        "1);");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values2"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS_CONVERSATIONS VALUES (" +
                        "100002, " +
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
                        "'300', " +
                        "'just do it');");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values1 into patterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO PATTERNS VALUES (" +
                        "2, " +
                        "'throw up', " +
                        "'4000', " +
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
        //===========================================================================

        isTryingSuccess = instance
                .executeQuery("CREATE TABLE IF NOT EXISTS USERS_PATTERNS (" +
                        "JUGGLER INTEGER, " +
                        "PATTERN INTEGER, " +
                        "STATUS INTEGER, " +
                        "FOREIGN KEY (JUGGLER) REFERENCES USERS (ID), " +
                        "FOREIGN KEY (PATTERN) REFERENCES PATTERNS (ID));");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with learnedPatterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS_PATTERNS VALUES (" +
                        "100001, " +
                        "1, " +
                        "2 );");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values_100001_1_2 into users patterns"; }
        isTryingSuccess = instance
                .executeQuery("INSERT INTO USERS_PATTERNS VALUES (" +
                        "100001, " +
                        "2, " +
                        "1 );");
        if (!isTryingSuccess) { return "\n\n\n\nproblem with insert values_100001_2_1 into users patterns"; }
        return null;
    }
}
