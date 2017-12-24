package exception;

import java.sql.SQLException;

public class UncheckedSQLException extends RuntimeException
{
    private SQLException exception;

    public UncheckedSQLException(SQLException e)
    {
        setException(e);
    }

    /**
     * Returns the exception.
     *
     * @return exception
     */
    public SQLException getException()

    {
        return exception;
    }

    private void setException(SQLException exception)
    {
        this.exception = exception;
    }
}
