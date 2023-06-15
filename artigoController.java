import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class artigoController {
    public static void main(String[] args) {
        Connection connection = Conexao.obterConexao();

        if (connection != null) {
            JOptionPane.showMessageDialog(null, "Conexão com o banco de dados estabelecida.");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao obter conexão com o banco de dados.");
        }
    }

    public static ArrayList<Artigo> listarArtigosPorProfessor(Connection connection, String professorId) {
        ArrayList<Artigo> artigos = new ArrayList<>();
        String sql = "SELECT * FROM artigo WHERE professor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, professorId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                String resumo = resultSet.getString("resumo");

                Artigo artigo = new Artigo();
                artigo.setId(id);
                artigo.setResumo(resumo);
                artigo.setTitulo(titulo);

                artigos.add(artigo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao executar o comando SQL: " + e.getMessage());
        }
        return artigos;
    }

    public static void cadastrarArtigo(Connection connection, String titulo, String resumo, String professorId, JFrame frame) {
        String sql = "INSERT INTO artigo (titulo, resumo, professor_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, titulo);
            statement.setString(2, resumo);
            statement.setString(3, professorId);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Artigo cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao cadastrar o artigo.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static Artigo lerArtigo(Connection connection, int id) {
        String sql = "SELECT * FROM artigo WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String resumo = resultSet.getString("resumo");

                Artigo artigo = new Artigo();
                artigo.setId(id);
                artigo.setTitulo(titulo);
                artigo.setResumo(resumo);

                return artigo;
            } else {
                JOptionPane.showMessageDialog(null, "Artigo não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao executar o comando SQL: " + e.getMessage());
        }
        return null;
    }

    public static void atualizarArtigo(Connection connection, Artigo artigo, JFrame frame) {
        String sql = "UPDATE artigo SET titulo = ?, resumo = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, artigo.getTitulo());
            statement.setString(2, artigo.getResumo());
            statement.setInt(3, artigo.getId());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Artigo atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao atualizar o artigo.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void deletarArtigo(Connection connection, int id, JFrame frame) {
        String sql = "DELETE FROM artigo WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Artigo deletado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao deletar o artigo.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }
}
