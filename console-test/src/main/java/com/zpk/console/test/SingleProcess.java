package com.zpk.console.test;

import com.zpk.console.config.SingleAppConfig;
import com.zpk.console.config.SingleInspectConfig;
import com.zpk.console.process.ProcessUtils;
import com.zpk.zcommon.util.ZFileUtils;
import com.zpk.zcommon.util.ZStringUtils;
import com.zpk.zcommon.util.ZThreadUtils;
import org.ho.yaml.Yaml;

public class SingleProcess {

    private static boolean close = false;

    public static void test() throws Exception {
        SingleAppConfig yml = Yaml.loadType(ZFileUtils.getResourceFile("application.yml"), SingleAppConfig.class);
        System.out.println(yml);


        SingleInspectConfig inspect = yml.getInspect();

        Thread runThread = new Thread(() -> {
            while (!close) {
                boolean toRun = ProcessUtils.isAlive(inspect.getCheckAliveAppName(), inspect.getCheckAliveAppCmds());
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
                String processId = ProcessUtils.getProcessId(inspect.getProcessIdAppCmds());
                System.out.println("processId: " + processId);
                if (ZStringUtils.isNotEmpty(processId)) {
                    checkAppAlive(inspect);
                    String[] closeAppCmds = inspect.getCloseAppCmds();
                    String[] closeCmds = new String[closeAppCmds.length];
                    for (int i = 0; i < closeAppCmds.length; i++) {
                        closeCmds[i] = ZStringUtils.format(closeAppCmds[i], processId);
                    }
                    close = ProcessUtils.run(closeCmds);
                    System.out.println("close: " + close);
                }
            }
        }

    }

    private static void checkAppAlive(SingleInspectConfig inspect) {
//        boolean alive = ProcessUtils.isAlive("RemoteMavenServer", "C:\\Program Files\\Java\\jdk1.8.0_151\\bin\\jps.exe");
//        boolean alive = ProcessUtils.isAlive("RemoteMavenServer", new String[]{"jps"});
//        boolean alive = ProcessUtils.isAlive("RemoteMavenServer", new String[]{"cmd", "/c", "jps | find \"RemoteMavenServer\""});
//        boolean alive = ProcessUtils.isAlive("RemoteMavenServer", new String[]{"/bin/sh", "-c", "jps | grep \"RemoteMavenServer\""});

        System.out.println("isAlive: " + ProcessUtils.isAlive(inspect.getCheckAliveAppName(), inspect.getCheckAliveAppCmds()));
    }

    private static void runApp(SingleInspectConfig inspect) {
//        System.out.println(ProcessUtils.run(inspect.getRunAppName(), inspect.getRunAppCmds()));
        System.out.println("run: " + ProcessUtils.runAndRead(inspect.getRunAppCmds(), 20, () -> close));
    }


}
