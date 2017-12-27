package facades;

import database.connect.ConnectionManager;
import exception.DBException;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This is a Facade abstracting all interaction between the persistent and volatile storage of this application.
 * This class is built to work with a connection to the mysql server and specializations of models in this application.
 * Rudimentary queries are meant to be abstracted with methods such as find, update and store, but direct access is
 * also possible through the connection. <br />
 * A model can be specialized with its own setters and getters to be made type safe or it can simply be
 * specialized by specifying its database table name during in the super constructor. The correct type will be deduced
 * through metaqueries the first time a specialized model is created. (lazy initialization)
 */
public abstract class Model<T extends Model> implements QueryAble
{
    // The fields this Model is maintaining.
    private HashMap<String, FieldData> fields = new HashMap<>();
    // The table this model is connected to.
    private String table;
    // To Connection to the persistent storage.
    private Connection connection;
    // The query this Model is maintaining.
    private QueryBuilder query;
    // Variable keeping track of the parametrized Model that is being used.
    // We unfortunately need this redundancy in order to be able to do type safe checked casting.
    // getClass does nog give us this option.
    // I'm going to go ahead and assume this design pattern has a name in Java but i don't know
    // how its called.
    private Class<T> class_;
    // A simple representation of all the primary keys.
    private ArrayList<String> primaryKeySet = new ArrayList<>();
    // A simple mapping between the database representation and the resulting set.
    // Integer is needed as the Types class in java.sql are not enum class.
    // This is used to force strict typing in the values it accepts for each individual
    // field of the model.
    
    private static HashMap<Integer, Class<?>> dBToField = new HashMap<>();
    // A simple mapping to produce a handler for every single type the Model accepts and
    // be able to 'pull' it from the ResultSet.
    
    private static HashMap<Class<?>, BiFunction<ResultSet, String, Object>> fieldToFunction = new HashMap<>();
    
    private static HashMap<Class<?>, CachedClass> cache = new HashMap<>();

    private static class CachedClass
    {
        // Variable keeping track whether or not the class is buffered.
        Boolean isBuffered;
        // A cache for storing all primaryKeySets for every specific Model.
        ArrayList<String> primaryKeySets;
        // A cache of all inspections.
        HashMap<String, FieldData> inspections;
    }

    /**
     * Constructs a new model based on the given table name.
     * @param class_ The class of the model to construct. This is needed because of type erasure.
     * @param table The table of the model belongs to.
     */
    public Model(Class<T> class_, String table)
    {
        try {
            setupConversions();
            setConnection(ConnectionManager.getConnection());
            setTable(table);
            inspectFields();
            setQuery(new QueryBuilder(getConnection(), getTable()));
            setClass_(class_);
            getCache().get(getClass()).isBuffered = true;
        }catch (DBException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC INTERFACE ---------------------------------------------------------------------------
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a new Model with the given class and trivial constructor.
     * @param class_    The runtime Model class argument. This is needed because of type erasure.
     * @param <M>       The Model to construct.
     * @return A new instance of the model.
     */
    public static <M extends Model> M constructModel( Class<M> class_)
    {
        try
        {
            return class_.cast(class_.getDeclaredConstructors()[0].newInstance());
        }
        catch ( InstantiationException | InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Find the Model with its primary key and load the contents.
     * @param primary   The primary key to load the model with.
     */
    public T find( Object primary)
    {
        ResultSet set = getQuery()  .select()
                                    .where(getPrimaryKeys().get(0), primary.toString())
                                    .get();
        try {
            if (set.next())
                fill(set);
        } catch (SQLException e) {
            e.printStackTrace();
            return cast(constructModel(getClass()));
        }
        return cast(this);
    }

    public boolean isEmpty()
    {
        for (Map.Entry<String, FieldData> x : getFields().entrySet())
        {
            if (x.getValue().getRunTimeValue() != null)
                return false;
        }
        return true;
    }

    /**
     * Find a Model with its composite primary key and load the contents.
     * @param primary   The primary key set where the key represents its name and the value the value of the key.
     */
    public T find ( Map<String, Object> primary)
    {
        Comparator[] comps = new Comparator[primary.size()];
        int i = 0;
        for (Map.Entry<String, Object> x : primary.entrySet())
        {
            comps[i] = Comparator.EQ(x.getKey(), x.getValue().toString());
            ++i;
        }
        fill(getQuery().select().where(comps).get());
        return cast(this);
    }

    /**
     * Overwrites the state of the Model to the persistent storage.
     */
    public T update()
    {
        getQuery().update(prepareFields()).where(getPrimaryComparators()).execute();
        return cast(this);
    }

    /**
     * Writes the current state to persistent storage through the connection and returns the
     * primaryKeySet of the object.
     */
    
    public ArrayList<Object> store()
    {
        getQuery().insert(prepareFields()).execute();
        return loadPrimaryKeys();
    }

    @Override
    public T select()
    {
        getQuery().select();
        return cast(this);
    }

    @Override
    public T where(String lhs, String rhs)
    {
        getQuery().where(lhs, rhs);
        return cast(this);
    }

    
    @Override
    public ArrayList<T> get()
    {
        clear();
        String[] a = new String[getPrimaryKeys().size()];
        getPrimaryKeys().toArray(a);
        getQuery().select(a);
        getQuery().select(a);
        ResultSet set = getQuery().get();
        ArrayList<T> tbr = new ArrayList<>();
        try
        {
            if (set.next()) {
                fill(set);
                tbr.add(cast(this));
            }
            while (set.next()) {
                T model = constructModel(class_);
                model.fill(set);
                tbr.add(model);
            }
            return tbr;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void clear()
    {
        for (Map.Entry<String, FieldData> field : getFields().entrySet())
            field.getValue().setRunTimeValue(null);
    }

    /**
     * Set a field equal to the given value.
     * @param name  The name of the field to be set.
     * @param value The value of the field to be set.
     */
    public void setField(String name, Object value)
    {
        if (!getFields().containsKey(name))
            throw new IllegalArgumentException("Field with name: "+name+" not found.");

        getFields().get(name).setRunTimeValue(value);
    }

    public void delete()
    {
        getQuery().delete().execute();
    }

    @Override
    public String toString()
    {
        StringBuilder tbr = new StringBuilder();
        for (HashMap.Entry<String, FieldData> entry : getFields().entrySet())
            tbr.append(entry.getKey()).append(" = ").append(entry.getValue().toString()).append(", ");
        return tbr.substring(0, tbr.length() - 3); //remove trailing comma and space
    }

    @Override
    public T select(String column)
    {
        getQuery().select(column);
        return cast(this);
    }

    @Override
    public T select(String[] column)
    {
        getQuery().select(column);
        return cast(this);
    }

    @Override
    public T where(Comparator comparator)
    {
        getQuery().where(comparator);
        return cast(this);
    }

    @Override
    public T where(Comparator[] comparators)
    {
        getQuery().where(comparators);
        return cast(this);
    }

    @Override
    public void execute()
    {
        getQuery().execute();
    }

    /**
     * Queries the database and then allocates all columns with their respective types.
     * @throws SQLException Thrown by the connection prepareStatement execution.
     */
    private void allocateFields() throws SQLException
    {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + getTable());
        statement.execute();
        ResultSetMetaData columns = statement.getResultSet().getMetaData();
        for (int i = 1; i <= columns.getColumnCount(); ++i) { //index starts at one...
            // Load query data and put the name of the column with a rune time type checkable FieldData.
            getFields().put(columns.getColumnName(i), new FieldData(dBToField.get(columns.getColumnType(i))));
        }
    }

    /**
     * Setup the conversion fields for the models
     */
    private void setupConversions()
    {
        if (getFields().isEmpty())
        {
            setUpField();
            setupFieldToFunction();
        }
    }

    /**
     * Setup a mapping between de database types deduced by PreparedStatement and
     * the corresponding runtime class.
     */
    private static void setUpField()
    {
        dBToField.put(Types.CHAR, String.class);
        dBToField.put(Types.ARRAY, Object[].class);
        dBToField.put(Types.BIGINT, BigDecimal.class);
        dBToField.put(Types.VARCHAR, String.class);
        dBToField.put(Types.BINARY, Object.class);
        dBToField.put(Types.BIT, Object.class);
        dBToField.put(Types.BLOB, Object.class);
        dBToField.put(Types.LONGNVARCHAR, String.class);
        dBToField.put(Types.LONGVARBINARY, Object.class);
        dBToField.put(Types.LONGVARCHAR, String.class);
        dBToField.put(Types.TINYINT, Integer.class);
        dBToField.put(Types.SMALLINT, Integer.class);
        dBToField.put(Types.INTEGER, Integer.class);
        dBToField.put(Types.BIGINT, BigDecimal.class);
        dBToField.put(Types.FLOAT, Float.class);
        dBToField.put(Types.DOUBLE, Double.class);
        dBToField.put(Types.DECIMAL, BigDecimal.class);
        dBToField.put(Types.DATE, Date.class);
        dBToField.put(Types.DATE, Date.class);
        dBToField.put(Types.TIMESTAMP, Timestamp.class);
        dBToField.put(Types.TIME, Time.class);
    }

    /**
     * Setup the mapping between the runtime time and the corresponding handler to pull it from
     * the ResultSet.
     */
    private static void setupFieldToFunction()
    {
        // The normal code styling is changed here as there is a lot of noise. The only part that is important is the
        // method that is invoked on the ResultSet variable.
        // This will have to do until we find a solution to the parameter binding problem that does not involve forced
        // try catch modules and a instead a direct reference to a function.
        fieldToFunction.put(String.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getString(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Integer.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getInt(columnName_); }
            catch (SQLException e) { return null;}
        });
        fieldToFunction.put(BigDecimal.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getBigDecimal(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Float.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getFloat(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Double.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getDouble(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Date.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getDate(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Timestamp.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getTimestamp(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Time.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getTime(columnName_); }
            catch (SQLException e) { return null; }
        });
        fieldToFunction.put(Object.class, (ResultSet set_, String columnName_) -> {
            try { return set_.getObject(columnName_); }
            catch (SQLException e) { return null; }
        });
    }

    /**
     * Inspects the class and stores or load its data in relation to the connection.
     */
    private void inspectFields()
    {
        try
        {
            setConnection(ConnectionManager.getConnection());
            if (!isCached()) { // Check if the fields have already been inspected once.
                allocateFields();
                allocatePrimaryKeys();
                cacheInspection();
            } else
                loadFromCache();
        }
        catch( DBException | SQLException e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * Inspects and allocates the names of the primary keys.
     */
    private void allocatePrimaryKeys()
    {
        try
        {
            PreparedStatement statement = getConnection().prepareStatement("SHOW KEYS FROM " + getTable() + " WHERE Key_name = 'PRIMARY'");
            statement.execute();
            ResultSet results = statement.getResultSet();
            while (results.next())
                getPrimaryKeys().add(results.getString("Column_name"));
        }
        catch (SQLException e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * Checks if the Class already has its fields cached.
     * @return True if the Class is already cached.
     */
    private boolean isCached()
    {
        if (getCache().getOrDefault(getClass(), null) == null)
            return false;
        else
            return getCache().get(getClass()).isBuffered;
    }

    /**
     * Loads all fields of the inspected class into the object.
     */
    private void loadFromCache()
    {
        HashMap<String, FieldData> clone = new HashMap<>();
        for (Map.Entry<String, FieldData> x : getCache().get(getClass()).inspections.entrySet())
            clone.put(x.getKey(), x.getValue().clone());
        setFields(clone);
        primaryKeySet = getCache().get(getClass()).primaryKeySets;
    }

    /**
     * Puts all fields of itself in the cache of inspections.
     */
    private void cacheInspection()
    {
        HashMap<String, FieldData> clone = new HashMap<>();
        for (Map.Entry<String, FieldData> x : getFields().entrySet())
            clone.put(x.getKey(), x.getValue().clone());
        getCache().put(getClass(),  new CachedClass());
        getCache().get(getClass()).inspections = clone;
        getCache().get(getClass()).primaryKeySets = getPrimaryKeys();
    }


    private void setTable(String table)
    {
        this.table = table;
    }

    private String getTable()
    {
        return table;
    }

    /**
     * Returns all the fields of the Model.
     * @return The fields of the  model.
     */
    private HashMap<String, FieldData> getFields()
    {
        return fields;
    }

    private void setFields(HashMap<String, FieldData> resultset)
    {
        this.fields = resultset;
    }

    /**
     * Returns the connection.
     *
     * @return connection
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Set the connection
     * @param connection The connection associated with the class.
     */
    private void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Retunrs a field of the object.
     * @param name  The name of the field.
     * @return The value of the field associated with the given name.
     */
    public Object getField(String name)
    {
        return getFields().get(name).getRunTimeValue();
    }

    /**
     * Returns the query.
     *
     * @return query
     */
    private QueryBuilder getQuery()
    {
        return query;
    }

    private void setQuery(QueryBuilder query)
    {
        this.query = query;
    }

    /**
     * Returns the primaryKeySet.
     *
     * @return primaryKeySet
     */
    public ArrayList<String> getPrimaryKeys()
    {
        return primaryKeySet;
    }

    /**
     * Loads the primary key values into another set.
     * @return  The primary keys.
     */
    
    private ArrayList<Object> loadPrimaryKeys()
    {
        ArrayList<Object> intersect = new ArrayList<>();
        for(String x : getPrimaryKeys())
            intersect.add(getField(x));
        return intersect;
    }

    /**
     * Fill the model with the current state of the of the ResultSet.
     * @param results   The results to be loaded in the Model.
     *
     * @note this method needs to be protected because of some template hiccups,more specifically in the method fill
     */
    @SuppressWarnings("WeakerAccess")
    protected void fill(ResultSet results)
    {
        for(Map.Entry<String, FieldData> x : getFields().entrySet())
        {
            //noinspection SuspiciousMethodCalls
            BiFunction<ResultSet, String, Object> handler = fieldToFunction.get(x.getValue().getRuntimeType());
            Object runtimeValue = handler.apply(results, x.getKey());
            x.getValue().setRunTimeValue(runtimeValue);
        }
    }

    /**
     * Prepares the field of the model to be used in a query.
     * @return  The prepared fields.
     */
    
    private HashMap<String, String> prepareFields()
    {
        HashMap<String, String> fields = new HashMap<>();
        for (Map.Entry<String, FieldData> x : getFields().entrySet())
        {
            if (x.getValue().getRunTimeValue() == null)
                fields.put(x.getKey(), null);
            else
                fields.put(x.getKey(), x.getValue().getRunTimeValue().toString());
        }
        return fields;
    }

    /**
     * Returns all comparators to identify a row by its primary keys.
     * @return  The comparators.
     */
    
    private Comparator[] getPrimaryComparators()
    {
        ArrayList<Object> values = loadPrimaryKeys();
        Comparator[] comp = new Comparator[values.size()];
        for (int i = 0; i < values.size(); ++i)
            comp[i] = Comparator.EQ(getPrimaryKeys().get(i), values.get(i).toString());
        return comp;
    }

    /**
     * Checked cast.
     */
    private T cast (Object toCast)
    {
        return class_.cast(toCast);
    }

    private void setClass_(Class<T> class_)
    {
        this.class_ = class_;
    }

    /**
     * Returns the cache.
     *
     * @return cache
     */
    
    private static HashMap<Class<?>, CachedClass> getCache()
    {
        return cache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Model)) return false;

        Model<?> model = (Model<?>) o;

        return getFields().equals(model.getFields()) &&
                getTable().equals(model.getTable()) &&
                getQuery().equals(model.getQuery()) &&
                class_.equals(model.class_) &&
                primaryKeySet.equals(model.primaryKeySet);
    }

    @Override
    public int hashCode()
    {
        int result = getFields().hashCode();
        result = 31 * result + getTable().hashCode();
        result = 31 * result + getConnection().hashCode();
        result = 31 * result + getQuery().hashCode();
        result = 31 * result + class_.hashCode();
        result = 31 * result + primaryKeySet.hashCode();
        return result;
    }
}
