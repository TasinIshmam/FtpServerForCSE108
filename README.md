# FtpServerForCSE108
This FTP Server was designed and implemented by me for my Objected Oriented Programming Language Sessional. The corresponding FTP Client was implemented by my partner Ashiqur Rahman.

The backend has been developed in JAVA and the frontend is made with JAVAFX

Features of this server include 

1) Support for 8 commands which are as follows
-get : to access a specific file from the server
-cd : to change the directory and browse through the server folder hirearchy
-pwd : to pass the pathname of the current directory
-put : to upload as file to the server from the client
-delete : to delete a file from the server
-ls : to list the files in the current directory
- bye: to terminate session 

2) Fully concurrent design to ensure a single server can server multiple clients simultaneously without any data corruption or conflicts.

3) Half Duplex Communication between server and client

4) Authentication and sign in of clients based on username and password

5) Monitor acitvities of all clients currently logged in

6) Realtime log of all commands passed, activities and errors with timestamps

![first](https://user-images.githubusercontent.com/29299547/46474023-a5fbcd80-c803-11e8-9486-21e7163cdfc7.png)
![second](https://user-images.githubusercontent.com/29299547/46474031-a7c59100-c803-11e8-9a6f-3b845a5e445e.png)
