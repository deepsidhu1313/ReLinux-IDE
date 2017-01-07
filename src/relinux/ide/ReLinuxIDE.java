/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relinux.ide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import tools.GlobalConstants;
import static tools.Tools.write;
import ui.CustomTree;
import ui.FilesTree;
import ui.SyntaxTextArea;

/**
 *
 * @author NAVDEEP SINGH SIDHU <navdeepsingh.sidhu95@gmail.com>
 */
public class ReLinuxIDE extends Application implements Runnable {

    static CustomTree AllNodes = new CustomTree();
    public static Tab tab21, tab22, tab23, tab24;
    static Tab filesTab = new Tab("Files");
    static TabPane centerTabPane;
    public static Tab textTab[] = new Tab[100];
    public static ProgressIndicator pi = new ProgressBar();

    // public static TreeView tree = new TreeView();
    //public static CheckBoxTreeItem[] root = new CheckBoxTreeItem[1000];
    public static boolean ideStarted = false;
    public static int tabcounter = 0;
    public static BorderPane bp;
    public static HBox statusBar;

    Stage stage = new Stage();
    public static SplitPane LeftSplitPane;
    public static int selectedtab = 0;
    public static Tab outputTab[] = new Tab[1000];
    public static ListView outputTabTextArea[] = new ListView[1000];
    public static ObservableList<String> outputTabText[] = new ObservableList[1000];
    public static TextArea consoleArea = new TextArea();
    public static TabPane bottomTabPane;

    @Override
    public void start(final Stage stage) throws Exception {
        loadOpenProjects();
        this.stage = stage;
        this.stage.setTitle("Relinux-IDE");
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

//VBox vb1= new VBox();
        //GridPane vb1 = new GridPane();
        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");

        MenuItem newProject = new MenuItem("\tNew Project\t\t");
        newProject.setOnAction(new EventHandler() {
            public void handle(Event t) {
                doNewProject();
            }
        });
        menuFile.getItems().add(newProject);

        MenuItem newFile = new MenuItem("\tNew File\t\t");
        newFile.setOnAction(new EventHandler() {
            public void handle(Event t) {
                doNew();
            }
        });
        menuFile.getItems().add(newFile);

        MenuItem openProject = new MenuItem("\tOpen Project\t\t");
        openProject.setOnAction(new EventHandler() {
            public void handle(Event t) {
                openProject();
            }
        });
        menuFile.getItems().add(openProject);

        MenuItem open = new MenuItem("\tOpen\t\t");
        open.setOnAction(new EventHandler() {
            public void handle(Event t) {
                doOpen();
            }
        });
        menuFile.getItems().add(open);

        MenuItem sync = new MenuItem("\tSync\t\t");
        sync.setOnAction(new EventHandler() {
            public void handle(Event t) {

                synchroniseUi();
            }
        });
        menuFile.getItems().add(sync);

        MenuItem save = new MenuItem("\tSave\t\t");
        save.setOnAction(new EventHandler() {
            public void handle(Event t) {
                doSave();
            }
        });
        menuFile.getItems().add(save);

        MenuItem saveAs = new MenuItem("\tSave As\t\t");
        saveAs.setOnAction(new EventHandler() {
            public void handle(Event t) {
                doSaveAs();
            }
        });
        menuFile.getItems().add(saveAs);

        MenuItem exit = new MenuItem("\tExit\t\t");
        exit.setOnAction(new EventHandler() {
            public void handle(Event t) {
                doExit(t);

            }
        });
        menuFile.getItems().add(exit);

// --- Menu Edit
        Menu menuEdit = new Menu("Edit");

        Menu menuRun = new Menu("Run");
        MenuItem itemParse = new MenuItem("Parse");
        itemParse.setOnAction(new EventHandler() {
            public void handle(Event t) {
                //   parse();
            }
        });
        MenuItem itemExec = new MenuItem("Build");
        itemExec.setOnAction(new EventHandler() {
            public void handle(Event t) {
                // execute(selectedtab);
            }
        });
        menuRun.getItems().addAll(itemParse, itemExec);
        Menu menuServer = new Menu("Server");
        MenuItem startServer = new MenuItem("Start Server");
        startServer.setOnAction(new EventHandler() {
            public void handle(Event t) {
            }
        });

        MenuItem stopServer = new MenuItem("StopServer");
        stopServer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
            }
        });
        menuServer.getItems().addAll(startServer, stopServer);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuRun, menuServer);
        //Setup Center and Right
        // TabPaneWrapper wrapper = new TabPaneWrapper(Orientation.HORIZONTAL, .9);
        centerTabPane = new TabPane();

//        Tab tab11 = new Tab("Start Tab");
//        tab11.setContent(new TextArea("\t"));
//        tab11.setId("7675586");
//        tab11.setTooltip(new Tooltip("Start Tab"));
//        centerTabPane.getTabs().addAll(tab11);
        this.stage.setTitle("ReLinux-IDE");

        //wrapper.addNodes(centerTabPane);
        //Add bottom
        bottomTabPane = new TabPane();

        Tab consoleTab = new Tab("Console");
        consoleTab.setClosable(false);
        consoleTab.setContent(consoleArea);

        // tab32.setContent(new TextArea());
        bottomTabPane.getTabs().addAll(consoleTab);
        bottomTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> tab, Tab oldTab, final Tab newTab) {

                synchroniseUi();
            }
        });
        SplitPane centerSplitPane = new SplitPane();
        centerSplitPane.setDividerPositions(0.7f);
        centerSplitPane.getItems().addAll(centerTabPane, bottomTabPane);
        centerSplitPane.setOrientation(Orientation.VERTICAL);
        //Add left
        LeftSplitPane = new SplitPane();
        //VBox LeftVbox= new VBox(10);
        TabPane leftTabPane = new TabPane();

        filesTab.setClosable(false);
        filesTab.setContent(new TextArea());

        leftTabPane.getTabs().addAll(filesTab);
        TabPaneWrapper wrapperleft = new TabPaneWrapper(Orientation.HORIZONTAL, .1);

        LeftSplitPane.getItems().add(leftTabPane);
        // LeftSplitPane.getItems().add(tree);

        centerTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> tab, Tab oldTab, final Tab newTab) {
                if (newTab.getText() == null) {
                    stage.setTitle("");

                } else {
                    stage.setTitle("Relinux IDE - " + newTab.getText());
                    selectedtab = Integer.parseInt(newTab.getId());
                    synchroniseUi();

                    tools.GlobalConstants.selectedProjectFolder = FilesTree.getProjectName(new File(newTab.getTooltip().getText()));
                    if (!tools.GlobalConstants.lastSelectedProject.trim().equalsIgnoreCase(tools.GlobalConstants.selectedProjectFolder.trim())) {
                        loadManifest(new File("workspace/" + tools.GlobalConstants.selectedProjectFolder));
                    }

                }

            }
        });

        LeftSplitPane.setOrientation(Orientation.VERTICAL);
        wrapperleft.addNodes(LeftSplitPane);
        LeftSplitPane.prefHeight(100);
        LeftSplitPane.prefWidth(100);
        //wrapperleft.addNodes(LeftSplitPane, wrapperBottom.getNode());

        // wrapperleft.getNode()
        bp = new BorderPane();
        bp.setTop(menuBar);
        //bp.setCenter(centerTabPane);
        //bp.setBottom(wrapperBottom.getNode());
        //bp.setLeft(wrapperleft.getNode());
        pi.maxHeight(10);
        statusBar = new HBox();
        statusBar.setSpacing(5);
        statusBar.maxHeight(10);

        statusBar.setAlignment(Pos.CENTER_RIGHT);
        statusBar.getChildren().add(pi);
        // bp.setBottom(statusBar);
        SplitPane t1 = new SplitPane();
        t1.prefHeight(0);
        t1.maxWidth(0);

        //  TabPaneWrapper wrapperRight = new TabPaneWrapper(Orientation.VERTICAL, .7);
        //  SplitPane rsplitpane = new SplitPane();
        // rsplitpane.setOrientation(Orientation.VERTICAL);
        // rsplitpane.setDividerPositions(0.6);
        // wrapperRight.addNodes(rsplitpane);
        SplitPane bigTabPane = new SplitPane();

        bigTabPane.getItems().add(LeftSplitPane);
        // bigTabPane.getItems().add(centerTabPane);
        bigTabPane.getItems().add(centerSplitPane);
        // bigTabPane.getItems().add(rsplitpane);
        bigTabPane.setDividerPositions(0.1f, 0.8f);
        bp.setCenter(bigTabPane);
        //bp.setRight(wrapperRight.getNode());
        Scene myScene = new Scene(bp);
        //    myScene.fillProperty();
        new tools.settings();
        this.stage.setScene(myScene);
        this.stage.sizeToScene();
        this.stage.setWidth(primaryScreenBounds.getWidth() - 100);
        this.stage.setHeight(primaryScreenBounds.getHeight() - 100);
        this.stage.setX(100);
        this.stage.setY(100);
        this.stage.show();
        loadOpenFiles();

        ideStarted = true;
        this.stage.setOnCloseRequest(new EventHandler() {
            public void handle(Event t) {
                doExit(t);
            }
        });

    }

    /*public Tab generateTab(String name) {
     Tab result = new Tab(name);
     BorderPane content = new BorderPane();
     TextArea text = new TextArea();
     content.setCenter(text);
     result.setContent(content);
     return result;
     }*/
    public static void addoutput(int pid, String content) {
        //     outputTabTextArea[pid].appendText(content);
        //    outputTabTextArea[pid].positionCaret(outputTabTextArea[pid].getLength());
    }

    public void synchroniseUi() {
        Thread t4 = new Thread(new FilesTree());
        t4.start();

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                filesTab.setContent(FilesTree.tv);
                // filesTab.setContent(FilesTree.tv);
                //           settings.outPrintln("" + Scanner.NetScanner.livehosts);

            }
        });

    }

    public void doNewProject() {

        synchroniseUi();

        Stage newProjectDialog = new Stage();
        newProjectDialog.setTitle("New Project");

        Label folderExist = new Label("Folder Already Exist!!");
        folderExist.setTextFill(Paint.valueOf("Red"));
        folderExist.setVisible(false);
        final TextField projectName = new TextField();
        projectName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (new File("workspace/" + newValue).exists()) {
                folderExist.setText("Folder Already Exist!!");
                folderExist.setVisible(true);
            } else {
                folderExist.setVisible(false);

            }
        });
        ObservableList<String> parentOS = FXCollections.observableArrayList("Ubuntu");
        parentOS.add("Debian");
        parentOS.add("Fedora");
        ComboBox<String> combobox = new ComboBox((parentOS));

        ButtonBar buttonBar = new ButtonBar();
        Button okbutton = new Button("OK");
        Button cancelbutton = new Button("Cancel");
        buttonBar.getButtons().addAll(okbutton, cancelbutton);
        okbutton.setOnAction((ActionEvent event) -> {
            boolean errorFree = true;
            String project=projectName.getText().trim();
            if (combobox.getValue() == null || combobox.getValue().trim().length() < 1) {
                combobox.setEffect(new DropShadow(2, 0.1, 0.1, Color.RED));
                errorFree = false;
            }

            if (project.length() < 1) {
                folderExist.setText("Set A Project Name!!");
                errorFree = false;

            }
            
            if(errorFree){
            new File("workspace/"+project).mkdirs();
                try {
                    write(new File("workspace/"+project+"/manifest.xml"), "<PROJECTNAME>"+project+"</PROJECTNAME>");
                } catch (IOException ex) {
                    Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             addTab(new File("workspace/"+project+"/manifest.xml"), tabcounter);

        });
        cancelbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProjectDialog.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        projectName.setPromptText("Project Name");

        grid.add(new Label("Project Name:"), 0, 0);
        grid.add(projectName, 1, 0);
        grid.add(folderExist, 2, 0);
        grid.add(new Label("Parent OS:"), 0, 1);
        grid.add(combobox, 1, 1);
        grid.add(okbutton, 0, 2);
        grid.add(cancelbutton, 1, 2);

        newProjectDialog.setScene(new Scene(grid));
        newProjectDialog.initOwner(stage);
        newProjectDialog.show();
//        dlg.getActions().addAll(actionLogin, Dialog.Actions.CANCEL);

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("New  Project: Select Destination Directory");
//        String initialDir = "workspace";
//        fileChooser.setInitialDirectory(new File(initialDir));
//        fileChooser.getExtensionFilters().addAll(
//                new ExtensionFilter("All Files", "*.*"));
//        File selectedFile = fileChooser.showSaveDialog(stage);
//        if (selectedFile != null) {
//            selectedFile.mkdir();
//            tools.GlobalConstants.listOpenedProjects.add(selectedFile.getAbsolutePath());
//            boolean mkdir = new File(selectedFile.getAbsolutePath() + "/libs").mkdirs();
//            boolean mkdir2 = new File(selectedFile.getAbsolutePath() + "/src").mkdirs();
//            boolean mkdir3 = new File(selectedFile.getAbsolutePath() + "/ast").mkdirs();
//            boolean mkdir4 = new File(selectedFile.getAbsolutePath() + "/dist-db").mkdirs();
//            String content = "<PROJECT>" + selectedFile.getName() + "</PROJECT>\n<MAIN></MAIN>\n<LIB></LIB>\n<ARGS></ARGS>";
//            try {
//                write(new File(selectedFile.getAbsolutePath() + "/manifest.xml"), content);
//
//                addTab(new File(selectedFile.getAbsolutePath() + "/manifest.xml"), tabcounter);
//            } catch (IOException ex) {
//                Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println(selectedFile.getAbsolutePath() + " added to Open Project List");
//        }
    }

    public void openProject() {

        synchroniseUi();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project: Select Manifest (Manifest.xml) File");
        String initialDir = "workspace";
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Manifest", "*.xml"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            tools.GlobalConstants.listOpenedProjects.add(selectedFile.getParentFile().getAbsolutePath());
            System.out.println(selectedFile.getParentFile().getAbsolutePath() + " added to Open Project List");
            addTab(selectedFile, tabcounter);
        }
    }

    public static void loadManifest(File f2) {
        Thread lmfThread = new Thread(new Runnable() {

            @Override
            public void run() {
                FileInputStream fstream = null;
                try {
                    String content = "";
                    File f = new File(f2.getAbsolutePath() + "/manifest.xml");
                    if (!f.exists()) {
                        String content2 = "<PROJECT>" + f2.getName() + "</PROJECT>";
                        write(f, content2);

                    }
                    fstream = new FileInputStream(f);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                    String strLine;
                    //Read File Line By Line

                    while ((strLine = br.readLine()) != null) {
                        // Print the content on the console

                        System.out.println(strLine);

                    }   //Close the input stream
                    br.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fstream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        lmfThread.start();

    }

    public void formatCode() {

    }

    public static String read(File f) throws IOException {
        String content = "";
        if (!f.exists()) {
            return "";
        }
        FileInputStream fstream = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

//Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            content += strLine;
            content += "\n";
            System.out.println(strLine);
        }

//Close the input stream
        br.close();
        return content;
    }

    public static void loadOpenProjects() throws IOException {
        File f = new File("appdb/projects.xml");
        if (!f.exists()) {
            f.getParentFile().mkdir();
            f.createNewFile();
        }
        FileInputStream fstream = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

//Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            File tf = new File(strLine);
            if (tf.exists()) {
                GlobalConstants.listOpenedProjects.add(strLine);
                System.out.println(strLine);
            }
        }

//Close the input stream
        br.close();
    }

    public static void saveOpenProjects() {
        try {
            try (FileWriter fw = new FileWriter("appdb/projects.xml"); PrintWriter pw = new PrintWriter(fw)) {
                tools.GlobalConstants.listOpenedProjects.stream().forEach((get) -> {
                    pw.println(get);
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void loadOpenFiles() throws IOException {
        String content = "";
        File f = new File("appdb/files.xml");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileInputStream fstream = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

//Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            File tf = new File(strLine);
            if (tf.exists() && tf.isFile()) {
                addTab(tf, tabcounter);
                if (!tools.GlobalConstants.listOpenedFiles.contains(strLine)) {
                    tools.GlobalConstants.listOpenedFiles.add(strLine);
                }

                System.out.println(strLine);
            }
        }

//Close the input stream
        br.close();
    }

    public static void saveOpenFiles() {
        try {
            if (new File("appdb/files.xml").exists()) {
                new File("appdb/files.xml").delete();
            }
            FileWriter fw = new FileWriter("appdb/files.xml");
            PrintWriter pw = new PrintWriter(fw);
            for (String get : tools.GlobalConstants.listOpenedFiles) {
                pw.println(get);
                System.out.println("Printing " + get + " to appdb/files.xml");
            }
            pw.close();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void doExit(Event e) {
        e.consume();
        saveOpenFiles();
        saveOpenProjects();
        Dialog dialog = new Alert(Alert.AlertType.WARNING, "Do you want to exit ??", ButtonType.YES, ButtonType.NO);
        dialog.initOwner(stage);
        dialog.showAndWait()
                .filter(response -> response == ButtonType.YES).ifPresent(response -> {
            stage.close();
            System.exit(0);
        });

    }

    private void doNew() {
        synchroniseUi();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New  File: Select Destination Directory");
        String initialDir = "workspace";
        if (tools.GlobalConstants.selectedProjectFolder != null) {
            initialDir += ("/" + tools.GlobalConstants.selectedProjectFolder + "/src");
        }
        File initFile = new File(initialDir);
        if (!initFile.exists()) {
            initFile.mkdirs();
        }
        fileChooser.setInitialDirectory(initFile);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Java Files", "*.java"),
                new FileChooser.ExtensionFilter("XML MANIFEST Files", "*.mxml"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {

            if (!selectedFile.exists()) {
                try {
                    selectedFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            addTab(selectedFile, tabcounter);

        }

    }

    private void doOpen() {
        {

            synchroniseUi();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open  File");
            fileChooser.setInitialDirectory(new File("workspace"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("Java Files", "*.java"),
                    new FileChooser.ExtensionFilter("SIPS XML MANIFEST Files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                addTab(selectedFile, tabcounter);
            }

        }
    }

    private boolean doSave() {
        File file2 = new File(textTab[selectedtab].getTooltip().getText());
        if (file2.exists()) {
            file2.delete();
        }
        PrintStream out = null;
        try {
            out = new PrintStream(textTab[selectedtab].getTooltip().getText()); //new AppendFileStream
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.print(tools.GlobalConstants.ta[selectedtab].getText());
        out.close();
        return true;
        // return doSaveAs();
    }

    public static void addTab(File selectedFile, int tabcounter) {
        if (!tools.GlobalConstants.listOpenedFiles.isEmpty() && tools.GlobalConstants.listOpenedFiles.contains(selectedFile.getAbsolutePath())) {
            ObservableList<Tab> templs = centerTabPane.getTabs();
            System.out.println("" + templs);
            for (int i = 0; i < templs.size(); i++) {
                System.out.println("" + templs.get(i).getTooltip());
                if ((templs.get(i).getTooltip() != null) && templs.get(i).getTooltip().getText().equalsIgnoreCase(selectedFile.getAbsolutePath())) {
                    final int r = i;
                    Platform.runLater(() -> {
                        centerTabPane.getSelectionModel().select(r);
                    });
                }
            }
        } else {
            textTab[tabcounter] = new Tab(selectedFile.getName());

            textTab[tabcounter].setId("" + tabcounter);
            textTab[tabcounter].setTooltip(new Tooltip(selectedFile.getAbsolutePath()));
            textTab[tabcounter].setOnClosed((Event e) -> {
                tools.GlobalConstants.listOpenedFiles.clear();
                ObservableList<Tab> tl = centerTabPane.getTabs();
                tl.stream().forEach((tl1) -> {
                    tools.GlobalConstants.listOpenedFiles.add(tl1.getTooltip().getText());
                });
            });
            tools.GlobalConstants.listOpenedFiles.add(selectedFile.getAbsolutePath().trim());
            tools.GlobalConstants.ta[tabcounter] = new SyntaxTextArea();
            //TextArea ta= new TextArea();
            //SwingNode sn = new SwingNode();
            String line = null;
            String text = "";
            try {
                String temp = read(selectedFile);
                if (temp != null) {
                    text += temp;
                }
            } catch (IOException ex) {
                Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
            }
            tools.GlobalConstants.ta[tabcounter].setText(text);
            PrintStream out = null;
            try {
                out = new PrintStream(selectedFile.getAbsolutePath()); //new AppendFileStream
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.print(text);
            out.close();

            textTab[tabcounter].setContent(tools.GlobalConstants.ta[tabcounter].getNode());
            textTab[tabcounter].getStyleClass().add(org.fxmisc.richtext.demo.JavaKeywordsAsync.class.getResource("java-keywords.css").toExternalForm());
            centerTabPane.getTabs().add(textTab[tabcounter]);
            IncrementTabcounter();
        }
    }

    public static void IncrementTabcounter() {
        tabcounter++;
    }

    private boolean doSaveAs() {
        synchroniseUi();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As File: Select Destination Directory");
        String initialDir = "workspace";
        if (tools.GlobalConstants.selectedProjectFolder != null) {
            initialDir += ("/" + tools.GlobalConstants.selectedProjectFolder);
        }
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("SIPS XML MANIFEST Files", "*.xml"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {

            if (!selectedFile.exists()) {
                try {
                    selectedFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                write(selectedFile, tools.GlobalConstants.ta[selectedtab].getText());
            } catch (IOException ex) {
                Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static void listProjects() {

    }

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }

        ReLinuxIDE.launch(args);
    }

    @Override
    public void run() {
        try {

        } catch (Exception ex) {
            Logger.getLogger(ReLinuxIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class TabPaneWrapper {

        SplitPane split;

        public TabPaneWrapper(Orientation o, double splitLocation) {
            split = new SplitPane();

            //Change the CSS (uncomment if using an external css)
            //split.getStylesheets().add("test.css");
            split.setOrientation(o);
            split.setDividerPosition(0, splitLocation);
        }

        public void addNodes(final Node node1, final Node node2) {
            //Add to the split pane
            split.getItems().addAll(node1, node2);
        }

        public void addNodes(final Node node1) {
            //Add to the split pane
            split.getItems().add(node1);
        }

        public Parent getNode() {
            // split.setResizableWithParent(split, Boolean.TRUE);
            return split;
        }

    }

}
