import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	
    private static final String URL = "jdbc:mysql://localhost:3306/rit";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public static Connection obterConexao() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao obter conexão com o banco de dados: " + e.getMessage());
            return null;
        }
    }

}
