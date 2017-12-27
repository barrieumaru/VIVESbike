package database;

import databag.Lid;
import databag.Rit;
import databag.Fiets;

import java.util.ArrayList;

public class LidDB implements InterfaceLidDB {
    
    private Lid lid = new Lid();
    private Rit rit = new Rit();
    private Fiets fiets = new Fiets();
    
    @Override
    public void toevoegenLid(Lid lid)
    {
        lid.store();
        
    }

    @Override
    public void wijzigenLid(Lid lid)  
    {
        lid.update();
    }

    @Override
    public void uitschrijvenLid(String rr)  
    {
        if (!rit.where("lid_rijksregisternummer", rr).get().isEmpty())
             throw new RuntimeException();
        
        lid.find(rr);
        lid.delete();
    }

    
    @Override
    public Lid zoekLid(String rijksregisternummer) 
    {
        return lid.find(rijksregisternummer);
    }

    
    @Override
    public ArrayList<Lid> zoekAlleLeden()  {
        return lid.select("*").get();
    }

   
  
}
