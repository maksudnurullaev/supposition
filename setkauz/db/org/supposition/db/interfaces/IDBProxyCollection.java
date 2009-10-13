package org.supposition.db.interfaces;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;

public interface IDBProxyCollection<E> {
	void addExpression(Expression inExpression);
	void addOrdering(Ordering inOrdering);
	void attachExpressions(SelectQuery inQuery);
	void attachOrderings(SelectQuery inQuery);
	void clearExpressions();
	void clearOrderings();
	void commitChanges();
	E createNew() throws Exception;
	void deleteObject(E object);
	void deleteObjects(List<E> objects);
	List<E> getAll();
	List<String> getColumnNames();
	int getCount();
	String getCurrentPageDef();
	E getDBObjectByUuid(String inUuid);
	List<Expression> getExpressions();
	String getGo2PageDef();
	String getHTMLPaginator(int inPage);
	String getHTMLPaginator(int inPage, int itemsCount);
	String getHTMLRowDensity();
	DataContext getObjectContext();
	List<Ordering> getOrderings();
	String getPageCountDef();
	String getPageDensityDef();
	int getPageSize();
	String getPageSizeDef();
	String getRemoveFilterDef();
	SelectQuery getSelectQuery();
	String getSessionFilterDef();
	String getSetPageDensityDef();
	boolean hasExpressions();
	boolean hasOrderings();
	boolean isSessionHasFilter();
	void rollbackChanges();
	void setEClass(Class<E> class1);
	void setPageSize(int inPageSize);
	
}