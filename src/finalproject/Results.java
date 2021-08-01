package finalproject;

public class Results {
		String keyword;
		int occurences;
		String path;
		String Url;
		
		public Results(String keyword, int occurences, String path, String Url){
			this.keyword= keyword;
			this.occurences = occurences;
			this.path = path;
			this.Url = Url;
		}
		
		
		@Override
		public String toString(){
			return "Found "+this.occurences+" occurences in the following url\n"
					+ this.Url+"\n";
		}
}

