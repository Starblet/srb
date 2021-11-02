package com.starblet;

/**
 * @author starblet
 * @create 2021-09-10 13:25
 */
public class Test {

    @org.junit.Test
    public void test() {
        String a = "23!n";
//        boolean matches = a.matches("^([0-9]*!n).*");
//        System.out.println(matches);

        int i = a.indexOf("!");
        System.out.println(i);
        String ddlValue = a.substring(0, i + 2);
        System.out.println(ddlValue);
    }
}
