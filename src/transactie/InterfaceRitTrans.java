/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import databag.Rit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceRitTrans {

    @NotNull Integer toevoegenRit(Rit rit) throws Exception;

    void afsluitenRit(Integer id) throws Exception;

    @NotNull ArrayList zoekAlleRitten() throws Exception;

    @NotNull Rit zoekRit(Integer ritID) throws Exception;

    @NotNull Integer zoekEersteRitVanLid(String rr) throws Exception;

    @NotNull ArrayList zoekActieveRittenVanLid(String rr) throws Exception;

    @NotNull ArrayList zoekActieveRittenVanFiets(Integer regnr) throws Exception;

}
