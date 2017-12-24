package facades;

import exception.UncheckedSQLException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

/**
 * A class representing a Query builder in order to add an abstraction layer to the query language used.
 * The philosophy behind this class is to manage the connection and prepared statements in a safer,
 * programatically easier and cleaner way in order to retrieve the ResultsSet.
 */
class QueryBuilder implements QueryAble
{
    // The connection as the main transaction point between persistent and volatile storage.
    private Connection connection;
    // The name of the table in the persistent storage.
    private String table;
    // A collection representing all columns to be selected.
    @NotNull
    private HashSet<String> selections = new HashSet<>();
    // A collection of comparisons to specify a subset to query.
    @NotNull
    private ArrayList<Comparator> comparisons = new ArrayList<>();
    // A collection of columns and values to be modified in some way.
    @NotNull
    private HashMap<String, String> modifiers = new HashMap<>();
    // A variable simply representing what mode this query is in. Possible modes are
    // enumerated in QueryMode.
    private QueryMode queryMode;

    /**
     * Enum representing all possible states the builder can be in.
     */
    private enum QueryMode
    {
        // The query is not yet definitely committed to a certain mode
        UNDECIDED,
        // The query will be a SELECT statement.
        SELECT,
        // The query will be an INSERT statement.
        INSERT,
        // The query will be an UPDATE statement.
        UPDATE,
        // The query will be a DELETE statement.
        DELETE
    }

    /**
     * Construct a new QueryBuilder with the given connection and table.
     * @param connection The connection the QueryBuilder will use to communicate between
     *                   persistent storage and itself.
     * @param table the table the QueryBuilder communicates with in the persistent storage.
     */
    QueryBuilder(Connection connection, String table)
    {
        setConnection(connection);
        setTable(table);
        setQueryMode(QueryMode.UNDECIDED);
    }

    /**
     * Simply return the state of the builder in SQL format.
     * @return  The sql string.
     */
    @NotNull String toSql()
    {
        StringBuilder tbr = new StringBuilder(getQueryMode().toString());
        if (getQueryMode() != QueryMode.UNDECIDED)
        {
            if (getQueryMode() == QueryMode.SELECT)
                selectSql(tbr);
            if (getQueryMode() == QueryMode.INSERT)
                insertSql(tbr);
            if (getQueryMode() == QueryMode.UPDATE)
                updateSql(tbr);
            if (getQueryMode() == QueryMode.DELETE)
                deleteSql(tbr);
        }
        return tbr.toString();
    }

    private void deleteSql(@NotNull StringBuilder sql)
    {
        sql.append(" FROM ").append(getTable());
        comparisonsSql(sql);
    }

    private void updateSql(@NotNull StringBuilder sql)
    {
           if (getModifiers().size() != 0)
           {
               sql.append(' ').append(getTable()).append(" SET ");
               for(Map.Entry<String,String> x : getModifiers().entrySet())
                   sql.append(x.getKey()).append(" = ").append('\'').append(x.getValue()).append("\'").append(", ");
               sql.delete(sql.length() - 2, sql.length());
               comparisonsSql(sql);
           }
           else
               setQueryMode(QueryMode.UNDECIDED);
    }

    private void insertSql(@NotNull StringBuilder sql)
    {
        if (getModifiers().size() != 0)
        {
            sql.append(" INTO ").append(getTable()).append(" (");
            for (String x : getModifiers().keySet())
                sql.append(x).append(", ");
            sql.delete(sql.length() - 2, sql.length());
            sql.append(") VALUES (");
            for (Object x : getModifiers().values()) {
                if (x == null)
                    x = "NULL";
                sql.append('\'').append(x.toString()).append('\'').append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            sql.append(')');
        }
        else
            setQueryMode(QueryMode.UNDECIDED);
    }

    private void comparisonsSql(@NotNull StringBuilder sql)
    {
        if (getComparisons().size() != 0)
        {
            sql.append(" WHERE ");
            for (Comparator x : getComparisons())
                sql.append(x.toString()).append(", ");
            sql.delete(sql.length() - 2, sql.length());
        }
    }

    private void selectSql(@NotNull StringBuilder sql)
    {
        if (getSelections().size() != 0)
        {
            sql.append(' ');
            for (String x : getSelections())
                sql.append(x).append(", ");
            sql.delete(sql.length() - 2, sql.length());
        }
        sql.append(" FROM ").append(getTable());
        comparisonsSql(sql);
    }

    @NotNull
    @Override
    public QueryBuilder select(String[] selections)
    {
        if (getQueryMode() == QueryMode.SELECT || getQueryMode() == QueryMode.UNDECIDED)
        {
            if (getSelections().size() >= 1 && getSelections().iterator().next().equals("*"))
                return this;

            setQueryMode(QueryMode.SELECT);
            getSelections().addAll(Arrays.asList(selections));
            return this;
        }
        else
            throw new RuntimeException();
    }

    @NotNull
    @Override
    public QueryBuilder select(String selection)
    {
        if (getQueryMode() == QueryMode.SELECT || getQueryMode() == QueryMode.UNDECIDED)
        {
            if (getSelections().size() >= 1 && getSelections().iterator().next().equals("*"))
                return this;

            setQueryMode(QueryMode.SELECT);
            getSelections().add(selection);
            return this;
        }
        else
            throw new RuntimeException();
    }

    @NotNull
    @Override
    public QueryBuilder select()
    {
        if (getQueryMode() == QueryMode.SELECT || getQueryMode() == QueryMode.UNDECIDED)
        {
            setQueryMode(QueryMode.SELECT);
            getSelections().clear();
            getSelections().add("*");
            return this;
        }
        else
            throw new RuntimeException();
    }

    public ResultSet get()
    {
        try {
            PreparedStatement statement = getConnection().prepareStatement(toSql());
            statement.execute();
            reset();
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    public void execute()
    {
        try {
            getConnection().prepareStatement(toSql()).execute();
            reset();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UncheckedSQLException(e);
        }
    }

    @NotNull
    public QueryBuilder where(Comparator comparison)
    {
        getComparisons().add(comparison);
        return this;
    }

    @NotNull
    public QueryBuilder where(Comparator[] comparison)
    {
        getComparisons().addAll(Arrays.asList(comparison));
        return this;
    }

    @NotNull
    @Override
    public QueryBuilder where(String lhs, String rhs)
    {
        return where(Comparator.EQ(lhs, rhs));
    }

    /**
     * Resets the Query in order to have a fresh start.
     */
    public void reset()
    {
        selections = new LinkedHashSet<>();
        modifiers = new HashMap<>();
        comparisons = new ArrayList<>();
        setQueryMode(QueryMode.UNDECIDED);
    }

    /**
     * Insert new values into a column where the keys represent the column names and the values represent
     * the values in the persistent storage.
     * There is a small note to be made that is of no real effect unless you rely on the toSQL feature
     * in very fringe cases: As a Map data structures do not define a specific order, so does the column
     * order in the SQL query.
     * @param columnValues  The Map representing the column names and their values.
     * @return  The this pointer.
     */
    @NotNull
    public QueryBuilder insert(@NotNull Map<String, String> columnValues)
    {
        if (getQueryMode() == QueryMode.UNDECIDED)
            setQueryMode(QueryMode.INSERT);
        if (getQueryMode() == QueryMode.INSERT || getQueryMode() == QueryMode.UPDATE)
        {
            for (Map.Entry<String, String> x : columnValues.entrySet())
                getModifiers().put(x.getKey(), x.getValue());
            return this;
        }
        else
            throw new RuntimeException();
    }

    /**
     * Insert new values into a column where the column represent the column name and the value represent
     * the value in the persistent storage.
     * There is a small note to be made that is of no real effect unless you rely on the toSQL feature
     * in very fringe cases: As a Map data structures do not define a specific order, so does the column
     * order in the SQL query.
     * @param column    The name of the column.
     * @param value The desired value of the column.
     * @return  The this pointer.
     */
    @NotNull
    public QueryBuilder insert(String column, String value)
    {
        if (getQueryMode() == QueryMode.UNDECIDED)
            setQueryMode(QueryMode.INSERT);
        if (getQueryMode() == QueryMode.INSERT || getQueryMode() == QueryMode.UPDATE)
        {
            getModifiers().put(column, value);
            return this;
        }
        else
            throw new RuntimeException();
    }

    /**
     * Updates the value from a column where the column represent the column name and the value represent
     * the value in the persistent storage.
     * There is a small note to be made that is of no real effect unless you rely on the toSQL feature
     * in very fringe cases: As a Map data structures do not define a specific order, so does the column
     * order in the SQL query.
     * @param column    The column to be updated.
     * @param value     The value to update the column with.
     * @return  The this pointer.
     */
    @NotNull
    public QueryBuilder update(String column, String value)
    {
        if (getQueryMode() == QueryMode.UNDECIDED)
            setQueryMode(QueryMode.UPDATE);
        return insert(column, value);
    }

    /**
     * Updates the values from columns where the keys represent the column names and the values represent
     * the values in the persistent storage.
     * There is a small note to be made that is of no real effect unless you rely on the toSQL feature
     * in very fringe cases: As a Map data structures do not define a specific order, so does the column
     * order in the SQL query.
     * @param values    The column and value pairs to update with.
     * @return  The this pointer.
     */
    @NotNull
    public QueryBuilder update(@NotNull Map<String, String> values)
    {
        if (getQueryMode() == QueryMode.UNDECIDED)
            setQueryMode(QueryMode.UPDATE);
        return insert(values);
    }

    /**
     * Deletes all entries specified by the Comparators accumulated in the Query builder.
     */
    @NotNull
    public QueryBuilder delete()
    {
        if (getQueryMode() == QueryMode.UNDECIDED)
            setQueryMode(QueryMode.DELETE);
        if (getQueryMode() != QueryMode.DELETE)
            throw new RuntimeException();
        return this;
    }

    /**
     * Returns the selections.
     * @return The selections.
     * @note this method is protected because Models need access to selections in order to ALWAYS be able to select primary keys.
     */
    @NotNull
    private HashSet<String> getSelections()
    {
        return selections;
    }


    /**
     * Returns the connection.
     *
     * @return connection
     */
    private Connection getConnection()
    {
        return connection;
    }

    private void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Returns the table.
     *
     * @return table
     */
    private String getTable()
    {
        return table;
    }

    private void setTable(final String table)
    {
        this.table = table;
    }

    /**
     * Returns the queryMode.
     *
     * @return queryMode
     */
    private QueryMode getQueryMode()
    {
        return queryMode;
    }

    private void setQueryMode(final QueryMode queryMode)
    {
        this.queryMode = queryMode;
    }

    /**
     * Returns the comparisons.
     *
     * @return comparisons
     */
    @NotNull
    private ArrayList<Comparator> getComparisons()
    {
        return comparisons;
    }

    @NotNull
    private HashMap<String, String> getModifiers()
    {
        return modifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryBuilder)) return false;

        QueryBuilder that = (QueryBuilder) o;

        return getTable().equals(that.getTable()) &&
                getSelections().equals(that.getSelections()) &&
                getComparisons().equals(that.getComparisons()) &&
                getModifiers().equals(that.getModifiers()) &&
                getQueryMode() == that.getQueryMode();
    }

    @Override
    public int hashCode() {
        int result = getConnection().hashCode();
        result = 31 * result + getTable().hashCode();
        result = 31 * result + getSelections().hashCode();
        result = 31 * result + getComparisons().hashCode();
        result = 31 * result + getModifiers().hashCode();
        result = 31 * result + getQueryMode().hashCode();
        return result;
    }
}
