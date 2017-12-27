package database;

import databag.Rit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RitDB implements InterfaceRitDB {

    @NotNull
    @Override
    public Integer toevoegenRit(Rit rit)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void afsluitenRit(Rit rit)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public ArrayList zoekAlleRitten()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public Rit zoekRit(Integer ritID)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int zoekEersteRitVanLid(String rr)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public ArrayList zoekActieveRittenVanLid(String rr)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public ArrayList zoekActieveRittenVanFiets(Integer regnr)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
}
