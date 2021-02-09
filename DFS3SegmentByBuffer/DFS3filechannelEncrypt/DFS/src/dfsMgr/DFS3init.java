package dfsMgr;

//import static init.WatchDir.startWatchDir;

import init.WatchDir;
import dfs3test.communication.Receiver;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class DFS3init {

    public static void main (String args[]) throws IOException, GeneralSecurityException {

        DFSInfo wrt = new DFSInfo();
        System.out.println("Welcome to B4 DFS");
        System.out.println("This is Brihaspati 4 Distributed File System : A peer-to-peer cloud storage system");
        System.out.println("You will get cloud storage 50% that of local storage offered (e.g.500MB cloud storage for 1GB local disk space offered)");
        System.out.println("Please enter your user ID for B4:");

        Scanner sc = new Scanner(System.in);
       // wrt.dfsuserID = sc.nextLine();

        System.out.println("Your DFS Directory is: " + System.getProperty("user.dir"));
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

        WatchDir.startWatchDir(wrt.dfsDir);

    }
}

class DFSInfo implements Serializable  {
        String dfsDir;
        long localspace;
        long cloudAuth = localspace/2;
        long cloudOccupied;
        long localfree;
        long localOccupied;
        long cloudBal;

    public DFSInfo() {
        File diskdtl = new File(System.getProperty("user.dir"));
        localfree = diskdtl.getFreeSpace();
        System.out.println("Free local space in user directory is: " + localfree/(1024*1024*1024) + "GB");

    }
}