package finalproject;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Crawler {
	private static final int MAX_DEPTH = 2;
	private static LinkedHashSet<String> links;
	private static LinkedHashMap<String, String> mappedURLS ;
	
	public Crawler() {
		links = new LinkedHashSet<>();
		mappedURLS  = new LinkedHashMap<String, String>();
	}
	
	//to keep the track of the URL associated with the title of the text document
	public LinkedHashMap<String,String> getMappedUrls(){
		return mappedURLS;
	}
	
	//A Utility Method to remove irrelevant content
	private boolean shouldCrawl(String url) {
		if (this.links.contains(url)) {
			return false;
		}
		if (url.endsWith(".jpeg")) {
			return false;
		}
		if (url.endsWith(".swf")) {
			return false;
		}
		if (url.startsWith("javascript:")) {
			return false;
		}
		if (url.endsWith(".gif")) {
			return false;
		}
		if (url.endsWith(".jpg")) {
			return false;
		}
		if (url.contains("mailto:")) {
			return false;
		}
		if (url.contains("#") || url.contains("?")) {
			return false;
		}
		if (url.endsWith(".pdf")) {
			return false;
		}
		if (url.endsWith(".png")) {
			return false;
		}
		return true;
	}
	
	//Fetching the nested links present on the web page
	public void getPageLinks(String URL, int depth, int pages) {
		if ((!links.contains(URL) && (depth < MAX_DEPTH) && (links.size() < pages))) {
			System.out.println(">> Crawling >> " + "[" + URL + "]");
			
			try {
					if(shouldCrawl(URL)){
					links.add(URL);
					Document document = Jsoup.connect(URL).get();
					Elements linksOnPage = document.select("a[href]");
					depth++;
				
				for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth, pages);
                }
			}
			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}
	
	//Fetching the content of the crawled pages and storing it as a txt file locally
	public void getPageContents() {
		
		// Create a new directory to store converted text files
		File newDirectory = new File("Converted Text Files");
		newDirectory.mkdir();
		
		// Get text contents from each URL
		int i = 0;
		try {
			for (String URL : links) {
				Document document = Jsoup.connect(URL).get();
				String title = document.title();
				
				File newFile = new File( "Converted Text Files//" + i + ".txt");
				mappedURLS.put(newFile.getName(), URL);
				BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
				bw.write(document.body().text().toLowerCase());
	    		bw.flush();
	    		bw.close();
	    		i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} 
		
}
