
package DLL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/software_gestion";
    private static final String USER = "root";
    private static final String PASSWORD = "Nubax0209.";

    private static Connection conect;
    private static ConexionDB instance;

    private ConexionDB(){
        try {
          
            conect = (Connection)
            DriverManager.getConnection(URL,USER,PASSWORD);
            JOptionPane.showMessageDialog(null, "Se conectó");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar");
        }
    }

    public static ConexionDB getInstance() {
        if(instance == null){
            instance = new ConexionDB();
        }
        return instance;
    } 
    public Connection getConnection() {
        return conect;
    }


}
    /*
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            JOptionPane.showMessageDialog(null, "Has ingresado");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la conexión: " + e.getMessage());
            return null;
        }
    }
}
 */
