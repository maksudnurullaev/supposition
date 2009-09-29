import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.query.Ordering;
import org.supposition.db.Ads;
import org.supposition.db.proxy.AdsProxy;

public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdsProxy adsProxy = new AdsProxy();
		
		List<Ads> adsList = adsProxy.getAll();
		List<Ads> sortedAdsList = new ArrayList<Ads>();
		for(Ads ads:adsList){
			System.out.println(ads.getCreated());
			sortedAdsList.add(ads);
		}
		
		adsList = null;
		
		Ordering ordering  = new Ordering("created", false);
		ordering.orderList(sortedAdsList);
		
		System.out.println("adsProxy.getPageSize = " + adsProxy.getPageSize());
		
		for(Ads ads:sortedAdsList){
			System.out.println(ads.getCreated());
		}		
		
	  }
}
