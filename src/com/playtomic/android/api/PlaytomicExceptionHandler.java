package com.playtomic.android.api;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaytomicExceptionHandler implements UncaughtExceptionHandler {
    
    private UncaughtExceptionHandler mDefaultUEH;

    private String mTrackUrl;
    private String mSourceUrl;
    
    public PlaytomicExceptionHandler() {
        mSourceUrl = Playtomic.SourceUrl();
        mTrackUrl = "http://g" 
                    + Playtomic.GameGuid
                    + ".api.playtomic.com/tracker/e.aspx?swfid="
                    + Playtomic.GameId
                    + "&url=" 
                    + mSourceUrl;
                    // debug 
                    //+ "&debug=y";
        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    public void uncaughtException(Thread t, Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        sendException(stacktrace);
        mDefaultUEH.uncaughtException(t, e);
    }

    public void sendException(String stacktrace) {        
        if (Playtomic.isInternetActive()) {    
            HttpURLConnection connection = null;
            try {          
                URL url = new URL(mTrackUrl);
                connection = (HttpURLConnection) url.openConnection();   
                
                String urlParameters = "&stacktrace=" + clean(stacktrace); 
                
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", 
                                "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + 
                                Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");  
                        
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                
                DataOutputStream wr = new DataOutputStream (
                          connection.getOutputStream ());
                wr.writeBytes (urlParameters);
                wr.flush ();
                wr.close ();
                
                connection.getInputStream();
                
                // debug
                /*
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                
                String decodedString;
                String response = "";
                
                while ((decodedString = in.readLine()) != null) {
                    response += decodedString;
                }
                response = "";
                */
            }
            catch(Exception ex) {
                return;
            }
            finally {
                if (connection != null)
                    connection.disconnect();
            }
        }
    }

    private String clean(String string) {    
        string = string.replace("/","\\");
        string = string.replace("~","-");
        string = URLUTF8Encoder.encode(string);
        return string;
    }
}
