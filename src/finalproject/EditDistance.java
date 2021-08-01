package finalproject;


public class EditDistance {
	
	
	public static int editDistance(String word1, String word2) {
		
		int len_1 = word1.length();
		int len_2 = word2.length();
	 
		// incrementing the len by 1, as we want to return dp[n][m]
		int[][] dp = new int[len_1 + 1][len_2 + 1];
	 
		for (int i = 0; i <= len_1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len_2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate through to get the last char
		for (int i = 0; i < len_1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len_2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars are equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len_1][len_2];
	}
	
}
