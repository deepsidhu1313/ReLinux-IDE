/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author navdeep singh <navdeepsingh.sidhu95@gmail.com>
 */
public class CPanel extends Application {

    CheckBox cb, cb2;
    int process, network, ping;
   // TextField hname;

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("OK");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

               new Thread(new Runnable() {

                    @Override
                    public void run() {
                     GlobalConstants.verbose = cb.isSelected();
                GlobalConstants.dumplog = cb2.isSelected();
                GlobalConstants.PROCESS_LIMIT = process;
               // GlobalConstants.PING_THREADS = ping;
                GlobalConstants.total_threads = network;
//                GlobalConstants.processExecutor.shutdownNow();
                GlobalConstants.processExecutor= Executors.newFixedThreadPool(process);
               // GlobalConstants.pingExecutor = Executors.newFixedThreadPool(GlobalConstants.PING_THREADS);
               // GlobalConstants.netExecutor = Executors.newFixedThreadPool(GlobalConstants.total_threads);
               // GlobalConstants.HOST_NAME = hname.getText();
                Tools.saveSettings();
                    }
                }).start();
                event.consume();                primaryStage.close();
            }
        });
        Button btn2 = new Button();
        btn2.setText("Cancel");
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                event.consume();
                primaryStage.close();
            }
        });
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(50);
        //hbox.setStyle("-fx-background-color: #336699;");
        hbox.setAlignment(Pos.CENTER);

        hbox.getChildren().addAll(btn, btn2);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vbox.getChildren().add(title);

        cb = new CheckBox("Verbose");
        cb.setSelected(GlobalConstants.verbose);

        cb2 = new CheckBox("Dump Log");
        cb2.setSelected(GlobalConstants.dumplog);

        Text plimit = new Text("Process Limit: " + tools.GlobalConstants.PROCESS_LIMIT);
        plimit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Slider processlimit = new Slider(1, 50, tools.GlobalConstants.PROCESS_LIMIT);
        processlimit.setMinorTickCount(1);
        processlimit.setMajorTickUnit(4);
        processlimit.setBlockIncrement(2);
        processlimit.setShowTickLabels(true);
        processlimit.setShowTickMarks(true);
        processlimit.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                process = new_val.intValue();
                plimit.setText(String.format("Process Limit: " + new_val.intValue()));
            }
        });
        Text nlimit = new Text("Network Threads: " + tools.GlobalConstants.total_threads);
        nlimit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Slider netlimit = new Slider(1, 20, tools.GlobalConstants.total_threads);
        netlimit.setMinorTickCount(1);
        netlimit.setMajorTickUnit(4);
        netlimit.setBlockIncrement(1);
        netlimit.setShowTickLabels(true);
        netlimit.setShowTickMarks(true);
        netlimit.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                network = new_val.intValue();
                nlimit.setText(String.format("Network Threads: " + new_val.intValue()));
            }
        });
//        //Text pinlimit = new Text("Ping Threads: " + controlpanel.GlobalConstants.PING_THREADS);
//       // pinlimit.setFont(Font.font("Arial", FontWeight.BOLD, 12));
//       // Slider pinglimit = new Slider(1, 250, controlpanel.GlobalConstants.PING_THREADS);
//        pinglimit.setMinorTickCount(1);
//        pinglimit.setMajorTickUnit(25);
//        pinglimit.setBlockIncrement(25);
//        pinglimit.setShowTickLabels(true);
//        pinglimit.setShowTickMarks(true);
//        pinglimit.valueProperty().addListener(new ChangeListener<Number>() {
//            public void changed(ObservableValue<? extends Number> ov,
//                    Number old_val, Number new_val) {
//                ping = new_val.intValue();
//                pinlimit.setText(String.format("Ping Threads:  " + new_val.intValue()));
//            }
//        });

        Text host = new Text("Host name: ");
        host.setFont(Font.font("Arial", FontWeight.BOLD, 12));

//        hname = new TextField();
//        hname.setPromptText(GlobalConstants.HOST_NAME);

        VBox.setMargin(host, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(host);

//        VBox.setMargin(hname, new Insets(0, 0, 0, 8));
//        vbox.getChildren().add(hname);

        VBox.setMargin(cb, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(cb);
        VBox.setMargin(cb2, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(cb2);
        VBox.setMargin(plimit, new Insets(20, 0, 0, 8));
        vbox.getChildren().add(plimit);
        VBox.setMargin(processlimit, new Insets(0, 8, 0, 8));
        vbox.getChildren().add(processlimit);
        VBox.setMargin(nlimit, new Insets(20, 0, 0, 8));
        vbox.getChildren().add(nlimit);
        VBox.setMargin(netlimit, new Insets(0, 8, 0, 8));
        vbox.getChildren().add(netlimit);
//        VBox.setMargin(pinlimit, new Insets(20, 0, 0, 8));
//        vbox.getChildren().add(pinlimit);
//        VBox.setMargin(pinglimit, new Insets(0, 8, 0, 8));
//        vbox.getChildren().add(pinglimit);
//        vbox.setAlignment(Pos.TOP_LEFT);

        BorderPane root = new BorderPane();
        root.setCenter(vbox);
        root.setBottom(hbox);

        Scene scene = new Scene(root, 450, 450);

        primaryStage.setTitle("Control Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
