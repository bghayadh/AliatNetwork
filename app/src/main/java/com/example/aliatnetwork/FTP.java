package com.example.aliatnetwork;

import org.apache.commons.net.ftp.FTPClient;

public class FTP extends FTPClient {
    private String server;
    private String user;
    private String pass;
    private int port;



    public FTP()
    {
        server="ftp.ipage.com";
        user="beid";
        pass="10th@Loop";
        port=21;

    }

    public String getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public int getPort() {
        return port;
    }

}
