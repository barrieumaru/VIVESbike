package common.columntranslation;

import java.sql.ResultSet;

public abstract class Translator<DataBag>
{
    protected abstract void translate(DataBag data, ResultSet resultset);
}
