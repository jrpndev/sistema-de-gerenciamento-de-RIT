import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Crud {
    
    public static ArrayList <Professor> selecionarTodosProfessores(Connection connection) {
        List<Professor> professores = new ArrayList<>();

        String sql = "SELECT * FROM professor";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	Professor professor = new Professor();
            	professor.setName(resultSet.getString("nome"));
                professor.setId(resultSet.getString("id"));
                professor.setAcademicDegree(resultSet.getString("academic_degree"));
                professor.setSalary(resultSet.getDouble("salario"));
                professor.setArea(resultSet.getString("area"));
                professores.add(professor);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }

        return (ArrayList<Professor>) professores;
    }
    

    public static void cadastrarProfessor(JFrame j, Connection connection, String nome, String id, String grauAcademico,
                                          double salario, String area) {
        String sql = "INSERT INTO professor (nome, id, academic_degree, salario, area) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            statement.setString(2, id);
            statement.setString(3, grauAcademico);
            statement.setDouble(4, salario);
            statement.setString(5, area);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
            	JOptionPane.showMessageDialog(j, "Professor cadastrado com sucesso!");
            } else {
            	JOptionPane.showMessageDialog(j, "Erro ao cadastrar professor ");
            }
        } catch (SQLException e) {
        	JOptionPane.showMessageDialog(j, "Erro ao cadastrar professor ", e.getMessage(), 0);
        }
    }

    public static Professor lerProfessor(Connection connection, String id) {
        String sql = "SELECT * FROM professor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Professor professor = new Professor();
                professor.setName(resultSet.getString("nome"));
                professor.setId(resultSet.getString("id"));
                professor.setAcademicDegree(resultSet.getString("academic_degree"));
                professor.setSalary(resultSet.getDouble("salario"));
                professor.setArea(resultSet.getString("area"));

                return professor;
            } else {
                System.out.println("Professor não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }

        return null;
    }


    public static void atualizarProfessor(JFrame j  , Connection connection, String id, Professor novoProfessor) {
        String sql = "UPDATE professor SET nome = ?, academic_degree = ?, salario = ?, area = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoProfessor.getName());
            statement.setString(2, novoProfessor.getAcademicDegree());
            statement.setDouble(3, novoProfessor.getSalary());
            statement.setString(4, novoProfessor.getArea());
            statement.setString(5, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(j, "Professor Atualizado com sucesso");
            } else {
                System.out.println("Falha ao atualizar o professor.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL: " + e.getMessage());
        }
    }

    public static void deletarProfessor(JFrame j , Connection connection, String id) {
        String sql = "DELETE FROM professor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(j, "Professor deletado com sucesso");
            } else {
                JOptionPane.showMessageDialog(j, "Falha ao deletar professor");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(j, "Erro ao deletar professor, outro registro depende dessa chave!");
        }
    }

}
