/*
 * Copyright (c) 2016 ChargeBee Inc
 * All Rights Reserved.
 */
package com.chargebee.util;

/**
 *
 * @author vaibhav
 */
public class Utils {

    public static String csv(Object... vals) {
        return strJoin(", ", vals);
    }

    public static String strJoin(String delim, Object... vals) {
        StringJoiner buf = new StringJoiner(delim, "NULL");
        for (Object val : vals) {
            buf.add(val);
        }
        return buf.toString();
    }

}
