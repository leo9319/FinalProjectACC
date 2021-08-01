package finalproject;

import textprocessing.BoyerMoore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebSearchEngine {
	
	public static String directoryPath = "Converted Text Files";
	
	 /*
     * Method to check if the URL is valid or not	
     */
	public static boolean isValidURL(String url)
    {
        String regex = "((http|https)://)(www.)?"
              + "[a-zA-Z0-9@:%._\\+~#?&//=]"
              + "{2,256}\\.[a-z]"
              + "{2,6}\\b([-a-zA-Z0-9@:%"
              + "._\\+~#?&//=]*)";
        Pattern pattern = Pattern.compile(regex);
        if (url == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
	
	public static int stringCompare(String str1, String str2)
    {
  
        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);
  
        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);
  
            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }
  

        if (l1 != l2) {
            return l1 - l2;
        }
  

        else {
            return 0;
        }
    }
	
	//Using Booyer-Moore Algorithm for string searching. This method would return the word count.
	public static int getWordCount(File filename, String keyword) throws IOException {
		int count = 0;
		int offset = 0;
		String my_data = "";
		String s1 = keyword;
		s1 = s1.toLowerCase();
		BoyerMoore offset1a = new BoyerMoore(s1);
		try {
			BufferedReader my_Object = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = my_Object.readLine()) != null) {
				my_data = my_data + line;
			}
			my_Object.close();

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		String txt = my_data;
		txt = txt.toLowerCase();	
		for (int loc = 0; loc <= txt.length(); loc += offset + s1.length()) {
			offset = offset1a.search(s1, txt.substring(loc));
			if ((offset + loc) < txt.length()) {
				count++;																								
			}
		}
		
		return count;
	}
	
public static void option1() throws Exception{
		
		Scanner input1 = new Scanner(System.in);
		Scanner input2 = new Scanner(System.in);
		String url = "";
		String keys = "";
		
		
		Crawler crawl = new Crawler();
		InvertedIndex index = new InvertedIndex();
		while(!isValidURL(url)) {
			System.out.println("Please enter a valid URL on which you want to perform a search: ");
			url = input1.nextLine();
		}
		
		
		// Initiates web crawler and converts the HTML to txt file
		crawl.getPageLinks(url, 0, 25);
		crawl.getPageContents();
		
		//Adding the directory path so that we can create Inverted Index
		File Directory = new File(directoryPath);
		String[] testingFiles = Directory.list();
		index.indexBuilding(testingFiles);
		
		//Storing the URLs associated with the title of the txt file stored in the directory
		LinkedHashMap<String,String> test = crawl.getMappedUrls();
	
		long start = System.nanoTime();   
		System.out.println();
		System.out.println("Enter the keywords: ");
		keys = input2.nextLine();
		String suggested = index.spellChecker(keys);

		String a = suggested.toLowerCase().toString();
		String b = keys.toLowerCase().toString();
		a= a.replaceAll("\\s", "");
		b = b.replaceAll("\\s", "");
		if((stringCompare(a, b) != 0)){
			System.out.println("Did you mean: "+suggested);
			System.out.println();
		}
		
		//Using find() method of Inverted Index to fetch the list of files in which the keyword appears
		ArrayList<String> fileNames = new ArrayList<String>();
		fileNames =  index.find(suggested);
		List<Results> res=new ArrayList<Results>();
		long end = System.nanoTime();
		long total_time = start-end;
		long seconds = total_time/1_000_000_000;
		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println("About :"+seconds+" seconds taken to fetch results");
		System.out.println("--------------------------------------------------");
		 for(int i=0;i<fileNames.size();i++){
	        	int word_count = getWordCount(new File(directoryPath+"/"+fileNames.get(i)), suggested);
	        	String url2 = test.get(fileNames.get(i));
	        	res.add(new Results(suggested, word_count,directoryPath+"/"+fileNames.get(i), url2 ));        
	        }
		 
		 //Using Comparator along with Collections.sort() to show the results in descending order
		Collections.sort(res, new SortbyOccurence());
		System.out.println();
		
		//Condition looped upto 10, to show the top 10 results
		for(int i=0;i<res.size();i++){
			System.out.println(res.get(i));
			System.out.println("------------------------------------------------");
		}
		input1.close();
		input2.close();
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception{
		
		System.out.println("-----------------------------------------------------");
		System.out.println("Welcome to JAVA Web Search Engine");
		System.out.println("-----------------------------------------------------");
		System.out.printf("%-20s\n","Team Members:");
		System.out.println();
		System.out.printf("%-20s\n","Rahul Meghani");
		System.out.printf("%-20s\n","Roxy Leonard Palma");
		System.out.printf("%-20s\n","Sarabjeet Kaur");
		System.out.printf("%-20s\n","Aksa Benny");
		System.out.println();
		System.out.println("-----------------------------------------------------");
		
		
		
		// Prompt an input from the user
		
		System.out.println();
		System.out.println("Press 1 to enter URL and then perform search");
		System.out.println("Press 2 to exit the program");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		switch(choice){
		case 1:
		    option1();
		    break;
		  case 2:
			System.out.println("Have a Nice Day!");
		    break;
		  default:
			  System.out.println("Invalid Input detected. Please try again");
			  
		}
		sc.close();
		
	}

}
