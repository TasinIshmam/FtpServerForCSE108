
package ftp.modal;


import ftp.controller.Main;
import ftp.controller.ServerController;
import javafx.application.Platform;
import shared.FileData;
import shared.InfoData;
import shared.LsData;

import java.io.*;
import java.net.Socket;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Worker implements Runnable {
    private FTPServer ftpServer;
    private Socket socket;
    private Path path;
    private List<String> commands;


    //Input


    ObjectOutputStream objectOutputStream;




    ObjectInputStream objectInputStream;


    String userName;
    String passWord;
    Boolean isClient;
    Main main;
    ServerController serverController;


    // I/OStream can only read/write bytes
    // DataI/OStream can read primitive java types, like int,char,long etc. So like an upgraded I/OStream
    //I/OStreamReader can only read CHARACTERS. The char class basically.
    //BufferedReader/Writer just makes the process of reading/writing faster by peeking ahead. Nothing else.
    //ObjectI/OStream


    public Worker(FTPServer ftpServer, Socket socket, Main main) throws Exception {
        this.ftpServer = ftpServer;
        this.socket = socket;
        path = Paths.get(System.getProperty("user.dir"));

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());


;
        
        this.main = main;
        serverController = main.getServerControllerInstance();

    }

    public void updateLog(String message) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                //Update UI here
                try {
                    serverController.updateLog(userName + ": " + message);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void get() throws Exception {
        String errorMessage;
        byte[] byteDataArray2 = new byte[100];

        if (Files.isDirectory(path.resolve(commands.get(1)))) {
            errorMessage =  "get: " + path.resolve(commands.get(1)).getFileName() + ": Is a directory" + "\n";
            FileData fileDataErr = new FileData(byteDataArray2, errorMessage,  commands.get(1), 0, true );
            objectOutputStream.writeObject(fileDataErr);
            return;
        }

        if (Files.notExists(path.resolve(commands.get(1)))) {
            errorMessage = "get: " + path.resolve(commands.get(1)).getFileName() + ": No such file or directory" + "\n";
            FileData fileDataErr = new FileData(byteDataArray2, errorMessage,  commands.get(1), 0, true );
            objectOutputStream.writeObject(fileDataErr);
            return;

        }
        //is a directory



        ftpServer.getLockAccess(path.resolve(commands.get(1)));



        //blank message


        File file = new File(path.resolve(commands.get(1)).toString());


        try (BufferedInputStream bufferedInputStreamForFile = new BufferedInputStream(new FileInputStream(file))) {


            int bytesRead = 0;

            FileData fileData = new FileData(byteDataArray2, "",  commands.get(1), 0, false   );


            System.out.println("before begging of while loop");
            int count = 0;
            long bytesWritten = 0;
           byte[] byteDataArray = new byte[1000000];
            // BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
           while(true)
           {    count++;
              /* byte[] byteDataArray = null;
               fileData.setDataArray(null) ;

               System.gc();*/
              // byteDataArray = new byte[100000000];
               if(ServerAccess.DEBUG)
               System.out.println("while loop first line count" + count);

               bytesRead = bufferedInputStreamForFile.read(byteDataArray);
               if(ServerAccess.DEBUG)
               System.out.println("while loop first line count after reading from stream" + count);


              fileData.setDataArray(byteDataArray);
              fileData.setBytesRead(bytesRead);



               fileData.setBytesRead(bytesRead);
               fileData.setDataArray(byteDataArray);

               bytesWritten += fileData.getBytesRead();

               if(ServerAccess.DEBUG)
               System.out.println("while loop instance" + count);

               if(bytesRead == -1)
               {    if(ServerAccess.DEBUG)
                   System.out.println("eof reached");

                   fileData.setEOF(true);
                   Integer a = fileData.getBytesRead();
                   System.out.println(a);
                   objectOutputStream.reset();
                   objectOutputStream.writeUnshared(fileData);

                   objectOutputStream.flush();
                   break;
               }
               else
               {    if(ServerAccess.DEBUG)
                   System.out.println("writing object time" + count);
                   objectOutputStream.reset();

                   objectOutputStream.writeUnshared(fileData);
                   Integer a = fileData.getBytesRead();
                   System.out.println(a);

                   objectOutputStream.flush();
                   if(ServerAccess.DEBUG)

                   System.out.println("After writing object" + count);
               }




           }






            System.out.println("Total bytes recieved " + bytesWritten );



        } catch (Exception e) {
            if(ServerAccess.DEBUG)
            System.out.println("Exception faced in get");
            errorMessage = "get: " + path.resolve(commands.get(1)).getFileName() + ": Could not transfer file" ;
            FileData fileDataErr = new FileData(byteDataArray2, errorMessage,  commands.get(1), 0, true );
            objectOutputStream.writeObject(fileDataErr);

        }



        updateLog("user requested to download file: " + commands.get(1));


        ftpServer.getLockUnlock(path.resolve(commands.get(1)));
/*

        while (!ftpServerShared.getLockAccess(path.resolve(commands.get(1))))
            Thread.sleep(10);


        //blank message
        dataOutputStream.writeBytes("\n");


        //transfer file
        byte[] buffer = new byte[10000];
        try {
            File file = new File(path.resolve(commands.get(1)).toString());


            long fileSize = file.length();

            dataOutputStream.writeBytes(Long.toString(fileSize) + "\n");
            System.out.println(fileSize);
            Thread.sleep(100);



            //write file
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            int count = 0;
            while ((count = in.read(buffer)) > 0) {


                dataOutputStream.write(buffer, 0, count);
                dataOutputStream.flush();
            }
*/

        }


    public void put() throws Exception {

        ftpServer.putLockAccess(path.resolve(commands.get(1)));


        File file = new File(commands.get(1));
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);

        System.out.println("Opened file");

        long bytesWriten = 0;


        while(true) {

            Object o = objectInputStream.readUnshared();


            System.out.println("read object");


            if(o instanceof FileData)
            {

                FileData fileData = (FileData) o;


                if(!fileData.getErrorMessage().equals(""))
                {
                    System.out.println(fileData.getErrorMessage());
                    updateLog(fileData.getErrorMessage());
                    break;
                }



                else if(fileData.isEOF() || fileData.getBytesRead() == -1){
                    fileOutputStream.close();
                    System.out.println("EOF reached");
                    break;
                }
                else {
                    System.out.println("reading data");
                    Integer a = fileData.getBytesRead();
                    Boolean b = fileData.isEOF();
                    System.out.println(a.toString() + b.toString());

                    fileOutputStream.write(fileData.getDataArray(), 0, fileData.getBytesRead());
                    bytesWriten += fileData.getBytesRead();
                }


            }
            else
            {

                System.out.println("Garbage data recieved, Canceling download");
                fileOutputStream.close();
                Files.deleteIfExists(file.toPath());
                break;
            }



        }

        System.out.println("Total bytes recieved "  + bytesWriten);
        System.out.println("Closing file, done recieving");

        ftpServer.putLockRelease(path.resolve(commands.get(1)));



       /* Object o = objectInputStream.readObject();

        ftpServer.putLockAccess(path.resolve(commands.get(1)));


        if(o instanceof FileData)
        {

            FileData fileData = (FileData) o ;
            FileOutputStream f = new FileOutputStream(new File(commands.get(1)));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(f);
            bufferedOutputStream.write(fileData.getDataArray());
            bufferedOutputStream.close();

            updateLog("User uploaded file " + commands.get(1) + "to the folder "  + path);

        }
        else
        {
            updateLog("User Failed to upload file" + commands.get(1));
        }*/

        ftpServer.putLockRelease(path.resolve(commands.get(1)));
/*


        while (!ftpServer.putLockAccess(path.resolve(commands.get(1))))
            Thread.sleep(10);


        System.out.println("Successfully locked");


        //get file size
        byte[] fileSizeBuffer = new byte[8];

        String fileSizeString = bufferedReader.readLine();

        System.out.println(fileSizeString);


        ByteArrayInputStream bais = new ByteArrayInputStream(fileSizeBuffer);
        DataInputStream dis = new DataInputStream(bais);

        long fileSize = Long.parseLong(fileSizeString);
        

        System.out.println(fileSize);


        System.out.println("read size");

        //receive the file
        FileOutputStream f = new FileOutputStream(new File(commands.get(1)).toString());
        int count = 0;

        byte[] buffer = new byte[10000];

        long bytesReceived = 0;
        int i = 0;
        while (bytesReceived < fileSize) {


            count = dataInputStream.read(buffer);
            f.write(buffer, 0, count);
            bytesReceived += count;
            System.out.println("Bytes Recieved: " + bytesReceived);
        }
        f.close();

        if(ServerAccess.DEBUG)
        System.out.println("Put has ended");

        ftpServer.putLockRelease(path.resolve(commands.get(1)));

        if(ServerAccess.DEBUG)
            System.out.println("Has removed path from the lockMap");*/
    }

    public void delete() throws Exception {
        if (!ftpServer.delete(path.resolve(commands.get(1)))) {
            objectOutputStream.writeObject(new InfoData("delete: cannot remove '" + commands.get(1) + "': The file is locked"));
            return;
        }

        try {
            boolean confirm = Files.deleteIfExists(path.resolve(commands.get(1)));
            if (!confirm) {


                objectOutputStream.writeObject(new InfoData("delete: cannot remove '" + commands.get(1) + "': No such file"));


            } else
                objectOutputStream.writeObject(new InfoData("delete successful"));


        } catch (DirectoryNotEmptyException e) {
            objectOutputStream.writeObject(new InfoData("delete: failed to remove `" + commands.get(1) + "': Directory not empty"));


        } catch (Exception e) {

            objectOutputStream.writeObject(new InfoData("delete: failed to remove `" + commands.get(1) + "'"));


        }
    }

    public void ls() throws Exception {
        try {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(path);

            ArrayList<String> theArrayList = new ArrayList<>();

            for (Path entry : dirStream) {
                theArrayList.add(entry.getFileName().toString());
            }

            LsData lsData = new LsData(theArrayList);
            objectOutputStream.writeObject(lsData);

        } catch (Exception e) {

            ArrayList<String> theArrayList = new ArrayList<>();
            LsData lsData = new LsData(theArrayList);
            objectOutputStream.writeObject(lsData);


            e.printStackTrace();
        }
    }

    public void cd() throws Exception {
        try {
            //cd
            if (commands.size() == 1) {

                path = Paths.get(System.getProperty("user.dir"));


                objectOutputStream.writeObject(new InfoData(""));
            }
            //cd ..


            else {
                if (Files.notExists(path.resolve(commands.get(1)))) {
                   objectOutputStream.writeObject(new InfoData("cd: \" + commands.get(1) + \": No such file or directory\""));
                }

                else if (Files.isDirectory(path.resolve(commands.get(1)))) {
                    path = path.resolve(commands.get(1));
                    objectOutputStream.writeObject(new InfoData(""));
                }
                else {
                    objectOutputStream.writeObject(new InfoData("cd: " + commands.get(1) + ": Not a directory"));
                }
            }


        } catch (Exception e) {
             objectOutputStream.writeObject(new InfoData("cd: " + commands.get(1) + ": Error"));
        }
    }

    public void mkdir() throws Exception {

        try {
            Files.createDirectory(path.resolve(commands.get(1)));


            InfoData infoData = new InfoData("");

            objectOutputStream.writeObject(infoData);

        } catch (FileAlreadyExistsException falee) {

            InfoData infoData = new InfoData("mkdir: cannot create directory `" + commands.get(1) + "': File or folder exists");

            objectOutputStream.writeObject(infoData);

        } catch (Exception e) {

            InfoData infoData = new InfoData("mkdir: cannot create directory `" + commands.get(1) + "': Permission denied");

            objectOutputStream.writeObject(infoData);

            }
    }

    public void pwd() throws Exception {
        //send path

        InfoData infoData = new InfoData(path.toString());

        objectOutputStream.writeObject(infoData);

    }

    public void bye() throws Exception {
        //close socket
        socket.close();

        throw new Exception();
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " Worker Started");

        try {

            System.out.println("First message being recieved which should be pwd: " );
            objectInputStream.readObject();
            pwd();



            isClient = true;



            if (isClient) {
                userName = (String) objectInputStream.readObject();
                passWord = (String) objectInputStream.readObject();

                //System.out.println("was able to accept username and pass, they are" + userName + " " + passWord);

                ClientAuthentification testClientAuthentification = ClientAuthentification.userHashMap.get(userName);

                if (testClientAuthentification != null && testClientAuthentification.passWord.equals(passWord)) {

                        objectOutputStream.writeObject("true");
                    updateLog("Connected to Server");
                } else {
                    objectOutputStream.writeObject("false");
                    bye();

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            updateLog("User's Server access request denied due to incorrect credentials");
            return;
        }

        if (isClient) Platform.runLater(new Runnable() {

            @Override
            public void run() {
                //Update UI here
                try {

                    serverController.addUser(userName);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        boolean runWorker = true;


        while (runWorker) {
            try {
                System.out.println("waiting for command");
                //check every 10 ms for input


                //capture and parse input
                commands = new ArrayList<String>();

                Object o = objectInputStream.readObject();

                if( ! (o instanceof String))
                {
                    System.out.println("GARBAGE DATA READ IN MAIN COMMAND WAITING FOR LOOP. DISCARDING");
                    continue;
                }

                String command = (String) o;
                Scanner tokenize = new Scanner(command);
                //gets command

                if (tokenize.hasNext())
                    commands.add(tokenize.next());

                if (tokenize.hasNext())


                    commands.add(command.substring(commands.get(0).length()).trim());
                tokenize.close();

                if (ServerAccess.DEBUG) System.out.println(commands.toString());

                if (ServerAccess.DEBUG)

                    System.out.println("Command recieved: " + command);

                //command selector
                switch (commands.get(0)) {
                    case "get":
                        get();
                        break;

                    case "cd":
                        cd();
                        break;

                    case "mkdir":
                        mkdir();
                        break;
                    case "pwd":
                        pwd();
                        break;
                    case "put":
                        put();
                        break;
                    case "delete":
                        delete();
                        break;
                    case "ls":
                        ls();
                        break;

                    case "bye":
                        runWorker = false;
                        bye();
                        break;
                    default:
                        System.out.println("invalid command");


                }
            } catch (Exception e) {

                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " quit");

        if (isClient)
            updateLog("Logging out");
        if (isClient) Platform.runLater(new Runnable() {

            @Override
            public void run() {


                try {
                    serverController.removeUser(userName);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}