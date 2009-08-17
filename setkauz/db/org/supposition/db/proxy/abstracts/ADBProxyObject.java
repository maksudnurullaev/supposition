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
import org.supposition.utils.DBUtils;


public abstract class ADBProxyObject<E extends CayenneDataObject> implements IDBProxyCollection<E>{
	private DataContext _context = DBUtils.getInstance().getDBContext();
	private List<Expression> _expressions = new ArrayList<Expression>();
	private Class<E> _eclass = null;
	private int _pageSize = 0;
	public Log _log = LogFactory.getLog(this.getClass());

	@Override
	public void setEClass(Class<E> eClass) {
		_eclass = eClass;
	}
	
	@Override	
	public E getDBObjectByIntPk(int pk) {
		return DataObjectUtils.objectForPK(getObjectContext(), _eclass, pk);
	}		
	
	@Override 
	public DataContext getObjectContext(){
		return _context;
	}
	
	@Override
	public void commitChanges() {
		_context.commitChanges();
	}
	
	@Override
	public void addExpression(Expression inExpression) {
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
	@SuppressWarnings("unchecked")
	public List<E> getAll() {
		return _context.performQuery(getSelectQuery());
	}

	@Override
	public List<Expression> getExpressions() {
		return _expressions;
	}

	@Override
	public int getPageSize() {
		return _pageSize;
	}	

	@Override
	public void setPageSize(int inPageSize) {
		this._pageSize = inPageSize;
	}

	@Override
	public boolean hasExpressions() {
		return (!getExpressions().isEmpty());
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
	public int getCount() {
		SelectQuery query = getSelectQuery();
		query.setPageSize(1);
		return _context.performQuery(query).size();
	}

	@Override
	public void deleteObjects(List<E> objects) {
		_context.deleteObjects(objects);
		
	}	
}
