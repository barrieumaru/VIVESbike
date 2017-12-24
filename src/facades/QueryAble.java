package facades;

import java.util.Collection;

/**
 * An interface for simple querying to the persistent storage. <br />
 * The philosophy behind this interface is to introduce a <b>very</b> rudimentary query
 * builder. It should only be used to fetch data from persistent Storage. The logic
 * for modifying Persistent storage is <b>not</b> included in this interface. The Queryable
 * interface is only here to do transactions that do not depend on the state of the
 * application.
 */
public interface QueryAble
{
    /**
     * Add a column in the SELECT clause.
     * @param column    The column to be added.
     * @return  The this pointer.
     */
    QueryAble select(String column);

    /**
     * Select all columns in the SELECT clause.
     * @return  The this pointer.
     */
    QueryAble select();

    /**
     * Add multiple columns in the SELECT clause.
     * @param column    An array of all columns that need to be selected.
     * @return  The this pointer.
     */
    QueryAble select(String[] column);

    /**
     * Specify a WHERE clause with the given comparator.
     * @param comparator    The comparator to specify a where clause with.
     * @return  The this pointer.
     */
    QueryAble where(Comparator comparator);

    /**
     * Specify a WHERE clause with multiple comparators.
     * @param comparators   The comparators used to specify the WHERE clause with.
     * @return  The this pointer.
     */
    QueryAble where(Comparator[] comparators);

    /**
     * Specify a WHERE clause by forcing equality between the value of the column with the
     * name equal to lhs and the value of rhs.
     * @param lhs   The name of the column to be compared.
     * @param rhs   The value the respective column needs to be equal to.
     * @return The this pointer.
     */
    QueryAble where(String lhs, String rhs);

    /**
     * Executes the query and returns the results.
     * @return The results
     */
    Object get();

    /**
     * Executes the query and possibly store some results somewhere in volatile storage.
     */
    void execute();
}
