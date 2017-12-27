package facades;


;

public class Comparator
{
    private String val;
    private String lval;
    private String rval;

    private Comparator(String lval, String val, String rval)
    {
        this.lval = lval;
        this.val = val;
        this.rval = rval;
    }

    
    public String toString()
    {
        return lval + ' ' + val + " '" + rval + '\'';
    }

    public static Comparator EQ(String lval,  String rval)
    {
        return new Comparator(lval, "=", rval);
    }

    public static Comparator LT(String lval, String rval)
    {
        return new Comparator(lval, "<", rval);
    }

    public static Comparator GT(String lval,  String rval)
    {
        return new Comparator(lval, ">",  rval);
    }

    public static Comparator GET(String lval, String rval)
    {
        return new Comparator(lval, ">=", rval);
    }

    public static Comparator LET(String lval, String rval)
    {
        return new Comparator(lval, "<=", rval);
    }

    public static Comparator LIKE(String lval, String rval)
    {
        return new Comparator(lval, "LIKE", rval);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comparator)) return false;

        Comparator that = (Comparator) o;

        return val.equals(that.val) &&
                lval.equals(that.lval) &&
                rval.equals(that.rval);
    }

    @Override
    public int hashCode()
    {
        int result = val.hashCode();
        result = 31 * result + lval.hashCode();
        result = 31 * result + rval.hashCode();
        return result;
    }
}
