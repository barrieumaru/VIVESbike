
package databag;

import datatype.Standplaats;
import datatype.Status;
import facades.Model;
;

/**
 * A Specialized model representing the database layer
 */
public class Fiets extends Model<Fiets>
{
    public Fiets()
    {
        super(Fiets.class, "Fiets");
    }

    public Standplaats getStandplaats() 
    {
        return Standplaats.valueOf((String) getField("standplaats"));
    }

    public void setStandplaats( Standplaats standplaats)
    {
        setField("standplaats", standplaats.toString());
    }

    public Status getStatus()
    {
        return Status.valueOf((String) getField("status"));
    }

    public void setStatus( Status status)
    {
        setField("status", status.toString());
    }

    
    public Integer getRegistratienummer()
    {
        return (Integer) getField("registratienummer");
    }

    public void setRegistratienummer(Integer registratienummer) 
    {
        setField("registratienummer", registratienummer);
    }

    
    public String getOpmerking()
    {
        return (String) getField("opmerkingen");
    }

    public void setOpmerking(String opmerking) 
    {
        setField("opmerkingen", opmerking);
    }


}
