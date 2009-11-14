package org.supposition.tests.run;
import java.util.Date;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.supposition.db.Ads;
import org.supposition.db.Cgroup;
import org.supposition.db.proxy.AdsProxy;
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.db.proxy.CompanyFilterBean;
import org.supposition.db.proxy.CompanyProxy;

public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdsProxy adsProxy = new AdsProxy();
//		adsProxy.addExpression(ExpressionFactory.lessExp("deleteAfter", (new Date())));
//		adsProxy.addExpression(ExpressionFactory.matchExp("companyId", null));
		
		adsProxy.addExpression(Expression.fromString("company_id != null"));
		
		for (Ads ad : adsProxy.getAll()) {
			System.out.println(ad);
			
		}
		
	  }
}
