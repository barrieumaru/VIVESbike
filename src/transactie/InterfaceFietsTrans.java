/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import databag.Fiets;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceFietsTrans {

    @NotNull Integer toevoegenFiets(Fiets fiets) throws Exception;

    void wijzigenActiefNaarHerstel(Integer regnr) throws Exception;

    void wijzigenActiefNaarUitOmloop(Integer regnr) throws Exception;

    void wijzigenHerstelNaarActief(Integer regnr) throws Exception;

    void wijzigenHerstelNaarUitOmloop(Integer regnr) throws Exception;

    void wijzigenOpmerkingFiets(Integer regnr, String opmerking) throws Exception;

    @NotNull Fiets zoekFiets(Integer registratienummer) throws Exception;

    @NotNull ArrayList<Fiets> zoekAlleFietsen() throws Exception;

}
