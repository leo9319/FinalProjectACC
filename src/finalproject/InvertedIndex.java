package finalproject;
import java.io.*;

import java.util.*;


public class InvertedIndex {
	
	private static Set<String> stop_words = new HashSet<String>(Arrays.asList("as,your,you,yours,at,be,by,for,from,of,on,it,a,the,and,are,an,that,was,to,were,has,he,is,its,will,with".split(",")));

	public static String directoryPath = "Converted Text Files";
    Map<Integer,String> sources; // (0, web.txt) (1, web2.txt)
    HashMap<String, HashSet<Integer>> index; //(for each word in web.txt, {set where unique value})

    InvertedIndex(){
        sources = new HashMap<Integer,String>();
        index = new HashMap<String, HashSet<Integer>>();
        
    }
    public HashMap<String, HashSet<Integer>> getIndex(){
    	return index;
    }
    
    //Method to create Inverted Index
    public void indexBuilding(String[] files){
        int map_index = 1;
        for(String filename:files){            
            try(BufferedReader file = new BufferedReader(new FileReader(directoryPath+"/"+filename))){
            	
                sources.put(map_index,filename);
                String text;
                while( (text = file.readLine()) !=null) {
                    String[] fetch_words = text.split("\\W+");
                    for(String word:fetch_words){
                        word = word.toLowerCase();
                        if(!stop_words.contains(word)){
                        if (!index.containsKey(word))
                            index.put(word, new HashSet<Integer>());
                        index.get(word).add(map_index);
                        }
                    }
                    
                }
                
            } catch (IOException e){
                System.out.println("File "+filename+" not found. Skip it");
            }
            map_index++;
        }
    
    }
    
    //This Method would return the list of all files in which the keyword is present.
    public ArrayList<String> find(String input){
    	ArrayList<String> filenames;
    	try {
    		filenames = new ArrayList<String>();
	        String[] words = input.split("\\W+");

	        String hashKey = words[0].toLowerCase();
	        if(index.get(hashKey) == null) {
	        	 System.out.println("Oops! Word not found!");
	        	return filenames;
	        }
	        HashSet<Integer> res = new HashSet<Integer>(index.get(hashKey));
	        for(String word: words){
	            res.retainAll(index.get(word));
	        }
	
	        if(res.size()==0) {
	            System.out.println("Word not found!");
	            return new ArrayList<>();
	        }
	        for(int number : res){
	        	filenames.add(sources.get(number));
	        }
    	}catch(Exception e) {
    		
    		 System.out.println("Exception Found:" + e.getMessage());
    		 return new ArrayList<>();
    	}  
    return filenames;
    }
    
    //This method would check the spelling and would suggest the correct words based on the edit distance of 1 unit.
    public String spellChecker(String query) {
		String[] tokens = query.split(" ");
		String finalQuery = "";

		for (String token : tokens) {
			if (index.containsKey(token)) {
				finalQuery += token + " ";
			} else {
				for (String key : index.keySet()) {
					if (EditDistance.editDistance(token, key) < 2) {
						finalQuery += key + " ";
						break;
					}
				}
			}

		}

		return finalQuery;
	}
   

}
