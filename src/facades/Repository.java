/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import facades.Model;

/**
 *
 * @author Barrie
 */
abstract public class Repository 
{
    protected Boolean isStoreable(Model model)
    {
        return true;
    }
}
