package database;

import databag.Fiets;
import datatype.Status;
import facades.Model;

;

import java.util.ArrayList;

public class FietsDB implements InterfaceFietsDB
{
    
    @Override
    public Integer toevoegenFiets( Fiets fiets)
    {
        return (Integer) fiets.store().get(0);
    }

    @Override
    public void wijzigenToestandFiets( Integer regnr,  Status status)
    {
        Fiets fiets = new Fiets();
        fiets.find(regnr);
        fiets.setStatus(status);
        fiets.update();
    }

    @Override
    public void wijzigenOpmerkingFiets( Integer regnr, String opmerking)
    {
        Fiets fiets = new Fiets();
        fiets.find(regnr);
        fiets.setOpmerking(opmerking);
        fiets.update();
    }

    
    @Override
    public Fiets zoekFiets( Integer regnr)
    {
        return Model.constructModel(Fiets.class).find(regnr);
    }

    
    @Override
    public ArrayList<Fiets> zoekAlleFietsen()
    {
        return Model.constructModel(Fiets.class).select().get();
    }
}
