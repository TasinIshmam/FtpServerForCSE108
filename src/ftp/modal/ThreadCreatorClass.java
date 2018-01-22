

package ftp.modal;


import ftp.controller.Main;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadCreatorClass implements Runnable {
    private FTPServer ftpServer;
    private ServerSocket nSocket;
    Main main;

    public ThreadCreatorClass(FTPServer ftpServer, ServerSocket nSocket, Main main) {
        this.ftpServer = ftpServer;
        this.nSocket = nSocket;
        this.main = main;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " ThreadCreatorClass");
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        while (true) {
            try {

                executorService.execute(new Worker(ftpServer, nSocket.accept(), main));
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " ThreadCreatorClass failed to start Worker");
            }
        }
    }
}