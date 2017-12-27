package database.connect;


import exception.DBException;

;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection getConnection() throws DBException
    {
        try {
            //driver laden
            Class.forName(DBProp.getDriver()).getDeclaredConstructors()[0].newInstance();
            return DriverManager.getConnection(DBProp.getDbUrl(), DBProp.getLogin(), DBProp.getPaswoord());
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new DBException("Connectie met de database mislukt");
        }
    }
}
