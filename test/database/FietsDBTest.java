package database;

import databag.Fiets;
import datatype.Standplaats;
import datatype.Status;
import facades.Comparator;
import static org.junit.Assert.*;
import org.junit.*;

public class FietsDBTest
{
    public FietsDBTest()
    {
            
    }
    private int key = 12345;
    private FietsDB db = new FietsDB();

    @Before
    public void setUp()
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

    @After
    public void tearDown()
    {
        (new Fiets()).where(Comparator.GT("registratienummer", "0")).delete();
    }

    @Test
    public void toevoegenFiets()
    {
        Fiets toevoegen = new Fiets();
        toevoegen.setStatus(Status.actief);
        toevoegen.setRegistratienummer(key+1);
        toevoegen.setStandplaats(Standplaats.Brugge);
        db.toevoegenFiets(toevoegen);
        assertEquals(toevoegen, (new Fiets()).find(key+1));
    }

    @Test
    public void wijzigenToestandFiets()
    {
        db.wijzigenToestandFiets(key, Status.uit_omloop);
        assertEquals(Status.uit_omloop, (new Fiets()).find(key).getStatus());
    }

    @Test
    public void wijzigenOpmerkingFiets()
    {
        db.wijzigenOpmerkingFiets(key, "een andere opmerking");
        assertEquals("een andere opmerking", (new Fiets()).find(key).getOpmerking());
    }

    @Test
    public void zoekFiets()
    {
        assertEquals((new Fiets()).find(key), db.zoekFiets(key));
    }

    @Test
    public void zoekAlleFietsen()
    {
        assertEquals((Integer) key, db.zoekAlleFietsen().get(0).getRegistratienummer());
    }
}