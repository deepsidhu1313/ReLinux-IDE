package tools;

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nika
 */
public class settings {

    public boolean Settings_EXIST = false;

    public settings() {
        Tools.loadSettings();
        GlobalConstants.processExecutor = Executors.newFixedThreadPool(GlobalConstants.PROCESS_LIMIT);
        try {
            GlobalConstants.dir_appdb = "appdb";

            File f2 = new File(GlobalConstants.dir_appdb);
            if (!f2.exists()) {
                if (!f2.mkdir()) {
                    System.err.println("Directory for appdb couldnot be created !\n"
                            + "Please create a dir with this name");
                }
            }

            String workingDir = null;
            if (Tools.isWindows()) {
                System.out.println("This is Windows");
                workingDir = System.getProperty("user.dir");
                GlobalConstants.OS_Name = 0;
            } else if (Tools.isMac()) {
                System.out.println("This is Mac");
                GlobalConstants.OS_Name = 1;
            } else if (Tools.isUnix()) {
                workingDir = System.getProperty("user.dir");
                System.out.println("This is Unix or Linux");

                GlobalConstants.OS_Name = 2;
            } else if (Tools.isSolaris()) {
                System.out.println("This is Solaris");
                GlobalConstants.OS_Name = 3;
            } else {
                System.out.println("Your OS is not support!!");
                GlobalConstants.OS_Name = 4;

            }
            GlobalConstants.out = new PrintStream("appdb/out.txt");
            GlobalConstants.out.print("************" + LocalDate.now() + "******" + LocalTime.now() + "***********");
            GlobalConstants.err = new PrintStream("appdb/err.txt");
            GlobalConstants.err.print("************" + LocalDate.now() + "******" + LocalTime.now() + "***********");
            GlobalConstants.log = new PrintStream("appdb/log.txt");
            GlobalConstants.log.append("************" + LocalDate.now() + "******" + LocalTime.now() + "***********");
            

            Tools.outPrintln(GlobalConstants.OS);

            Tools.outPrintln("Current working directory : " + workingDir);
            //GlobalConstants.PWD = workingDir;
            File f = new File(workingDir);
            Tools.outPrintln("" + f.getAbsolutePath());

            
            GlobalConstants.dir_workspace = "workspace";
            File f3 = new File(workingDir + "/" + GlobalConstants.dir_workspace);
            if (!f3.exists()) {
                if (!f3.mkdir()) {
                    Tools.errPrintln("Directory for Workspace couldnot be created !\n"
                            + "Please create a dir with this name");
                }
            }
            

      
        } catch (FileNotFoundException ex) {
            Logger.getLogger(settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new settings();
    }

   
}
