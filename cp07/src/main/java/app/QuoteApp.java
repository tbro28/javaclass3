package app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Application to conduct some testing.
 */
public class QuoteApp {
    private static void exec(final String ticker, final String rstype) {
        HttpURLConnection conn = null;
        String baseUrl = "http://localhost:8080/StockQuote/StockQuoteServlet";
        try {
            String urlStr = String.format("%s?symbol=%s&rstype=%s", baseUrl, ticker, rstype);
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");

            //System.out.printf("Content-Type: %s%n", conn.getContentType());
            InputStream in = conn.getInputStream();
            Reader rdr = new InputStreamReader(in);
            char buf[] = new char[1024];
            int len = 0;
            while ((len = rdr.read(buf)) != -1) {
                System.out.print(new String(buf, 0, len));
            }
            System.out.println();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) {
        String symbol = "gme";
        
        System.out.println("JSON:");
        exec(symbol, "json");

        System.out.println();
        System.out.println("Plain:");
        exec(symbol, "plain");
        
        System.out.println();
        System.out.println("HTML:");
        exec(symbol, "html");
        
        System.out.println();
        System.out.println("XML:");
        exec(symbol, "xml");
        
        System.out.println();
        System.out.println();
    }

}
