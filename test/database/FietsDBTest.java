package database;

import databag.Fiets;
import datatype.Standplaats;
import datatype.Status;
import facades.Comparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FietsDBTest
{
    private int key = 12345;
    private FietsDB db = new FietsDB();

    @BeforeEach
    void setUp()
    {
        Fiets fiets = new Fiets();
        if (fiets.find(key).isEmpty())
        {
            fiets.setOpmerking("TEST OPMERKING");
            fiets.setStandplaats(Standplaats.Kortrijk);
            fiets.setRegistratienummer(key);
            fiets.setStatus(Status.actief);
            fiets.store();
        }
    }

    @AfterEach
    void tearDown()
    {
        (new Fiets()).where(Comparator.GT("registratienummer", "0")).delete();
    }

    @Test
    void toevoegenFiets()
    {
        Fiets toevoegen = new Fiets();
        toevoegen.setStatus(Status.actief);
        toevoegen.setRegistratienummer(key+1);
        toevoegen.setStandplaats(Standplaats.Brugge);
        db.toevoegenFiets(toevoegen);
        assertEquals(toevoegen, (new Fiets()).find(key+1));
    }

    @Test
    void wijzigenToestandFiets()
    {
        db.wijzigenToestandFiets(key, Status.uit_omloop);
        assertEquals(Status.uit_omloop, (new Fiets()).find(key).getStatus());
    }

    @Test
    void wijzigenOpmerkingFiets()
    {
        db.wijzigenOpmerkingFiets(key, "een andere opmerking");
        assertEquals("een andere opmerking", (new Fiets()).find(key).getOpmerking());
    }

    @Test
    void zoekFiets()
    {
        assertEquals((new Fiets()).find(key), db.zoekFiets(key));
    }

    @Test
    void zoekAlleFietsen()
    {
        assertEquals((Integer) key, db.zoekAlleFietsen().get(0).getRegistratienummer());
    }
}