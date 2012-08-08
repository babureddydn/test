
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Misc implements Runnable {
    private String url ="http://ww.google.com";
    private static int i=0;
    public Misc(String u){
        url = u;
    }
    public static void main(String[] args)throws Exception{
        new Thread(new Misc("http://www.google.com/")).start();
    }
    private static void retrieveBinaryData(String targetUrl) throws IOException {
        URL url;
                URLConnection urlConnection = null;
        DataInputStream dataInputStream = null;
        byte []imageBytes;
        int initialSizeLimit = 2000;
        int nextSizeLimit = 2000;
        try {
            url = new URL(targetUrl);
            urlConnection = url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(1000 * 10);
            urlConnection.setReadTimeout(1000 * 60 * 5);

            imageBytes = new byte[initialSizeLimit];
            dataInputStream = new DataInputStream(urlConnection.getInputStream());

             while(dataInputStream.available()>0) {
                dataInputStream.read(imageBytes, 0,nextSizeLimit);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(dataInputStream != null) {
                    dataInputStream.close ();
                    dataInputStream = null;
                }
            }catch(Exception e){
                dataInputStream = null;
                e.printStackTrace();
            }
            try {
                if(urlConnection != null) {
                    ((HttpURLConnection)urlConnection).disconnect();
                    System.out.println("Url closed");
                }
            }catch(Exception e){
                urlConnection = null;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
        	long startTime = System.nanoTime();
            retrieveBinaryData(url);
        	long endTime = System.nanoTime();
			System.out.println("Time taken:" + (endTime - startTime) + " ns");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}