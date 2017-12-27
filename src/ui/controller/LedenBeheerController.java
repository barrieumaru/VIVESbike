/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import ui.VIVESbike;

/**
 * FXML Controller class
 *
 * @author Katrien.Deleu
 */
public class LedenBeheerController implements Initializable
{

    // referentie naar VIVESbike (main)
    private VIVESbike parent;

    /**
     * Referentie naar parent (start) instellen
     *
     * @param p referentie naar de runnable class die alle oproepen naar de
     * schermen bestuurt
     */
    public void setParent(VIVESbike p)
    {
        parent = p;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

}
