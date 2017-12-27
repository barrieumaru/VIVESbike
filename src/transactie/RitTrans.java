/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import databag.Rit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RitTrans implements InterfaceRitTrans {

    @NotNull
    @Override
    public Integer toevoegenRit(Rit rit)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void afsluitenRit(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public ArrayList zoekAlleRitten()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public Rit zoekRit(Integer ritID)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public Integer zoekEersteRitVanLid(String rr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public ArrayList zoekActieveRittenVanLid(String rr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NotNull
    @Override
    public ArrayList zoekActieveRittenVanFiets(Integer regnr)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
