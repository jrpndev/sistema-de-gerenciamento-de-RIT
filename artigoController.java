import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class artigoController {
    public static void main(String[] args) {
        Connection connection = Conexao.obterConexao();

        if (connection != null) {
            System.out.println("Conexão com o banco de dados estabelecida.");

        } else {
            System.out.println("Erro ao obter conexão com o banco de dados.");
        }
    }

    public static void cadastrarArtigo(Connection connection, String titulo, String resumo, int professorId) {
        String sql = "INSERT INTO artigo (titulo, resumo, professor_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, titulo);
            statement.setString(2, resumo);
            statement.setInt(3, professorId);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Artigo cadastrado com sucesso!");
            } else {
                System.out.println("Falha ao cadastrar o artigo.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void lerArtigo(Connection connection, int id) {
        String sql = "SELECT * FROM artigo WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String resumo = resultSet.getString("resumo");
                int professorId = resultSet.getInt("professor_id");

                System.out.println("Título: " + titulo);
                System.out.println("Resumo: " + resumo);
                System.out.println("Professor ID: " + professorId);
            } else {
                System.out.println("Artigo não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void atualizarArtigo(Connection connection, int id, String novoTitulo) {
        String sql = "UPDATE artigo SET titulo = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoTitulo);
            statement.setInt(2, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Artigo atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o artigo.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }
    public static void deletarArtigo(Connection connection, int id) {
        String sql = "DELETE FROM artigo WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Artigo deletado com sucesso!");
            } else {
                System.out.println("Falha ao deletar o artigo.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

}
