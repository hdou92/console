package com.zpk.console;

import org.junit.Test;

import java.io.File;

public class Tests {

    @Test
    public void test() {
        File file = new File("/var/properties");
        System.out.println(file.getAbsolutePath());
    }

}
