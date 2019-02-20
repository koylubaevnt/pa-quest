package com.pa.march.paquestserver.repository;

public class SearchOperation {

    public static final String GREATER_THAN = ">";
    public static final String LESS_THAN    = "<";
    public static final String EQUALITY     = ":";
    public static final String NEGATION     = "!";
    public static final String LIKE         = "~";

    public static final String AND = ",";
    public static final String OR  = ";";

    public static String getPatternString() {
        return "(\\w+?)"
                + "(" + SearchOperation.EQUALITY     + "|"
                + SearchOperation.LESS_THAN    + "|"
                + SearchOperation.GREATER_THAN + "|"
                + SearchOperation.LIKE         + "|"
                + SearchOperation.NEGATION + ")"
                + "(.+?)"
                + "(" + SearchOperation.AND + "|"
                + SearchOperation.OR  + "|"
                + "$" + ")";
    }

}
