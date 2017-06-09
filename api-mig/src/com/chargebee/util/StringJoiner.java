/*
 * Copyright (c) 2016 ChargeBee Inc
 * All Rights Reserved.
 */
package com.chargebee.util;

/**
 *
 * @author vaibhav
 */
public class StringJoiner {

    private String delim;
    private String defaultValue;
    private StringBuilder buff = new StringBuilder();

    public StringJoiner(String delim) {
        this(delim, null);
    }

    public StringJoiner(String delim, String defaultValue) {
        this.delim = delim;
        this.defaultValue = defaultValue;
    }

    public boolean isEmpty() {
        return buff.length() == 0;
    }

    public int length() {
        return buff.length();
    }

    public StringJoiner add(Object obj) {
        return obj != null ? add(obj.toString()) : add((String) null);
    }

    public StringJoiner add(String element) {
        if (buff.length() != 0) { // not the first-time
            buff.append(delim);
        }
        if (element == null || element.isEmpty()) { // empty
            if (defaultValue == null) {
                throw new IllegalArgumentException("cannot add null element");
            }
            buff.append(defaultValue);
        } else {
            buff.append(element);
        }
        return this;
    }

    public void wrap(char start, char end) {
        buff.insert(0, start);
        buff.insert(buff.length(), end);
    }

    @Override
    public String toString() {
        return buff.toString();
    }

    public static void main(String[] args) {
        StringJoiner sj = new StringJoiner(" ,", "test");
        System.out.println(sj.add("").add("1"));
    }

}
