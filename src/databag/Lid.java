
package databag;
import facades.Model;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import java.time.LocalDate;

public class Lid extends Model<Lid>
{
    public Lid() 
    {
        super(Lid.class, "Lid");
    }

    
    public String getRijksregisternummer()
    {
        return (String) getField("rijksregisternummer");
    }

    public void setRijksregisternummer(Rijksregisternummer rijksregisternummer) 
    {
        setField("rijksregisternummer", rijksregisternummer);
    }

    public String getNaam() {
        return (String)getField("naam");
    }

    public void setNaam(String naam) {
         setField ("naam" , naam);
    }

    public String getVoornaam() {
        return (String)getField("voornaam");
    }

    public void setVoornaam(String voornaam) {
         setField("voornaam", voornaam);
    }

    public Geslacht getGeslacht() {
        return Geslacht.valueOf((String) getField("geslacht"));
    }

    public void setGeslacht(Geslacht geslacht) {
         setField("geslacht" , geslacht);
    }

    public String getTelnr() {
        return (String)getField("telnr");
    }

    public void setTelnr(String telnr) {
        setField("telnr" , telnr);
    }

    public String getEmailadres() {
        return (String)getField("emailadres");
    }

    public void setEmailadres(String email) {
         setField("emailadres", email);
    }

    public LocalDate getStart_lidmaatschap() {
        return LocalDate.parse((String) getField("start_lidmaatschap"));
    }

    public void setStart_lidmaatschap(LocalDate start_lidmaatschap) {
        setField("start_lidmaatschap" , start_lidmaatschap);
    }

    public LocalDate getEinde_lidmaatschap() {
        return LocalDate.parse((String)getField("einde_lidmaatschap"));
    }

    public void setEinde_lidmaatschap(LocalDate einde_lidmaatschap) {
         setField("einde_lidmaatschap ", einde_lidmaatschap);
    }

    public String getOpmerkingen() {
        return (String)getField("opmerkingen");
    }

    public void setOpmerkingen(String opmerkingen) {
         setField("opmerkingen", opmerkingen);
    }

    
    @Override
    public String toString() {
        return "Lid{" + "rijksregisternummer=" + getRijksregisternummer() + ", naam=" + getNaam() + ", voornaam=" + getVoornaam() + ", geslacht=" + getGeslacht() + ", telnr=" + getTelnr() + ", email=" + getEmailadres() + ", start_lidmaatschap=" + getStart_lidmaatschap() + ", einde_lidmaatschap=" + getEinde_lidmaatschap() + ", opmerkingen=" + getOpmerkingen() + '}';
    }

}
