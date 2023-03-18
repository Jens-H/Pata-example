import org.duckdb.DuckDBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PataExample {
    static {
        try {
            Class.forName("org.duckdb.DuckDBDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    static Connection con;
    public static void main(String[] args)
    {
        System.out.println("Start database ...");
        try
        {
            con = DriverManager.getConnection("jdbc:duckdb:./duckdbfiles");
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Start Pata server ...");
        Thread t = new Thread(new PataServer((DuckDBConnection) con));
        t.start();

        System.out.println("Create table ...");
        try
        {
            var createStmt = con.createStatement();
            createStmt.execute("create table IF NOT EXISTS growing_rows (id int8 primary key, value varchar)");
            createStmt.execute("delete from growing_rows");
            createStmt.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Start data generation ...");
        int id = 0;
        try {
            var insertStmt = con.prepareStatement("insert into growing_rows values (?, ?)");
            while (true) {
                id++;

                insertStmt.setObject(1, id);
                insertStmt.setObject(2, "Text for id: " + id);
                insertStmt.execute();

                // Wait a second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}