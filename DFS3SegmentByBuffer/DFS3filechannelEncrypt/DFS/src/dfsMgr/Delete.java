package dfsMgr;

import dfs3test.communication.Sender;
import dfs3test.encrypt.Hash;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static dfs3Util.file.DeleteRecord;
import static dfs3Util.file.csvreader;
import static dfs3test.xmlHandler.XMLWriter.writer;
/**
 * Class responsible for performing Delete.
 * This class deletes the file from DFS cloud.
 * <p><b>Functions:</b> At the user end of DFS.</p>
 * <b>Note:</b> Change this file to change functionality
 * related to Delete
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since   15th Feb 2020
 */
public class Delete {
    /**
     * Starts the delete process.
     * This method starts the deletion of the file from DFS.
     * It interacts with the communication manager on DFS behalf for
     * deleting the files through XMLhandler
     * @param inode inode of the file that user selected
     * @param uploadIndex file containing the index of uploaded file
     * @throws IOException for input output exception
     * @throws NoSuchAlgorithmException In case algorithm provided for hashing is invalid
     */
    public static void start(String uploadIndex, String inode) throws IOException, NoSuchAlgorithmException {

        // get path to splitindex.csv and retrieve the segment inodes
        String splitFile = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "SplitIndex.csv";
        // retrieve all segment inodes against inode from the splitIndex
        String[] segmentInode = csvreader(splitFile,inode);
        //Querry the DFS with each segment inode for deleting them
        for(int i = 0;i< segmentInode.length  && !(segmentInode[i]==null);i++){
            // compute the hash of the inode to be deleted
            String hashedInode = Hash.hashpath("DFS://sidharth@iitk.ac.in"+segmentInode[i]);
            // xml query  with inode.tag for delete is 3
            //the data filed is blank hence "Nothing" to avoid null pointer exception
            String xmlPath = writer(3,hashedInode,"Nothing".getBytes());
            //TODO - retrieve the ip of the responsible node
            // send query for deletion to destination
            Sender.start(xmlPath, "localhost");
            //update the index splitFile
            DeleteRecord(inode,splitFile);
        }
        // Update index once the deletion has been carried out
        DeleteRecord(inode,uploadIndex);
    }
}
