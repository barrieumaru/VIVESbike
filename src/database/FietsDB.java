package database;

import databag.Fiets;
import datatype.Status;
import facades.Model;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FietsDB implements InterfaceFietsDB
{
    @NotNull
    @Override
    public Integer toevoegenFiets(@NotNull Fiets fiets)
    {
        return (Integer) fiets.store().get(0);
    }

    @Override
    public void wijzigenToestandFiets(@NotNull Integer regnr, @NotNull Status status)
    {
        Fiets fiets = new Fiets();
        fiets.find(regnr);
        fiets.setStatus(status);
        fiets.update();
    }

    @Override
    public void wijzigenOpmerkingFiets(@NotNull Integer regnr, String opmerking)
    {
        Fiets fiets = new Fiets();
        fiets.find(regnr);
        fiets.setOpmerking(opmerking);
        fiets.update();
    }

    @NotNull
    @Override
    public Fiets zoekFiets(@NotNull Integer regnr)
    {
        return Model.constructModel(Fiets.class).find(regnr);
    }

    @NotNull
    @Override
    public ArrayList<Fiets> zoekAlleFietsen()
    {
        return Model.constructModel(Fiets.class).select().get();
    }
}
