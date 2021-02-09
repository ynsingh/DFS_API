package init;

import dfs3test.communication.Receiver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Scanner;
import dfsMgr.Upload;

public class DFSInit {

    public static void main(String[] args) throws IOException, GeneralSecurityException {

        System.out.println("Welcome to B4 DFS!");
        DFSConfig dfsconfig = DFSConfig.getInstance();
        Thread rx = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Receiver.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        rx.start();
        Upload.start();
        //WatchDir.startWatchDir();

    }

}
