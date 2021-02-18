package init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import dfsMgr.Util;

public class DFSConfig implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static volatile DFSConfig config;

    public static String getRootinode() {
        try {
            FileInputStream fis= new FileInputStream(configFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            config = (DFSConfig)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return config.rootinode;
    }

    String rootinode;
    String PubU = null;
    String PubN = null;
    long initlocalFree;
    public long localfree;
    public long localOffered;
    public long localOccupied;
    public static String mailID;

    public static void update(long fileSize) throws IOException {
        config.setCloudOccupied(fileSize);
        config.setCloudAvlb(config.cloudOccupied);
        try {
            FileOutputStream fos = new FileOutputStream(configFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
            oos.close();
            System.out.println("DFS Config file updated successfully");
        } catch(Exception e) {
            e.printStackTrace();

        }
        System.out.println("Now cloud occupied is: "+ config.getCloudOccupied());
        System.out.println("Now cloud available is: "+ config.getCloudAvlb());
    }

    /**
     * @return the cloudAuth
     */
    public double getCloudAuth() {
        return config.cloudAuth;
    }

    /**
     * @param cloudAuth the cloudAuth to set
     */
    public void setCloudAuth(long cloudAuth) {
        config.cloudAuth = cloudAuth;
    }

    /**
     * @return the cloudOccupied
     */
    public static long getCloudOccupied() {
        try {
            FileInputStream fis= new FileInputStream(configFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            config = (DFSConfig)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return config.cloudOccupied;
    }

    /**
     * @param fileSize the cloudOccupied to set
     */
    public static void setCloudOccupied(long fileSize) {
        config.cloudOccupied = config.cloudOccupied + fileSize;
    }

    /**
     * @return the cloudAvlb
     */
    public static long getCloudAvlb() throws IOException{
        try {
        FileInputStream fis= new FileInputStream(configFile);
        ObjectInputStream ois = new ObjectInputStream(fis);

        config = (DFSConfig)ois.readObject();
        ois.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
        return config.cloudAvlb;
    }

    /**
     * @param cloudOccupied
     */
    public static void setCloudAvlb(long cloudOccupied) {
        config.cloudAvlb = config.cloudAvlb- config.cloudOccupied;
    }
    long cloudAuth;
    long cloudOccupied;
    long cloudAvlb;
    long localCacheSize = (long) (0.1*cloudAuth);
    long cacheOccupied=0;

    String dfsDir;
    String dfsSrvr;
    String dfsCache;
    static String configFile;

    private DFSConfig(){
        //Prevent form the reflection api.
        if (config != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static DFSConfig getInstance() {
        if (config == null) { //if there is no instance available... create new one
            synchronized (DFSConfig.class) {
                if (config == null) config = new DFSConfig();
                configFile=System.getProperty("user.dir")+System.getProperty("file.separator")+ "b4dfs"+System.getProperty("file.separator")+ "configFile.txt";
                File configfile = new File(configFile);
                if(configfile.exists()==false) config.firstInit();
                else config.regInit();
            }
        }
        return config;
    }

    //Make singleton safe from serialize and deserialize operation.
    protected DFSConfig readResolve() {
        return getInstance();
    }
    void firstInit()
    {
        System.out.println("This is Brihaspati 4 Distributed File System : A peer-to-peer cloud storage system");
        System.out.println("You will get cloud storage 50% that of local storage offered (e.g.500MB cloud storage for 1GB local disk space offered)");
        //Functions to get emailID, pubU and and pvtU

        File file = new File(System.getProperty("user.dir"));
        config.initlocalFree= file.getFreeSpace();
        int i=0;
        while(i<3) {
            System.out.println("Please specify your registered email ID for B4:");
            Scanner sc = new Scanner(System.in);
            mailID=sc.nextLine();
            System.out.println("Please specify local disk to be offered in GB:");
            String localdisk = sc.nextLine();
            if (Util.isValidEmail(mailID) && Util.isValidFloat(localdisk))
            {
                config.rootinode = "dfs://"+mailID+"/";
                float localoffered = Float.parseFloat(localdisk)*1024*1024*1024;
                long localOffered = (long)localoffered;
                if(config.initlocalFree>localOffered)
                    config.localOffered=localOffered;
                else
                {
                    System.out.println("Disk space available is insufficient.");
                    System.out.println("Disk space is: "+(config.initlocalFree/(1024*1024*1024))+"GB");
                    continue;
                }
                config.cloudAuth=config.localOffered/2;
                config.cloudAvlb=config.cloudAuth;

                break;
            }

            else
            {
                System.out.println("Invalid email ID or space offered.");
                i=i+1;
                continue;
            }
        }

        if(i==3)
        {
            System.out.println("You have exceeded the attempts.");
            System.exit(i);
        }

        config.dfsDir = System.getProperty("user.dir")+System.getProperty("file.separator")+ "b4dfs";
        Path pathDir = Paths.get(config.dfsDir);

        config.dfsSrvr = config.dfsDir + System.getProperty("file.separator")+ "dfsSrvr";
        Path pathSrvr = Paths.get(config.dfsSrvr);

        config.dfsCache = config.dfsDir + System.getProperty("file.separator")+ "dfsCache";
        Path pathCache = Paths.get(config.dfsCache);

        Path pathFile = Paths.get(configFile);


        try {
            Files.createDirectory(pathDir);
            System.out.println("DFS directory created successfully");
            Files.createDirectory(pathSrvr);
            System.out.println("DFS server directory created successfully");
            Files.createDirectory(pathCache);
            System.out.println("DFS cache directory created successfully");
            Files.createFile(pathFile);
            System.out.println("DFS configuration file created successfully");
            config.setCloudOccupied(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(configFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
            oos.close();
            System.out.println("DFS Config file initialized successfully");
        } catch(Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * @return the localfree
     */
    public long getLocalfree() {
        return localfree;
    }

    /**
     * @param localfree the localfree to set
     */
    public void setLocalfree(long localfree) {
        this.localfree = localfree;
    }

    /**
     * @return the localOffered
     */
    public long getLocalOffered() {
        return localOffered;
    }

    /**
     * @param localOffered the localOffered to set
     */
    public void setLocalOffered(long localOffered) {
        this.localOffered = localOffered;
    }

    /**
     * @return the localOccupied
     */
    public long getLocalOccupied() {
        return localOccupied;
    }

    /**
     * @param localOccupied the localOccupied to set
     */
    public void setLocalOccupied(long localOccupied) {
        this.localOccupied = localOccupied;
    }

    /**
     * @return the cacheOccupied
     */
    public long getCacheOccupied() {
        return cacheOccupied;
    }

    /**
     * @param cacheOccupied the cacheOccupied to set
     */
    public void setCacheOccupied(long cacheOccupied) {
        this.cacheOccupied = cacheOccupied;
    }

    /**
     * @return the dfsCache
     */
    public String getDfsCache() {
        return dfsCache;
    }

    /**
     * @param dfsCache the dfsCache to set
     */
    public void setDfsCache(String dfsCache) {
        this.dfsCache = dfsCache;
    }

    void regInit()
    {
        System.out.println("DFS is already initialized");
        System.out.println("Configuration file is in: "+configFile);

        try {
            FileInputStream fis= new FileInputStream(configFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            config = (DFSConfig)ois.readObject();
            System.out.println("Your root inode is: "+config.rootinode);
            System.out.println("Your authorized cloud space is: "+ (config.getCloudAuth()/(1024*1024*1024))+"GB");
            System.out.println("You have already used: "+ (config.getCloudOccupied()/(1024*1024))+"MB cloud space");
            System.out.println("Cloud space available is:" + (config.getCloudAvlb()/(1024*1024*1024))+"GB");

            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
