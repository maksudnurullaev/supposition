package org.supposition.db.proxy.interfaces;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

public interface IDBProxyCollection<E> {
	int getCount();
	List<String> getColumnNames();
	void setPageSize(int inPageSize);
	int getPageSize();
	void cleanExpressions();
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
	E getDBObjectByIntPk(int pk);
}