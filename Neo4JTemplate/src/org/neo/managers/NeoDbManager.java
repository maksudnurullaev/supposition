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
    private static NeoService _neo = null;
    private static IndexService _indexService = null;	    
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
		if(_neo == null){
			_neo = new EmbeddedNeo(getFullPath2NeoDbFolder());
			_log.debug("Setup neo database service done for path: " + getFullPath2NeoDbFolder());
			if(!_isShutDownHookInitilazed){
				registerShutdownHookForNeoAndIndexService();
				_log.debug("Registering shutdown hook for Neo and Index service: DONE!");
			}
		}
		
		if(_indexService == null){
			_indexService = new LuceneIndexService(_neo);
			_log.debug("Setup neo index service: DONE!");
			if(!_isShutDownHookInitilazed){
				registerShutdownHookForNeoAndIndexService();
				_log.debug("Registering shutdown hook for Neo and Index service: DONE!");
			}
		}
		return _neo != null && _indexService != null;
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
        if(_indexService != null) 
        	_indexService.shutdown();
        if(_neo != null) 
        	_neo.shutdown();
        
        _log.debug("Neo and Index services shutted down!");
    }    
}
