
package databag;

import datatype.Rijksregisternummer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import facades.Model;

public class Rit extends Model<Rit>
{

    

    public Rit() 
    {
        super(Rit.class, "Rit");
    }

    public Integer getRitID() {
        return (Integer) getField("ritID");
    }

    public void setRitID(Integer RitID) {
         setField("ritID", RitID);
    }

    public LocalDateTime getStarttijd() {
        return LocalDateTime.parse((String) getField("starttijd"));
    }

    public void setStarttijd(LocalDateTime starttijd) {
         setField("starttijd", starttijd);
    }

    public LocalDateTime getEindtijd() {
        return LocalDateTime.parse((String) getField("eindtijd"));
    }

    public void setEindtijd(LocalDateTime eindtijd) {
         setField("eindtijd", eindtijd);
    }

    public BigDecimal getPrijs() {
        return (BigDecimal)getField("prijs");
    }

    public void setPrijs(BigDecimal prijs) {
         setField("prijs", prijs);
    }

    
    public String getLidRijksregisternummer() {
        
            return (String) getField("getRijksregisternummer()");
        
    }

    public void setLidRijksregisternummer(Rijksregisternummer lidRijksregisternummer) {
         setField("lidRijksregisternummer",lidRijksregisternummer);
    }

    public int getFietsRegistratienummer() {
        return (int) getField("fietsRegistratienummer");
    }

    public void setFietsRegistratienummer(int fietsRegistratienummer) {
         setField("fietsRegistratienummer", fietsRegistratienummer);
    }

}
