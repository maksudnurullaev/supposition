package org.supposition.db.interfaces;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;

public interface IDBProxyCollection<E> {
	int getCount();
	List<String> getColumnNames();
	void setPageSize(int inPageSize);
	int getPageSize();
	void clearExpressions();
	boolean hasExpressions();
	void addExpression(Expression inExpression);
	List<Expression> getExpressions();
	void commitChanges();
	void rollbackChanges();
	void attachExpressions(SelectQuery inQuery);
	SelectQuery getSelectQuery();
	List<E> getAll();
	void setEClass(Class<E> class1);
	E createNew() throws Exception;
	void deleteObject(E object);
	void deleteObjects(List<E> objects);
	DataContext getObjectContext();
	String getPageSizeDef();
	String getCurrentPageDef();
	String getPageCountDef();
	String getGo2PageDef();
	String getHTMLPaginator(int inPage);
	String getPageDencityDef();
	String getSetPageDencityDef();
	String getHTMLRowDensity();
	String getSessionFilterDef();
	boolean isSessionHasFilter();
	String getRemoveFilterDef();
	E getDBObjectByUuid(String inUuid);
	String getHTMLPaginator(int inPage, int itemsCount);
	int getPageCount(int itemCount);
	void addOrdering(Ordering inOrdering);
	void clearOrderings();
	void attachOrderings(SelectQuery inQuery);
	boolean hasOrderings();
	List<Ordering> getOrderings();
	
}