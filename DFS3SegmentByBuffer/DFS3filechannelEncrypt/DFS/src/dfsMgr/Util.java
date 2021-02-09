package dfsMgr;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

        //function to check file size
        public static long checkFileSize(String path)
        {

            Path filepath = Paths.get(path);
            FileChannel fileChannel;
            long fileSize = 0;
            try {
                fileChannel = FileChannel.open(filepath);
                fileSize = fileChannel.size();
                //System.out.println(fileSize + " bytes");
                fileChannel.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileSize;
        }

        public static boolean isValidEmail(String email)
        {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            if (email == null)
                return false;
            return pat.matcher(email).matches();
        }

        public static boolean isValidFloat(String localdisk)
        {
            // regular expression for a floating point number
            String regex = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";

            // compiling regex
            Pattern p = Pattern.compile(regex);

            // Creates a matcher that will match input1 against regex
            Matcher m = p.matcher(localdisk);

            // If match found and equal to input1
            if(m.find() && m.group().equals(localdisk))
                return true;
            else
                return false;
        }

    }

