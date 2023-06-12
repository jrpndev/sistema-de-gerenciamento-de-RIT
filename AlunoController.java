import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlunoController {
    public static void main(String[] args) {
        Connection connection = Conexao.obterConexao();

        if (connection != null) {
            System.out.println("Conexão com o banco de dados estabelecida.");
            
        } else {
            System.out.println("Erro ao obter conexão com o banco de dados.");
        }
    }

    public static void cadastrarAluno(Connection connection, String nome, int orientadorId, String matricula, String nomeProjeto, String tabela) {
        String sql = "INSERT INTO " + tabela + " (matricula, nome, orientador_id, nome_projeto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);
            statement.setString(2, nome);
            statement.setInt(3, orientadorId);
            statement.setString(4, nomeProjeto);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Aluno cadastrado com sucesso!");
            } else {
                System.out.println("Falha ao cadastrar o aluno.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void lerAluno(Connection connection, String matricula, String tabela) {
        String sql = "SELECT * FROM " + tabela + " WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                int orientadorId = resultSet.getInt("orientador_id");
                String nomeProjeto = resultSet.getString("nome_projeto");

                System.out.println("Nome: " + nome);
                System.out.println("Orientador ID: " + orientadorId);
                System.out.println("Nome do Projeto: " + nomeProjeto);
            } else {
                System.out.println("Aluno não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void atualizarAluno(Connection connection, String matricula, String novoNome, String tabela) {
        String sql = "UPDATE " + tabela + " SET nome = ? WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoNome);
            statement.setString(2, matricula);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Aluno atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar o aluno.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }
    
    public static void deletarAluno(Connection connection, String matricula, String tabela) {
        String sql = "DELETE FROM " + tabela + " WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricula);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Aluno deletado com sucesso!");
            } else {
                System.out.println("Falha ao deletar o aluno.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }
}