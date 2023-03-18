import duckdb_driver.pata.server.Server;
import org.duckdb.DuckDBConnection;

public class PataServer implements Runnable
{
    private Server server;

    DuckDBConnection con;

    public PataServer(DuckDBConnection con)
    {
        this.con = con;
    }

    @Override
    public void run()
    {
        server = new Server(con, 41442);
        server.startServer();
    }
}
