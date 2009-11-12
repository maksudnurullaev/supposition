package org.supposition.tests.run;
import org.supposition.db.Cgroup;
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.db.proxy.CompanyFilterBean;
import org.supposition.db.proxy.CompanyProxy;

public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CompanyProxy cProxy = new CompanyProxy();
		CompanyFilterBean inFilter = new CompanyFilterBean();
		CgroupProxy cgroupProxy = new CgroupProxy();
		
		Cgroup cgroup_root = cgroupProxy.getRootElement();
		
		if(cgroup_root == null)	System.out.println("errors.data.not.found");
		
		inFilter.setCity("11115"); // Tashkent
		inFilter.setGuuid(cgroup_root.getUuid());
		inFilter.setOwner(false);
		
		System.out.println(cProxy.getPageAsHTMLTable(inFilter, 1));		
	  }
}
