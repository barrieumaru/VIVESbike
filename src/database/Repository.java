package database;


import common.datastructures.Pair;

import java.util.AbstractMap;

public abstract class Repository<DataBag>
{
    private String table;

    private DataBag databag;

    Repository(String table, DataBag bag)
    {
        setTable(table);
    }

    private void setTable(String table)
    {
        this.table = table;
    }

    private String getTable()
    {
        return table;
    }

    /**
     * Returns the databag.
     *
     * @return databag
     */
    protected DataBag getDatabag()
    {
        return databag;
    }

    private void setDatabag(DataBag databag)
    {
        this.databag = databag;
    }


}
