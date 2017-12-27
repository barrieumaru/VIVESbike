
package transactie;

import databag.Fiets;
import facades.Repository;

import java.util.ArrayList;

public class FietsTrans extends Repository implements InterfaceFietsTrans {

    
    @Override
    public Integer toevoegenFiets(Fiets fiets)
    {
        if (!isStoreable(fiets))
            throw new RuntimeException();//exceptions moeten nog aagepaste worden
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenActiefNaarHerstel(Integer regnr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenActiefNaarUitOmloop(Integer regnr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenHerstelNaarActief(Integer regnr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenHerstelNaarUitOmloop(Integer regnr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenOpmerkingFiets(Integer regnr, String opmerking)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public Fiets zoekFiets(Integer registratienummer)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public ArrayList<Fiets> zoekAlleFietsen()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
