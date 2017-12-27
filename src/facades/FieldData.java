package facades;

/**
 * A class representing a more 'lightweight' concept such as a struct or tuple in order to keep track of all
 * inspections between a model and the connection.
 * There is no available compile time type checking as far as I know of so rudimentary generic type checking
 * needs to be done here.
 */
class FieldData
{
    // NOTE TO MY FORGETFUL SELF:
    // Please do not try to introduce types here that cannot be deduced from the connector.
    // Enums cannot successfully be deduces from there, a possible way out is to let
    // users overwrite the variables in a class, but that would require INTENSIVE
    // reflection
    
    private Class<?> runtimeType;
    
    private Object runtimeValue = null;

    FieldData( Class<?> type)
    {
        runtimeType = type;
    }

    void setRunTimeValue( Object object)
    {
        if (object != null && object.equals("NULL"))
            runtimeValue = null;
        else if (runtimeType.isInstance(object) || object == null)
            runtimeValue = object;
        else
            throw new ClassCastException();
    }

     Object getRunTimeValue()
    {
        return runtimeValue;
    }

     Class<?> getRuntimeType()
    {
        return runtimeType;
    }

    @Override
    
    public FieldData clone()
    {
        return new FieldData(runtimeType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldData)) return false;

        FieldData fieldData = (FieldData) o;

        return getRuntimeType().equals(fieldData.getRuntimeType()) &&
                (runtimeValue != null ? runtimeValue.equals(fieldData.runtimeValue) :
                        fieldData.runtimeValue == null);
    }

    @Override
    public int hashCode()
    {
        int result = getRuntimeType().hashCode();
        result = 31 * result + (runtimeValue != null ? runtimeValue.hashCode() : 0);
        return result;
    }
}
