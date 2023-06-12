import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DisciplinaController {
	public static void cadastrarDisciplina(JFrame frame, Connection connection, Disciplina disciplina, String id) {
	    String sql = "INSERT INTO disciplinas (nome, descricao, carga_horaria, sala_aula, horario, professor_id) VALUES (?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, disciplina.getNome());
	        statement.setString(2, disciplina.getDescricao());
	        statement.setInt(3, disciplina.getCargaHoraria());
	        statement.setString(4, disciplina.getSalaAula());
	        statement.setString(5, disciplina.getHorario());
	        statement.setString(6, id); // Inserir o ID do professor como chave estrangeira

	        int linhasAfetadas = statement.executeUpdate();
	        if (linhasAfetadas > 0) {
	            JOptionPane.showMessageDialog(frame, "Disciplina cadastrada com sucesso!");
	        } else {
	            JOptionPane.showMessageDialog(frame, "Falha ao cadastrar a disciplina.");
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
	    }
	}

    
	public static Disciplina lerDisciplina(Connection connection, String id) {
	    String sql = "SELECT * FROM disciplinas WHERE codigo = ?";
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        int codigo = Integer.parseInt(id); 
	        statement.setInt(1, codigo);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            String nome = resultSet.getString("nome");
	            String descricao = resultSet.getString("descricao");
	            int cargaHoraria = resultSet.getInt("carga_horaria");
	            String salaAula = resultSet.getString("sala_aula");
	            String horario = resultSet.getString("horario");
	            Disciplina disciplina = new Disciplina(nome, descricao, cargaHoraria, salaAula, horario);
	            disciplina.setCodigo(codigo);
	            System.out.println(codigo);
	            return disciplina;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}



    public static ArrayList<Disciplina> listarDisciplinas(JFrame frame, Connection connection, String id) {
        ArrayList<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT * FROM disciplinas WHERE professor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int codigo = resultSet.getInt("codigo");
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                int cargaHoraria = resultSet.getInt("carga_horaria");
                String salaAula = resultSet.getString("sala_aula");
                String horario = resultSet.getString("horario");
                Disciplina disciplina = new Disciplina(nome, descricao, cargaHoraria, salaAula, horario);
                disciplina.setCodigo(codigo);
                disciplinas.add(disciplina);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
        return disciplinas;
    }


    public static void atualizarDisciplina(JFrame frame, Connection connection, Disciplina disciplina) {
        String sql = "UPDATE disciplinas SET nome = ?, descricao = ?, carga_horaria = ?, sala_aula = ?, horario = ? WHERE codigo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, disciplina.getNome());
            statement.setString(2, disciplina.getDescricao());
            statement.setInt(3, disciplina.getCargaHoraria());
            statement.setString(4, disciplina.getSalaAula());
            statement.setString(5, disciplina.getHorario());
            statement.setInt(6, disciplina.getCodigo());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Disciplina atualizada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao atualizar a disciplina.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void deletarDisciplina(JFrame frame, Connection connection, int codigo) {
        String sql = "DELETE FROM disciplinas WHERE codigo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, codigo);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(frame, "Disciplina deletada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao deletar a disciplina.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao executar o comando SQL: " + e.getMessage());
        }
    }
}

