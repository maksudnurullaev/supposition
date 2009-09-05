package org.supposition.db.proxy.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.proxy.interfaces.IDBProxyCollection;
import org.supposition.utils.Constants;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;


public abstract class ADBProxyObject<E extends CayenneDataObject> implements IDBProxyCollection<E>{
	private DataContext _context = DBUtils.getInstance().getDBContext();
	private List<Expression> _expressions = new ArrayList<Expression>();
	private Class<E> _eclass = null;
	private int _pageSize = Constants.getIntFromStr(MessagesManager.getText("default.page.size"));
	public Log _log = LogFactory.getLog(this.getClass());

	@Override
	public void addExpression(Expression inExpression) {
		_log.debug("->addExpression: " + inExpression.toString());
		_expressions.add(inExpression);
	}
	
	@Override	
	public void attachExpressions(SelectQuery inQuery) {
		for(Expression exp: getExpressions())inQuery.andQualifier(exp);
	}		
	
	@Override	
	public void cleanExpressions(){
		_expressions.clear();
	}
	
	@Override
	public void commitChanges() {
		_context.commitChanges();
	}
	
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
	
	@Override
	@SuppressWarnings("unchecked")
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
		return getClass().getSimpleName() + Constants._current_page_def;
	}
	
	@Override	
	public E getDBObjectByIntPk(int pk) {
		return DataObjectUtils.objectForPK(getObjectContext(), _eclass, pk);
	}	
	
	@Override
	public List<Expression> getExpressions() {
		return _expressions;
	}

	@Override
	public String getGo2PageDef() {
		return getClass().getSimpleName() + Constants._go2Page_jsf_def;
	}

	private String getHTMLFilter() {
		return (isSessionHasFilter()?
				String.format(MessagesManager.getText("template.simple.paginator.filter"),
						getRemoveFilterDef())
						:
						"");
	}	
	
	@Override
	public String getHTMLPaginator(int inPage) {
		String result = MessagesManager.getText("template.simple.paginator.header");
		int pageCount = getPageCount();

		if(pageCount < inPage)
			inPage = pageCount;
		
		if(inPage == 0)
			inPage = 1;
		
		
		if(pageCount == 1){
			result += " 1 ";
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
		
		result += String.format(MessagesManager.getText("template.simple.paginator.total"),
				getPageCountDef(), pageCount);
		
		result += getHTMLRowDensity();
		
		result += getHTMLFilter();
		
		result += MessagesManager.getText("template.simple.paginator.footer");
		
		return result;
	}

	@Override
	public String getHTMLRowDensity() {
		return String.format(MessagesManager.getText("template.simple.paginator.density"),
				getPageDencityDef(),
				getPageSize(),
				getSetPageDencityDef());
	}	

	@Override 
	public DataContext getObjectContext(){
		return _context;
	}		
	
	@Override
	public int getPageCount() {
		int pageSize = getPageSize();
		int itemCount = getCount();
		
		if(pageSize >= itemCount) return 1;
		
		int lastItems = itemCount % pageSize;
		int result = 0;
		
		if(lastItems == 0)
			result = itemCount / pageSize;
		else 
			result = (itemCount - lastItems) / pageSize +1;
		
		return result;
	}

	@Override
	public String getPageCountDef() {
		return getClass().getSimpleName() + Constants._page_count_def;
	}	
	
	@Override
	public String getPageDencityDef() {
		return getClass().getSimpleName() + Constants._page_density_def;
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
		return getClass().getSimpleName() + Constants._page_size_def;
	}

	@Override
	public String getRemoveFilterDef() {
		return getClass().getSimpleName() + Constants._remove_filter_jsf_def;
	}

	@Override
	public SelectQuery getSelectQuery() {
		SelectQuery resultQuery = new SelectQuery(_eclass);
		
		resultQuery.setPageSize(getPageSize());
		
		if(hasExpressions())
			attachExpressions(resultQuery);
		
		return resultQuery;
	}	
	
	@Override
	public String getSessionFilterDef() {
		return getClass().getSimpleName() + Constants._session_filter_def;
	}
	
	@Override
	public String getSetPageDencityDef() {
		return getClass().getSimpleName() + Constants._page_density_jsf_def;
	}

	@Override
	public boolean hasExpressions() {
		return (!getExpressions().isEmpty());
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
		SessionManager.setToSession(getPageSizeDef(), inPageSize);
	}	
}
