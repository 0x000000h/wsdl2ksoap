/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wsdl2ksoap.businesslogic;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author newky
 */
public class FileHelper
{
    private static String m_CompleteFolderPath;

    public static String GetOutputFolderPath()
    {
        return m_CompleteFolderPath;
    }
static public String getContents(InputStream aStream) {
    //...checks on aFile are elided
    StringBuilder contents = new StringBuilder();

    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input =  new BufferedReader(new InputStreamReader(aStream));
      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
          contents.append(line);
          contents.append(System.getProperty("line.separator"));
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }

    return contents.toString();
  }

static public void createFolderStructure(String parentPath, String packageName) throws Exception {
    if (parentPath.length() != 0) {
        //see if last character is already set with a /
        File path = new File(parentPath, packageName.replace('.', '/'));
        m_CompleteFolderPath = path.toString();
        path.mkdirs();
    }
    else {
        throw new Exception("Output folder has not been set");
    }
}

    static public boolean WriteClassTextToFile(String filename, String text)
    {
        try {
            //see if file exists first
            File fi = new File(filename);

            if (fi.exists()) {
                //delete it
                fi.delete();
            }

            //write to file
            Writer out = new OutputStreamWriter(new FileOutputStream(fi));

            out.write(text);
            out.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }


        return true;
    }
    
    private static String CLASPATH_PREFIX = "classpath:";
    public static InputStream loadResource(String uri){
        if(uri == null)
            return null;
        uri = uri.trim();
        
        if(uri.toLowerCase().startsWith(CLASPATH_PREFIX)){
            // load from classpath
            return Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(uri.substring(CLASPATH_PREFIX.length()));
        }else{
            try {
                // load from file
                return new FileInputStream(uri);
            } catch (FileNotFoundException ex) {}
        }
        return null;
    }
    
    public static void main(String [] args){
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("file:///home/pavle/asoundrc.txt");
            System.out.println(is);
            URL url = new URL("classpath:wsdl2ksoap/businesslogic/resources/BaseObject.txt");
            is = url.openStream();
            System.out.println(is);
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
