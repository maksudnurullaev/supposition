package org.neo.managers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo.managers.interfaces.INeoDbManager;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.util.index.IndexService;
import org.neo4j.util.index.LuceneIndexService;
import org.supposition.utils.Utils;

public class NeoDbManager implements INeoDbManager {
	private static Log _log = LogFactory.getLog(NeoDbManager.class);

	
	private static String _dbPath = null;
    private static NeoService _neoService = null;
    private static IndexService _neoIndexService = null;	    
    private static boolean _isShutDownHookInitilazed = false;
    
    public NeoDbManager() {
	}

	@Override
	public String getDbPath() {
		return _dbPath;
	}

	@Override
	public void setDbPath(String inPath) {
		_dbPath = inPath;
		_log.debug("NeoDbManager database folder name is: " + getDbPath());		
	}
	
	private static enum RelationTypes implements RelationshipType{
		Base2Managers,
		Managers2Manager
	}
	
	public boolean checkServices(){
		if(_neoService == null){
			_neoService = new EmbeddedNeo(getFullPath2NeoDbFolder());
			_log.debug("Setup neo database service done for path: " + getFullPath2NeoDbFolder());
			if(!_isShutDownHookInitilazed){
				registerShutdownHookForNeoAndIndexService();
				_log.debug("Registering shutdown hook for Neo and Index service: DONE!");
			}
		}
		
		if(_neoIndexService == null){
			_neoIndexService = new LuceneIndexService(_neoService);
			_log.debug("Setup neo index service: DONE!");
			if(!_isShutDownHookInitilazed){
				registerShutdownHookForNeoAndIndexService();
				_log.debug("Registering shutdown hook for Neo and Index service: DONE!");
			}
		}
		return _neoService != null && _neoIndexService != null;
	}
	
    public String getFullPath2NeoDbFolder() {
    	String result = Utils.getWebAppRootPath() + getDbPath();
    	_log.debug("Full path to neo db foulder is: " + result);
		return result;
	}

	private static void registerShutdownHookForNeoAndIndexService()
    {
        // Registers a shutdown hook for the Neo4j and index service instances
        // so that it shuts down nicely when the VM exits (even if you
        // "Ctrl-C" the running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                shutdown();
            }
        } );
    }    
    
    private static void shutdown()
    {
        if(_neoIndexService != null) 
        	_neoIndexService.shutdown();
        if(_neoService != null) 
        	_neoService.shutdown();
        
        _log.debug("Neo and Index services shutted down!");
    }

	public NeoService getNeoService() {
		if(checkServices()){
			return _neoService;
		}else if(_neoService == null){
			throw new NullPointerException("error.neo.service.is.null");
		}else if(_neoIndexService == null){
			throw new NullPointerException("error.neo.index.service.is.null");
		}
		return null;
	}    
}
