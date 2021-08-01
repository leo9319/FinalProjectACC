package finalproject;

import java.util.Comparator;

public class SortbyOccurence implements Comparator<Results>{
	
	//compare method to showcase results in descending order
	public int compare(Results a, Results b){
		return b.occurences - a.occurences;
	}
}
