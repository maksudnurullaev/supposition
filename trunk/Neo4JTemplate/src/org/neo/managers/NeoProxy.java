package org.neo.managers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.supposition.utils.Utils;

public final class NeoProxy {
	public static Log _log = LogFactory.getLog("org.neo.managers.NeoProxy");	
	private static final boolean _isTomcatContext = Utils.isWebContext();
	private static NeoDbManager _neoDbManager = null;
	
	public String getNeoFolderName(){
		return getNeoDbManager().getFullPath2NeoDbFolder();
	}
	
	public static NeoDbManager getNeoDbManager(){
		if(_neoDbManager == null){
			if(_isTomcatContext){
				_neoDbManager = (NeoDbManager) ContextLoader.getCurrentWebApplicationContext().getBean("neoDbManager");
				_log.debug("Neo Database Manager created from Spring");
			}else{
				_neoDbManager = new NeoDbManager();
				_neoDbManager.setDbPath("neo-db");
				_log.debug("Neo Database Manager created with new operator");
			}
		}		
		return _neoDbManager;			
	}
}
