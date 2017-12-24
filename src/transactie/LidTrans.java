package transactie;

import databag.Lid;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LidTrans implements InterfaceLidTrans {

    @Override
    public void toevoegenLid(Lid l)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenLid(Lid teWijzigenLid)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void uitschrijvenLid(String rr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public Lid zoekLid(String rijksregisternummer)  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public ArrayList<Lid> zoekAlleLeden()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
