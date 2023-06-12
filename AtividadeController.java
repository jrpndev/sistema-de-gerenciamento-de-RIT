import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AtividadeController {
    public static void main(String[] args) {
        Connection connection = Conexao.obterConexao();

        if (connection != null) {
            System.out.println("Conexão com o banco de dados estabelecida.");
        } else {
            System.out.println("Erro ao obter conexão com o banco de dados.");
        }
    }

    public static void cadastrarAtividade(JFrame j, Connection connection, String nome, String descricao, Date data, String coordenadorId) {
    	
        if (nome.isEmpty() || descricao.isEmpty() || data == null || coordenadorId.isEmpty()) {
            JOptionPane.showMessageDialog(j, "Por favor, preencha todos os campos");
            return;
        }

        String sql = "INSERT INTO atividade (nome, descricao, data, coordenador_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            statement.setString(2, descricao);
            statement.setDate(3, new java.sql.Date(data.getTime()));
            statement.setString(4, coordenadorId);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(j, "Atividade cadastrada com sucesso");
            } else {
                JOptionPane.showMessageDialog(j, "Falha ao cadastrar atividade");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(j, "Falha ao cadastrar atividade: " + e.getMessage());
        }
    }



    public static Atividades lerAtividade(Connection connection, int id) {
        String sql = "SELECT * FROM atividade WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                Date data = resultSet.getDate("data");

   
                return new Atividades(id, nome, descricao, data);
            } else {
                System.out.println("Atividade não encontrada.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }

        return null; 
    }

    
    public static ArrayList<Atividades> lerAtividades(Connection connection, String id) {
    	
        ArrayList<Atividades> atividades = new ArrayList<>();
        String sql = "SELECT * FROM atividade WHERE coordenador_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int atividadeId = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                Date data = resultSet.getDate("data");
                String descricao  = resultSet.getString("descricao");
                Atividades atividade = new Atividades(atividadeId, nome, descricao, data);
                atividades.add(atividade);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
        return atividades;
    }





    public static void atualizarAtividade(JFrame j, Connection connection, Atividades atividade) {
        String sql = "UPDATE atividade SET nome = ?, descricao = ?, data = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, atividade.getNome());
            statement.setString(2, atividade.getDescricao());
            statement.setDate(3, new java.sql.Date(atividade.getData().getTime()));
            statement.setInt(4, atividade.getId());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(j, "Atividade atualizada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(j, "Falha ao atualizar a atividade.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(j, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void deletarAtividade(JFrame j, Connection connection, int id) {
        String sql = "DELETE FROM atividade WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(j, "Atividade deletada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(j, "Falha ao deletar a atividade.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(j, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

}