package com.zpk.console.test;

import com.zpk.console.config.AppConfig;
import com.zpk.console.config.InspectAppConfig;
import com.zpk.console.process.ProcessUtils;
import com.zpk.zcommon.util.ZFileUtils;
import com.zpk.zcommon.util.ZStringUtils;
import com.zpk.zcommon.util.ZThreadUtils;
import org.ho.yaml.Yaml;

import java.util.List;

public class MultiProcess {

    private static boolean close = false;

    public static void test() throws Exception {
        AppConfig yml = Yaml.loadType(ZFileUtils.getResourceFile("application.yml"), AppConfig.class);
        System.out.println(yml);


        InspectAppConfig inspect = yml.getInspect();

        Thread runThread = new Thread(() -> {
            while (!close) {
                boolean toRun = ProcessUtils.isAlive(inspect.getApps().get(0).getPackageName(),
                        inspect.getApps().get(0).getCheckAliveAppCmds());
                try {
                    if (!toRun) {
                        runApp(inspect);
                    }
                } catch (Exception e) {
                    toRun = false;
                    e.printStackTrace();
                }
                ZThreadUtils.sleep(1000);
            }
        });
        runThread.setDaemon(false);
        runThread.start();

        int ival = 0;
//        while (!close) {
        while (true) {
            ZThreadUtils.sleep(1000);
            if (ival++ >= 10) {
                String processId = ProcessUtils.getProcessId(inspect.getApps().get(0).getProcessIdAppCmds());
                System.out.println("processId: " + processId);
                if (ZStringUtils.isNotEmpty(processId)) {
                    checkAppAlive(inspect);
                    List<String> closeAppCmds = inspect.getApps().get(0).getCloseAppCmds();
                    String[] closeCmds = new String[closeAppCmds.size()];
                    for (int i = 0; i < closeAppCmds.size(); i++) {
                        closeCmds[i] = ZStringUtils.format(closeAppCmds.get(i), processId);
                    }
                    close = ProcessUtils.run(closeCmds);
                    System.out.println("close: " + close);
                }
            }
        }

    }

    private static void checkAppAlive(InspectAppConfig inspect) {
        System.out.println("isAlive: " + ProcessUtils.isAlive(inspect.getApps().get(0).getPackageName(), inspect.getApps().get(0).getCheckAliveAppCmds()));
    }

    private static void runApp(InspectAppConfig inspect) {
        System.out.println("run: " + ProcessUtils.runAndRead(inspect.getApps().get(0).getRunAppCmds(), 20, () -> close));
    }


}
