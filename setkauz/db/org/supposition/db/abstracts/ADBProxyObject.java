package org.supposition.db.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.interfaces.IDBProxyCollection;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;
import org.supposition.utils.Utils;


public abstract class ADBProxyObject<E extends CayenneDataObject> implements IDBProxyCollection<E>{
	private DataContext _context;
	private List<Expression> _expressions = new ArrayList<Expression>();
	private List<Ordering> _orderings = new ArrayList<Ordering>();
	private Class<E> _eclass = null;
	private int _pageSize = Utils.getIntFromStr(MessagesManager.getDefault("default.page.size"));
	public Log _log = LogFactory.getLog(this.getClass());

	
	@Override
	public void addExpression(Expression inExpression) {
		_expressions.add(inExpression);
	}
	
	@Override
	public void addOrdering(Ordering inOrdering){
		_orderings.add(inOrdering);
	}
	
	@Override
	public void clearOrderings(){
		_orderings.clear();
	}
	
	@Override
	public void attachOrderings(SelectQuery inQuery){
		for(Ordering ordering:_orderings) inQuery.addOrdering(ordering);
	}
	
	@Override	
	public void attachExpressions(SelectQuery inQuery) {
		for(Expression exp: getExpressions())inQuery.andQualifier(exp);
	}		
	
	@Override	
	public void clearExpressions(){
		_expressions.clear();
	}
	
	@Override
	public void commitChanges() {
		_context.commitChanges();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E createNew() throws Exception {
		if(_eclass == null)
			throw new Exception("Null _eClass, should be assigned some DBObject class ");
		return (E)_context.newObject(_eclass);
	}

	@Override
	public void deleteObject(E object){
		_context.deleteObject(object);
		_context.commitChanges();
	}
	
	@Override
	public void deleteObjects(List<E> objects) {
		_context.deleteObjects(objects);
		
	}
	
	public DataContext getDataContext(){
		return _context;
	}
	
	public void setDataContext(DataContext inContext){
		_context = inContext;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAll() {
		return _context.performQuery(getSelectQuery());
	}		
	
	@Override
	public int getCount() {
		SelectQuery query = getSelectQuery();
		query.setPageSize(1);
		return _context.performQuery(query).size();
	}
	
	@Override
	public String getCurrentPageDef(){
		return getClass().getSimpleName() + MessagesManager.getDefault("current.page.def");
	}

	@Override	
	public E getDBObjectByUuid(String inUuid){
		if(inUuid == null){
			_log.debug(String.format("%s: Could not find object,  UUID = null", getClass().getSimpleName()));
				
			return null;
		}
		
		clearExpressions();
		addExpression(ExpressionFactory.matchDbExp("uuid", inUuid));
		List<E> listOfObjects = getAll();
		if(listOfObjects == null || listOfObjects.size() == 0) {
			_log.warn(String.format("%s: Could not find object by UUID = %s", getClass().getSimpleName(), inUuid));
			return null;
		}
		
		if(listOfObjects.size() == 1)
			return listOfObjects.get(0);
		
		_log.fatal(String.format("Unique UUID %s found for %s %s objects", inUuid, listOfObjects.size(), getClass().getSimpleName()));
		return null;		
	}
	
	@Override
	public List<Expression> getExpressions() {
		return _expressions;
	}

	@Override
	public List<Ordering> getOrderings(){
		return _orderings;
	}
	
	@Override
	public String getGo2PageDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("go2Page.jsf.def");
	}

	private String getHTMLFilter() {
		return (isSessionHasFilter()?
				String.format(MessagesManager.getText("template.simple.paginator.filter"),
						getRemoveFilterDef())
						:
						"");
	}	
	
	@Override
	public String getHTMLPaginator(int inPage, int itemsCount) {
		int pageCount = DBUtils.getPageCount(itemsCount, getPageSize());

		if(pageCount < inPage)
			inPage = pageCount;
		
		if(inPage == 0)
			inPage = 1;
		
		
		String result = MessagesManager.getText("template.simple.paginator.header");
		if(pageCount == 1){
			result += String.format(MessagesManager.getText("template.simple.paginator.page_current.disabled"), 
					getCurrentPageDef(), 1);
		}else{
			if(inPage != 1) 
				result += 
					String.format(MessagesManager.getText("template.simple.paginator.btn_back"), 
							String.format(getGo2PageDef(), inPage - 1 ));
			
			result += String.format(MessagesManager.getText("template.simple.paginator.page_current"), 
					getCurrentPageDef(), inPage);

			if(inPage != pageCount){
				result += String.format(MessagesManager.getText("template.simple.paginator.btn_forward"), 
						String.format(getGo2PageDef(), inPage + 1 ));
			}
		}
		
		result += String.format(MessagesManager.getText("template.simple.paginator.total"),pageCount);
		
		result += getHTMLRowDensity();
		
		result += getHTMLFilter();
		
		result += MessagesManager.getText("template.simple.paginator.footer");
		
		return result;
	}
	
	@Override
	public String getHTMLPaginator(int inPage) {
		return getHTMLPaginator(inPage, getCount());
	}

	@Override
	public String getHTMLRowDensity() {
		return String.format(MessagesManager.getText("template.simple.paginator.density"),
				getPageDensityDef(),
				getPageSize(),
				getSetPageDensityDef());
	}	

	@Override 
	public DataContext getObjectContext(){
		return _context;
	}		
	
	@Override
	public String getPageCountDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("page.count.def");
	}	
	
	@Override
	public String getPageDensityDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("page.density.def");
	}	
	
	@Override
	public int getPageSize() {
		if(SessionManager.isExist(getPageSizeDef()))
			return MessagesManager.getSessionIntValue(getPageSizeDef());
		else
			SessionManager.setToSession(getPageSizeDef(), _pageSize);
		return _pageSize;
	}		
	
	@Override
	public String getPageSizeDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("page.size.def");
	}

	@Override
	public String getRemoveFilterDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("remove.filter.jsf.def");
	}

	@Override
	public SelectQuery getSelectQuery() {
		SelectQuery resultQuery = new SelectQuery(_eclass);
		
		resultQuery.setPageSize(getPageSize());
		
		if(hasExpressions())
			attachExpressions(resultQuery);
		
		if(hasOrderings())
			attachOrderings(resultQuery);
		
		return resultQuery;
	}	
	
	@Override
	public String getSessionFilterDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("session.filter.def");
	}
	
	@Override
	public String getSetPageDensityDef() {
		return getClass().getSimpleName() + MessagesManager.getDefault("page.density.jsf.def");
	}

	@Override
	public boolean hasExpressions() {
		return (!getExpressions().isEmpty());
	}	
	
	@Override
	public boolean hasOrderings(){
		return (!getOrderings().isEmpty());
	} 
	
	@Override
	public boolean isSessionHasFilter(){
		return (SessionManager.getFromSession(getSessionFilterDef()) != null);		
	}
	
	@Override
	public void rollbackChanges() {
		_context.rollbackChanges();
	}
	
	@Override
	public void setEClass(Class<E> eClass) {
		_eclass = eClass;
	}

	@Override
	public void setPageSize(int inPageSize) {
		if(inPageSize <= 100)
			SessionManager.setToSession(getPageSizeDef(), inPageSize);
		else
			SessionManager.setToSession(getPageSizeDef(), 100);
	}	
}
