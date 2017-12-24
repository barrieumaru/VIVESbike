
package databag;

import datatype.Standplaats;
import datatype.Status;
import facades.Model;
import org.jetbrains.annotations.NotNull;

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

    public void setStandplaats(@NotNull Standplaats standplaats)
    {
        setField("standplaats", standplaats.toString());
    }

    public Status getStatus()
    {
        return Status.valueOf((String) getField("status"));
    }

    public void setStatus(@NotNull Status status)
    {
        setField("status", status.toString());
    }

    @NotNull
    public Integer getRegistratienummer()
    {
        return (Integer) getField("registratienummer");
    }

    public void setRegistratienummer(Integer registratienummer) 
    {
        setField("registratienummer", registratienummer);
    }

    @NotNull
    public String getOpmerking()
    {
        return (String) getField("opmerkingen");
    }

    public void setOpmerking(String opmerking) 
    {
        setField("opmerkingen", opmerking);
    }


}
