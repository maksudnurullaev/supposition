import java.util.Calendar;

public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
	    Calendar now = Calendar.getInstance();

	    System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
	        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

	    System.out.println("Current week of month is : " + now.get(Calendar.WEEK_OF_MONTH));
	    System.out.println("Current week of year is : " + now.get(Calendar.WEEK_OF_YEAR));

	    

	    now.add(Calendar.WEEK_OF_YEAR, 1);
	    System.out.println("date after one week : " + (now.get(Calendar.MONTH) + 1) + "-"
	        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
	  }
}
