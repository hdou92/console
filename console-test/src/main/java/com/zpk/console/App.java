package com.zpk.console;

import com.zpk.console.config.AppConfig;
import com.zpk.console.process.ProcessUtils;
import com.zpk.console.test.MultiProcess;
import com.zpk.zcommon.util.ZFileUtils;
import org.ho.yaml.Yaml;

public class App {

    public static void main(String[] args) throws Exception {
//        MultiProcess.test();
//        boolean alive = ProcessUtils.isAlive("spring-boot-test-1.0.jar", new String[]{"cmd /c jps | find \"spring-boot-test-1.0.jar\""});
//        boolean alive = ProcessUtils.isAlive("spring-boot-test-1.0.jar", new String[]{"cmd /c jps | find spring-boot-test-1.0.jar"});
//        boolean alive = ProcessUtils.isAlive("spring-boot-test-1.0.jar", new String[]{"cmd", "/c", "jps |find spring-boot-test-1.0.jar"});


        AppConfig yml = Yaml.loadType(ZFileUtils.getResourceFile("application.yml"), AppConfig.class);
        System.out.println(yml);

    }

}
