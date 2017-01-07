/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tools.GlobalConstants.OS;
import static tools.GlobalConstants.OS_Name;
import static tools.GlobalConstants.PROCESS_LIMIT;
import static tools.GlobalConstants.dumplog;
import static tools.GlobalConstants.err;
import static tools.GlobalConstants.log;
import static tools.GlobalConstants.setting;
import static tools.GlobalConstants.total_threads;
import static tools.GlobalConstants.verbose;

/**
 *
 * @author nika
 */
public class Tools {

    public static boolean loadSettings() {
        File f = new File("appdb/settings.db");
        if (!f.exists()) {
            return false;
        }
        try {
            //InputStreamReader inp= new FileReader("appdb/settings.db");

            BufferedReader br = new BufferedReader(new FileReader("appdb/settings.db"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();

            total_threads = Integer.parseInt(everything.substring(everything.indexOf("<NETTRD>") + 8, everything.indexOf("</NETTRD>")));
            PROCESS_LIMIT = Integer.parseInt(everything.substring(everything.indexOf("<PROCLMT>") + 9, everything.indexOf("</PROCLMT>")));
            verbose = everything.substring(everything.indexOf("<VERBOSE>") + 9, everything.indexOf("</VERBOSE>")).contains("true");
            dumplog = everything.substring(everything.indexOf("<LOG>") + 5, everything.indexOf("</LOG>")).contains("true");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public static void saveSettings() {
        File f = new File("appdb/settings.db");
        if (!f.exists()) {
            if (!f.delete()) {
                deleteFile("appdb/settings.db");
            }
        }
        try {
            setting = new PrintStream("appdb/settings.db");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "<NETTRD>" + total_threads + "</NETTRD>" + "\n"
                + "<PROCLMT>" + PROCESS_LIMIT + "</PROCLMT>" + "\n"
                + "<VERBOSE>" + verbose + "</VERBOSE>" + "\n"
                + "<LOG>" + dumplog + "</LOG>" + "\n"
                + "";
        setting.print(sql);
        setting.close();
    }

    /*
     public static double getCPULoad() {
     OperatingSystemMXBean osMBean
     = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

     double load = osMBean.getSystemLoadAverage();

     return load;
     }
     */
    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

    }

    public static boolean isSolaris() {

        return (OS.indexOf("sunos") >= 0);

    }

    public static void outPrintln(String sout) {
        if (verbose) {
            System.out.println(sout);
        }
        if (dumplog) {
            log.append("\n" + sout);

        }
        GlobalConstants.out.append("\n" + sout);
    }

    public static void errPrintln(String sout) {
        if (verbose) {
            System.err.println(GlobalConstants.out);
        }
        if (dumplog) {
            log.append("\n" + sout);
        }
        err.append("\n" + sout);

    }

    public static boolean deleteFile(String path) {
        boolean b = false;
        try {
            ArrayList<String> command = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            if (OS_Name == 0) {
                //command.add("del");
                //command.add("/f");
                //sb.append("cd /d ");
                //sb.append(path.substring(0,path.lastIndexOf("\\")));
                sb.append("cmd /c del /f ");
            } else {
                //sb.append("cd ");
                //sb.append(path.substring(path.lastIndexOf("/")));
                sb.append("rm -vf ");
            }
            //command.add(path);
            sb.append(path);
            Runtime rt = Runtime.getRuntime();
            ProcessBuilder pb = new ProcessBuilder();

            //   pb.command(sb.toString());
            Process p = rt.exec(sb.toString());

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            Tools.outPrintln("Here is the standard output of the command:\n");

            String s = null;
            String output = "";
            int c = 0;
            while ((s = stdInput.readLine()) != null) {
                Tools.outPrintln(s);
                System.out.println(s);
                b = true;
            }

            Tools.outPrintln("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                Tools.outPrintln(s);
                b = false;

            }
            int exitValue = p.waitFor();
            Tools.outPrintln("\n\nExit Value is " + exitValue);
            p.destroy();

            return b;
        } catch (IOException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    public static void copyFolder(File src, File dest) throws IOException {

        if (src.isDirectory()) {

            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
                System.out.println("Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
            String files[] = src.list();

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile, destFile);
            }

        } else {
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes 
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            System.out.println("File copied from " + src + " to " + dest);
        }
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    public static void write(File f, String text) throws IOException {
        try (FileWriter fw = new FileWriter(f);
                PrintWriter pw = new PrintWriter(fw)) {
            pw.print(text);
            pw.close();
            fw.close();
        }

    }
}
