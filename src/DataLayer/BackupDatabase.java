package DataLayer;


import javax.naming.CommunicationException;
import java.sql.*;

/**
 * Created by Thomas on 06-05-2017.
 */
public class BackupDatabase {


    //Local connection
    private Connection localconn;
    //Local values for local database
    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String LocalDB_URL = "jdbc:mysql://localhost/costumerregistry";
    private static String Local_DB_USER = "root";
    private static String Local_DB_PASS = "tocrazy547";

    public void backUp () {

        //Creating connection to local hosted database first
        try {
            Class.forName("com.mysql.jdbc.Driver");
            localconn = DriverManager.getConnection(LocalDB_URL, Local_DB_USER, Local_DB_PASS);
            String MySQL = ("SELECT * FROM costumer");
            ResultSet rs_customer = localconn.createStatement().executeQuery(MySQL);
            MySQL = ("SELECT * FROM faktura");
            ResultSet rs_faktura = localconn.createStatement().executeQuery(MySQL);
            MySQL = ("SELECT * FROM debitor");
            ResultSet rs_debitor = localconn.createStatement().executeQuery(MySQL);
            //Creation of cloud connection
            Connection onlineconn;
            Class.forName(JDBC_DRIVER);

            String cloud_DB_URL = "jdbc:mysql://yearproject.ceumid9pylis.eu-west-2.rds.amazonaws.com:3306/costumerregistry";
            String cloud_DB_USER = "root";
            String cloud_DB_PASS = "root1234";
            onlineconn = DriverManager.getConnection(cloud_DB_URL, cloud_DB_USER, cloud_DB_PASS);

            String create_table = null;
            /*create_table = "CREATE TABLE faktura (fakturaNr VARCHAR (40) NOT NULL)";
            onlineconn.createStatement().execute(create_table);
            System.out.println(create_table);
            create_table = ("CREATE TABLE costumer(idCostumer VARCHAR (40) NOT NULL, Customer_name VARCHAR (40) NOT NULL, " +
                    "Costumer_adress VARCHAR (40) NOT NULL, iddebitor VARCHAR (40) NOT NULL, PRIMARY KEY (idCostumer), FOREIGN KEY (iddebitor))");
            onlineconn.createStatement().execute(create_table);
            System.out.println("Creating table to costumerregistry");
            create_table = ("CREATE TABLE debitor(iddebitor VARCHAR (40) NOT NULL, PRIMARY KEY (iddebitor))");
            onlineconn.createStatement().execute(create_table);*/
            ResultSet rs_table = null;
            String Sql = null;

            System.out.println("I got to here");
            if (onlineconn.getMetaData().getCatalogs() != null) {
                do {
                    System.out.println("I jumped into this becuase I'm dumb");

                    //Dropping of tables, individually for now
                    Sql = ("DROP TABLE faktura");
                    onlineconn.createStatement().execute(Sql);
                    System.out.println("I'm dropping table faktura");
                    Sql = ("DROP TABLE costumer WHERE costumer EXISTS");
                    onlineconn.createStatement().execute(Sql);
                    Sql = ("DROP TABLE debitor EXISTS");
                    onlineconn.createStatement().execute(Sql);
                } while (onlineconn.getMetaData().getCatalogs().next());
            }
            if (onlineconn.getMetaData().getCatalogs() == null) {
                System.out.println("I made it past");
                //Creation of table
                create_table = ("CREATE TABLE faktura(fakturaNr VARCHAR (40) NOT NULL , total_beløb VARCHAR (40) NOT NULL, " +
                        "faktura_dato VARCHAR (40) NOT NULL, idCostumer VARCHAR (40) NOT NULL, PRIMARY KEY (fakturaNr), FOREIGN KEY (idCostumer))");
                onlineconn.createStatement().execute(create_table);
                System.out.println(create_table);
                create_table = ("CREATE TABLE costumer(idCostumer VARCHAR (40) NOT NULL, Customer_name VARCHAR (40) NOT NULL, " +
                        "Costumer_adress VARCHAR (40) NOT NULL, iddebitor VARCHAR (40) NOT NULL, PRIMARY KEY (idCostumer), FOREIGN KEY (iddebitor))");
                onlineconn.createStatement().execute(create_table);
                System.out.println("Creating table to costumerregistry");
                create_table = ("CREATE TABLE debitor(iddebitor VARCHAR (40) NOT NULL, PRIMARY KEY (iddebitor)) ");
                onlineconn.createStatement().execute(create_table);

                //Insertion of values into faktura
                Sql = ("INSERT INTO faktura VALUES '" + rs_faktura.getString("fakturaNr") + "', '" + rs_faktura.getString("total_beløb") + "', " +
                        " '" + rs_faktura.getString("faktura_dato") + "', '" + rs_faktura.getString("idCostumer") + "';");
                onlineconn.createStatement().execute(Sql);
                System.out.println("I'm inserting data");
                //SQL insertion into costumer
                Sql = ("INSERT INTO costumer VALUES '" + rs_customer.getString("idCostumer") + "', '" + rs_customer.getString("Customer_name") + "'," +
                        "'" + rs_customer.getString("Costumer_adress") + "', '" + rs_customer.getString("iddebitor") + "';");
                onlineconn.createStatement().execute(Sql);
                //SQL insertion of debitor
                Sql = ("INSERT INTO debitor VALUE '" + rs_debitor.getString("iddebitor") + "';");
                onlineconn.createStatement().execute(Sql);
            }



            onlineconn.close();
            rs_customer.close();
            rs_debitor.close();
            rs_faktura.close();
            rs_table.close();
            localconn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("You wanna hear the most annoying sound in the world?");
            System.out.println(e);
            System.out.println("EHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");

        } catch (SQLException e) {
            System.out.println(e);
        } finally {

        }


    }




}
