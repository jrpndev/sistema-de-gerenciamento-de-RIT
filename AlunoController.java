import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AlunoController {
    // Restante do código...

    public static void cadastrarAluno(Connection connection, String nome, String orientadorId, String matricula,
            String nomeProjeto, String tipo, String tabela, JFrame frame) {
        String sql = "INSERT INTO " + tabela
                + " (matricula, nome, orientador_id, nome_projeto, tipo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);
            statement.setString(2, nome);
            statement.setString(3, orientadorId);
            statement.setString(4, nomeProjeto);
            statement.setString(5, tipo);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao cadastrar o aluno.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static Aluno lerAluno(Connection connection, String matricula, JFrame frame) {
        String sql = "SELECT * FROM aluno WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                int orientadorId = resultSet.getInt("orientador_id");
                String nomeProjeto = resultSet.getString("nome_projeto");
                String tipo = resultSet.getString("tipo");

                Aluno aluno = new Aluno(matricula, nome, nomeProjeto, tipo);

                return aluno;
            } else {
                JOptionPane.showMessageDialog(frame, "Aluno não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Aluno> lerAlunos(Connection connection, String professorId, JFrame frame) {
        ArrayList<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno WHERE orientador_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, professorId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String matricula = resultSet.getString("matricula");
                String nome = resultSet.getString("nome");
                String nomeProjeto = resultSet.getString("nome_projeto");
                String tipo = resultSet.getString("tipo");

                Aluno aluno = new Aluno(matricula, nome, nomeProjeto, tipo);
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }

        return alunos;
    }

    public static void atualizarAluno(Connection connection, Aluno aluno, JFrame frame) {
        String sql = "UPDATE aluno SET nome = ?, nome_projeto = ?, tipo = ? WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, aluno.getNome());
            statement.setString(2, aluno.getNomeProjeto());
            statement.setString(3, aluno.getTipo());
            statement.setString(4, aluno.getMatricula());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao atualizar o aluno.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void deletarAluno(Connection connection, String matricula, JFrame frame) {
        String sql = "DELETE FROM aluno WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Aluno deletado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao deletar o aluno.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }
}
