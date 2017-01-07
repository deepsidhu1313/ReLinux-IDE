/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ui.SyntaxTextArea;

/**
 *
 * @author NAVDEEP SINGH SIDHU <navdeepsingh.sidhu95@gmail.com>
 */
public class GlobalConstants {

    public static String[] projectDirs = {"iso", "mnt", "extract"};
    public static String[] projectFiles = {"manifest.xml"};
    public static String selectedProjectFolder = "", lastSelectedProject = "", selectedProjectName = "";
    public static ArrayList<String> listOpenedFiles = new ArrayList<>();
    public static ArrayList<String> listOpenedProjects = new ArrayList<>();
    public static SyntaxTextArea ta[] = new SyntaxTextArea[1000];
    public static ExecutorService processExecutor = Executors.newFixedThreadPool(1);
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static int OS_Name = 0;
    public static String dir_workspace = "workspace";
    public static String dir_appdb = "appdb";
    public static String dir_scripts = "scripts";
    public static String dir_patches = "patches";
    
    public static int total_threads = 2;
    public static int process_id = 0;
    public static int PROCESS_LIMIT = Runtime.getRuntime().availableProcessors() - 1;
    public static int PROCESS_WAITING = 0;

    // public static double CPU_LOAD_AVG = 0.0;
    public static boolean verbose = true, dumplog = true;
    public static PrintStream out, err, log;
    public static PrintStream setting;

}
