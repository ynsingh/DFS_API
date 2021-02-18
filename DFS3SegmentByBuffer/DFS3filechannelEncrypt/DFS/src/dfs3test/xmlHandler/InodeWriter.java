package dfs3test.xmlHandler;
import dfsMgr.Upload;
import init.DFSConfig;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class InodeWriter {
    public static void writeInode(String fileName, long fileSize, HashMap<String, String> index)
    {
        try {
            String inode = System.getProperty("user.dir") + System.getProperty("file.separator") + fileName + "_Inode.xml";
            //System.out.println(inode);
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(new FileOutputStream(inode));
            xMLStreamWriter.writeStartDocument("1.0");
            //start inode
            xMLStreamWriter.writeStartElement("inode");
            //start fileName
            xMLStreamWriter.writeStartElement("FileName");
            xMLStreamWriter.writeCharacters(fileName);
            //end fileName
            xMLStreamWriter.writeEndElement();
            //start fileSize
            xMLStreamWriter.writeStartElement("FileSize");
            //write fileSize attribute
            xMLStreamWriter.writeCharacters(String.valueOf(fileSize));
            //end fileSize
            xMLStreamWriter.writeEndElement();
            //start fileURI
            xMLStreamWriter.writeStartElement("FileURI");
            //write fileURI attribute
            xMLStreamWriter.writeCharacters(Upload.fileURI);
            //System.out.println(Upload.fileURI);
            System.out.println(DFSConfig.getRootinode());
            //end fileURI
            xMLStreamWriter.writeEndElement();
            //start Author element
            xMLStreamWriter.writeStartElement("Author");
            //write Author attribute
            xMLStreamWriter.writeCharacters(System.getProperty("user.name"));
            //System.out.println(DFSConfig.mailID);
            //end Author
            xMLStreamWriter.writeEndElement();
            //start NoOfSegments
            xMLStreamWriter.writeStartElement("NoOfSegments");
            //write NoOfSegments attribute
            xMLStreamWriter.writeCharacters(String.valueOf(index.size()));
            //end NoOfSegments
            xMLStreamWriter.writeEndElement();
            long timestamp = System.currentTimeMillis();
            Date date = new Date(timestamp);
            Format format = new SimpleDateFormat(" dd MM  yyyy HH:mm:ss");
            System.out.println("Timestamp:" + format.format(date));
            //start timestamp element
            xMLStreamWriter.writeStartElement("TimeStamp");
            //write timestamp attribute
            xMLStreamWriter.writeCharacters(format.format(date));
            //System.out.println(DFSConfig.mailID);
            //end timestamp
            xMLStreamWriter.writeEndElement();
            //start FBit element
            xMLStreamWriter.writeStartElement("FBit");
            //write FBit attribute
            xMLStreamWriter.writeCharacters(String.valueOf(Upload.fBit));
            //end FBit
            xMLStreamWriter.writeEndElement();
            //write segment key and values
            xMLStreamWriter.writeStartElement("SplitParts");
            for(Map.Entry<String, String> entry: index.entrySet()) {
                // write the key
                xMLStreamWriter.writeStartElement(String.valueOf(entry.getKey()));
                //System.out.println(String.valueOf(entry.getKey()));
                // write the value
                xMLStreamWriter.writeCharacters(String.valueOf(entry.getValue()));
                //System.out.println(String.valueOf(entry.getValue()));
                //end tuple
                xMLStreamWriter.writeEndElement();
            }
            //end splitpart table
            xMLStreamWriter.writeEndElement();
            //end inode
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();

    } catch(Exception e){
        System.out.println(e.getMessage());
    }

        System.out.println("Inode file created successfully!");
    }

}
