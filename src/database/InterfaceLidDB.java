/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import databag.Lid;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceLidDB {

    void toevoegenLid(Lid lid) throws Exception;

    void wijzigenLid(Lid lid) throws Exception;

    void uitschrijvenLid(String rr) throws Exception;

    @NotNull Lid zoekLid(String rijksregisternummer) throws Exception;

    @NotNull
    ArrayList<Lid> zoekAlleLeden() throws Exception;

}
