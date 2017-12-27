/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import databag.Fiets;
import datatype.Status;
import exception.DBException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceFietsDB {

    @NotNull Integer toevoegenFiets(Fiets fiets) throws DBException;

    void wijzigenToestandFiets(Integer regnr, Status status) throws Exception;

    void wijzigenOpmerkingFiets(Integer regnr, String opmerking) throws Exception;

    @NotNull Fiets zoekFiets(Integer regnr) throws Exception;

    @NotNull ArrayList<Fiets> zoekAlleFietsen() throws Exception;
}
