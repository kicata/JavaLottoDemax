package bg.demax.lottoWebUtils;

import java.util.Comparator;

public class CustomSortComparator implements Comparator<String>{

	    @Override
	    public int compare(String str1, String str2) {
	    	
	    	try {
	    		int val1 = Integer.parseInt(str1);
	    		int val2 = Integer.parseInt(str2);
	    		
	    		if(val1 > val2){
	    			return 1;
	    		}else if(val1 < val2){
	    			return -1;
	    		}else{
	    			return 0;
	    		}

			} catch (Exception e) {
				return str1.compareTo(str2);
		}

	}

}
