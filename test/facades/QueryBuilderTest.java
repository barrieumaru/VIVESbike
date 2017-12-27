package facades;

import database.connect.ConnectionManager;
import exception.DBException;

import java.sql.SQLException;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.*;


class QueryBuilderTest
{
    private QueryBuilder builder;
    private final static String TEST_TABLE = "unit_test_table";

    @BeforeClass
    static void setUp() throws DBException, SQLException
    {
        ConnectionManager.getConnection().prepareStatement("CREATE TABLE " + TEST_TABLE + " (test1 TEXT, test2 INT)").execute();
    }

    @Before
    void setUpEach() throws DBException
    {
        builder = new QueryBuilder(ConnectionManager.getConnection(), TEST_TABLE);
    }

    @Test
    void singleSelect()
    {
        builder.select("test1");
        assertEquals("SELECT test1 FROM " + TEST_TABLE, builder.toSql());
    }

    @Test
    void multipleSelect()
    {
        builder.select(new String[]{"test1", "test2"});
        assertEquals("SELECT test1, test2 FROM " + TEST_TABLE, builder.toSql());
    }

    @Test
    void repeatSelect()
    {
        builder.select("test1").select("test2");
        assertEquals("SELECT test1, test2 FROM " + TEST_TABLE, builder.toSql());
    }

    @Test
    void selectByCompare()
    {
        builder.select("test1").where(Comparator.EQ("test1", "vrolijke vrienden"));
        assertEquals("SELECT test1 FROM " + TEST_TABLE + " WHERE test1 = 'vrolijke vrienden'", builder.toSql());
    }

    @Test
    void selectByRepeatCompare()
    {
        builder.select("test1") .where(Comparator.LIKE("test1", "%v*vrienden%"))
                                .where(Comparator.GT("test2", "abc"));
        assertEquals("SELECT test1 FROM "+TEST_TABLE+" WHERE test1 LIKE '%v*vrienden%', test2 > 'abc'", builder.toSql());
    }

    @Test
    void selectByMultipleCompare()
    {
        builder.select("test1") .where(new Comparator[]{Comparator.LIKE("test1", "%v*vrienden%"),
                                                        Comparator.GT("test2",  "abc")});
        assertEquals("SELECT test1 FROM "+TEST_TABLE+" WHERE test1 LIKE '%v*vrienden%', test2 > 'abc'", builder.toSql());
    }

    @Test
    void selectAll()
    {
        assertEquals("SELECT * FROM "+TEST_TABLE, builder.select().toSql());
    }

    @Test
    void selectAllOverride()
    {
        assertEquals("SELECT * FROM "+TEST_TABLE, builder.select("test1").select().toSql());
    }

    @Test
    void selectAllOverride1()
    {
        assertEquals("SELECT * FROM "+TEST_TABLE, builder.select().select("test1").toSql());
    }

    @Test
    void undecidedQuery()
    {
        assertEquals("UNDECIDED",  builder.toSql());
    }

    @Test
    void simpleInsertionQuery()
    {
        builder.insert("test1", "Hallo");
        assertEquals("INSERT INTO "+TEST_TABLE+" (test1) VALUES ('Hallo')", builder.toSql());
    }

    @Test
    void multipleInsertionQuery()
    {
        builder.insert("test1", "Hallo").insert("test2",  "1");
        String  perm1 = "INSERT INTO "+TEST_TABLE+" (test1, test2) VALUES ('Hallo', '1')",
                perm2 = "INSERT INTO "+TEST_TABLE+" (test2, test1) VALUES ('1', 'Hallo')";
        assertTrue(perm1.equals(builder.toSql()) || perm2.equals(builder.toSql()));
    }

    @Test
    void insertionQuery()
    {
        HashMap<String, String> insertions = new HashMap<>();
        insertions.put("test1",  "hoihoi");
        builder.insert(insertions);
        assertEquals("INSERT INTO "+TEST_TABLE+" (test1) VALUES ('hoihoi')", builder.toSql());
    }

    @Test
    void insertionQueryCombined()
    {
        HashMap<String, String> insertions = new HashMap<>();
        insertions.put("test1",  "Hallo");
        builder.insert("test2", "1").insert(insertions);
        String  perm1 = "INSERT INTO "+TEST_TABLE+" (test1, test2) VALUES ('Hallo', '1')",
                perm2 = "INSERT INTO "+TEST_TABLE+" (test2, test1) VALUES ('1', 'Hallo')";
        assertTrue(perm1.equals(builder.toSql()) || perm2.equals(builder.toSql()));
    }

    @Test
    void simpleUpdateQuery()
    {
        builder.update("test1", "Hallo");
        assertEquals("UPDATE "+TEST_TABLE+" SET test1 = 'Hallo'", builder.toSql());
    }

    @Test
    void multipleUpdateQuery()
    {
        builder.update("test1", "Hallo").update("test2",  "1");
        String  perm1 = "UPDATE "+TEST_TABLE+" SET test1 = 'Hallo', test2 = '1'",
                perm2 = "UPDATE "+TEST_TABLE+" SET test2 = '1', test1 = 'Hallo'";
        assertTrue(perm1.equals(builder.toSql()) || perm2.equals(builder.toSql()));
    }

    @Test
    void updateQuery()
    {
        HashMap<String, String> updates = new HashMap<>();
        updates.put("test1",  "hoihoi");
        builder.update(updates);
        assertEquals("UPDATE "+TEST_TABLE+" SET test1 = 'hoihoi'", builder.toSql());
    }

    @Test
    void updateQueryCombined()
    {
        HashMap<String, String> updates = new HashMap<>();
        updates.put("test1",  "Hallo");
        builder.update("test2", "1").update(updates);
        String  perm1 = "UPDATE "+TEST_TABLE+" SET test1 = 'Hallo', test2 = '1'",
                perm2 = "UPDATE "+TEST_TABLE+" SET test2 = '1', test1 = 'Hallo'";
        assertTrue(perm1.equals(builder.toSql()) || perm2.equals(builder.toSql()));
    }

    @Test
    void updateAndCompare()
    {
        builder.update("test1", "new value").where("test1", "hallo");
        assertEquals("UPDATE "+TEST_TABLE+" SET test1 = 'new value' WHERE test1 = 'hallo'", builder.toSql());
    }

    @AfterClass
    static void tearDown() throws DBException, SQLException
    {
        ConnectionManager.getConnection().prepareStatement("DROP TABLE " + TEST_TABLE).execute();
    }
}