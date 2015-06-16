import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * The Class Crawler.
 */
public class Crawler {
    
    /** The pages visited. */
    
	//Used HashSet in order to remove the duplicated links 
	private static Set<String> pagesVisited = new HashSet<String>();

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws MalformedURLException the malformed url exception
     */
    public static void main(String[] args) throws IOException , MalformedURLException {
        
    	System.out.println("Enter a URL to crawl");
    	
    	@SuppressWarnings("resource")
		Scanner inputEntries = new Scanner(System.in);
    	String rootUrl = inputEntries.nextLine();
    	
    	System.out.println("Enter Number of Links To Visit"); 
    	int noofLinks = inputEntries.nextInt();
    	
    	if(isValidURL(rootUrl)) {
        pagesVisited.add(rootUrl);
        visitLink(rootUrl , noofLinks);
    	}
      
    }

    /**
     * Checks if is valid url.
     *
     * @param rootUrl the root url
     * @return true, if is valid url
     */
    private static boolean isValidURL(String rootUrl) {
    	try { 
        	URL url = new URL(rootUrl);
        	URLConnection connection = url.openConnection();
        	connection.connect();
        	} catch (MalformedURLException e) {
        		System.out.println("No protocol");
        		return false;
        	} catch (IOException e) {
        		System.out.println("Invalid URL");
        		return false;
        	}
		return true;
	}

	/**
	 * Visit link.
	 *
	 * @param url the url
	 * @param noofcrawlers the maxdepth
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void visitLink(String url , int noofcrawlers) throws IOException {

       //Exit when Maximum Links are fetched.
    	if (noofcrawlers == 0) {
            System.out.println("Maximum visit limit reached!...");
            System.exit(0);
        }

        System.out.println("Parsing the url :" + url);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Skipping url " + url + " due to connection issue.");
        }

        // retrieve a document from web page
        // extract the data
        Elements docElements = doc.select("p");
        for (Element docElement : docElements) {
            String data = docElement.text();
            if (!data.isEmpty()) {
                System.out.println(data);     
            } else {
                System.out.println("Empty page");
            }
        }

        // collect all the links
        Elements hrefElements = doc.select("a[href]");
        for (Element element : hrefElements) {
            String href = element.absUrl("href");
            if (!href.isEmpty()) {
                if (href.endsWith("/")) {
                    href = href.substring(0, href.length() - 1);
                }
               
                //Recursive call to fetch sub links.
                
                if (!pagesVisited.contains(href)) {
                    pagesVisited.add(href);
                    visitLink(href , noofcrawlers-1);
                } else {
                    System.out.println("Skipping already visited url " + href);
                }
            }
        }


    }
}