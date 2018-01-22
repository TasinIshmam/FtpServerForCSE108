

package ftp.modal;


import ftp.controller.Main;
import ftp.controller.StartController;
import javafx.application.Platform;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class ServerAccess {
    public static final boolean DEBUG = true;
    private static ServerSocket socket;


    public static void startServer(String portString, StartController startController, Main main) {


        int port = 0;
        try {
            port = Integer.parseInt(portString);
            if (port < 1 || port > 65535) throw new Exception();
        } catch (NumberFormatException nfe) {

            startController.showPortConnectError("Invalid port Number", "Enter a proper integer number");

            return;
        } catch (Exception e) {
            startController.showPortConnectError("Invalid port Number", "Invalid nport range, valid ranges: 1-65535");
            System.out.println("error: Invalid port range, valid ranges: 1-65535");
            return;
        }


        try {
            socket = new ServerSocket(port);

        } catch (BindException be) {

            startController.showPortConnectError("Port In Use", "Select a port that is not being used");


            return;
        } catch (Exception e) {
            startController.showPortConnectError("Server Initiation Error", "Server could not be started");
            return;
        }


        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                //Update UI here
                try {
                    main.showServerPage(portString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        try {
            ClientAuthentification.read();
        } catch (IOException e) {
            System.out.println("Error reading user authentification data");
        }


        try {
            FTPServer ftpServer = new FTPServer();

            (new Thread(new ThreadCreatorClass(ftpServer, socket, main))).start();

        } catch (Exception e) {
            System.out.println("ftp.modal.ServerAccess");
            e.printStackTrace();
        }
    }
}