package com.riotapps.word.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;

public class FileUtils {
	private static final String TAG = FileUtils.class.getSimpleName();
	
	public static String ReadRawTextFile(Context ctx, int resId) 
    {
		try {
         InputStream inputStream = ctx.getResources().openRawResource(resId);

  
        //    InputStreamReader inputreader = new InputStreamReader(inputStream);
        //    BufferedReader buffreader = new BufferedReader(inputreader, 8192);
            
          //create a buffer that has the same size as the InputStream  
            byte[] buffer = new byte[inputStream.available()];  
            //read the text file as a stream, into the buffer  
            inputStream.read(buffer);  
            //create a output stream to write the buffer into  
            ByteArrayOutputStream oS = new ByteArrayOutputStream();  
            //write this buffer to the output stream  
            oS.write(buffer);  
            //Close the Input and Output streams  
            oS.close();  
        
			
					inputStream.close();
				
		
      
            //return the output stream as a String  
            return oS.toString(); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
        return "";
            
          /*  
             String line;
             StringBuilder text = new StringBuilder();
 
            sBuffer.append(strLine + "\n"); 
             try {
               while (( line = buffreader.readLine()) != null) {
                   text.append(line);
                   text.append('\n');
                 }
           } catch (IOException e) {
               return null;
           }
             try {
				buffreader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             buffreader = null;
             try {
				inputreader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             inputreader = null;
             
             try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             inputStream = null;
             return text.toString();
             */
         
    }
	
}
