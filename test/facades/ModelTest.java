package facades;

import databag.Fiets;
import datatype.Standplaats;
import datatype.Status;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.*;

class ModelTest 
{

    private static Integer key = 12345;
    private Fiets model;

    @Before
    void setUp()
    {
        model = Model.constructModel(Fiets.class);
        if (model.find(key).isEmpty()) {
            model.setField("opmerkingen", "test opmerking");
            model.setField("status", Status.actief.toString());
            model.setField("registratienummer", key);
            model.setField("standplaats", Standplaats.Kortrijk.toString());
            model.store();
            model.setField("registratienummer", (Integer) model.getField("registratienummer") + 1);
            model.store();
        }
        model = Model.constructModel(Fiets.class);
    }

    @Test
    void simpleFind()
    {
        model.find(key);
        assertEquals(key, model.getField("registratienummer"));
        assertEquals(Status.actief.toString(), model.getField("status"));
        assertEquals("test opmerking", model.getField("opmerkingen"));
        assertEquals(Standplaats.Kortrijk.toString(), model.getField("standplaats"));
    }

    @Test
    void simpleSelect()
    {
        model.select("status").where("registratienummer", key.toString()).get();
        assertEquals(key, model.getField("registratienummer"));
        assertEquals(Status.actief.toString(), model.getField("status"));
        assertEquals(null, model.getField("opmerkingen"));
        assertEquals(null, model.getField("standplaats"));
    }

    @Test
    void multipleSelectTest()
    {
        ArrayList<Fiets> results =
                model.select().where("standplaats", Standplaats.Kortrijk.toString()).get();
        assertEquals(model.find(key), results.get(0));
        assertEquals(model.find(key + 1), results.get(1));
    }

    @Test
    void testPrimaryKeys()
    {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("registratienummer");
        assertEquals(keys, model.getPrimaryKeys());
    }

    @Test
    void testSimpleUpdate()
    {
        model.find(key).setOpmerking("nieuwe opmerking");
        model.update();
        assertEquals("nieuwe opmerking", model.find(key).getOpmerking());
    }

    @After

    void tearDown()
    {
        model.find(key).delete();
        model.find(key + 1).delete();
    }
}