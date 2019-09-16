package com.zpk.console.process;

import com.zpk.zcommon.util.ZStringUtils;
import com.zpk.zcommon.util.ZThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.function.Supplier;

public class ProcessUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtils.class);

    public static boolean isAlive(String procName, List<String> cmd) {
        return isAlive(procName, cmd.toArray(new String[cmd.size()]));
    }

    /**
     * 判断程序是否在运行，通过命令行
     */
    public static boolean isAlive(String procName, String[] cmd) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        BufferedReader br = null;
        try {
            p = r.exec(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(procName)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            LOGGER.warn(ZStringUtils.exceptionToString(e));
            return false;
        } finally {
            closeProcess(p, br, true);
        }
    }


    public static boolean runAndRead(List<String> cmd, int waitTime, Supplier<Boolean> isClose) {
        return runAndRead(cmd.toArray(new String[cmd.size()]), waitTime, isClose);
    }

    /**
     * 程序运行，并且读取程序的输出
     */
    public static boolean runAndRead(String[] cmd, int waitTime, Supplier<Boolean> isClose) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        BufferedReader br = null;
        try {
            p = r.exec(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while (!isClose.get()) {
                if ((line = br.readLine()) != null) {
                    LOGGER.info(line);
                } else {
                    ZThreadUtils.sleep(waitTime);
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.warn(ZStringUtils.exceptionToString(e));
            return false;
        } finally {
            closeProcess(p, br, true);
        }
    }

    public static String getProcessId(List<String> cmd) {
        return getProcessId(cmd.toArray(new String[cmd.size()]));
    }

    /**
     * 获取进程id
     */
    public static String getProcessId(String[] cmd) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        BufferedReader br = null;
        try {
            p = r.exec(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                return ZStringUtils.split(line)[0];
            }
        } catch (IOException e) {
            LOGGER.warn(ZStringUtils.exceptionToString(e));
        } finally {
            closeProcess(p, br, true);
        }
        return null;
    }

    /**
     * 运行进程
     */
    public static boolean run(String[] cmd) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        BufferedReader br = null;
        try {
            p = r.exec(cmd);
            return true;
        } catch (IOException e) {
            LOGGER.warn(ZStringUtils.exceptionToString(e));
        } finally {
            closeProcess(p, br, true);
        }
        return false;
    }

    private static void closeProcess(Process p, BufferedReader br, boolean wait) {
        try {
            if (p != null) {
                if (br != null) {
                    br.close();
                }
                p.getOutputStream().close();
                p.getInputStream().close();
                p.getErrorStream().close();
                if (wait) {
                    p.waitFor();
                }
                p.destroy();
            }
        } catch (Exception e) {
            LOGGER.warn(ZStringUtils.exceptionToString(e));
        }
    }

}
