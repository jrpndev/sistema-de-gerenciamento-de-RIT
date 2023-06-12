import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
    
    	
        Connection connection = Conexao.obterConexao();

        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Janela em Branco");
            janela.getContentPane().setBackground(new Color(255, 255, 204));

            JPanel painelSuperior = new JPanel();
            painelSuperior.setBackground(new Color(255, 255, 204));

            JLabel tituloLabel = new JLabel("Sistema de Gerenciamento de RIT");
            tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));

            JButton botaoNovo = new JButton("Novo Professor");
            botaoNovo.setBounds(10, 0, 150, 30);
            painelSuperior.add(botaoNovo);

            botaoNovo.addActionListener(e -> abrirJanelaCadastro(connection, janela, ""));


            painelSuperior.add(tituloLabel);

            ArrayList<Professor> professores = Crud.selecionarTodosProfessores(connection);

            Object[][] dados = new Object[professores.size()][5];

            for (int i = 0; i < professores.size(); i++) {
                Professor professor = professores.get(i);
                dados[i][0] = professor.getName();
                dados[i][1] = professor.getId();
                dados[i][2] = professor.getAcademicDegree();
                dados[i][3] = professor.getSalary();
                dados[i][4] = professor.getArea();
            }

            String[] colunas = { "Nome do Professor", "ID", "Grau Acadêmico", "Salário", "Área" };

            DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabela = new JTable(modelo);
            tabela.setBackground(new Color(255, 255, 204));

            tabela.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int linhaSelecionada = tabela.getSelectedRow();
                    int colunaSelecionada = tabela.getSelectedColumn();

                    if (linhaSelecionada != -1 && colunaSelecionada != -1) {
                        String professorId = (String) tabela.getValueAt(linhaSelecionada, 1); // Obtém o ID do professor da célula selecionada
                        abrirJanelaNova(janela, professorId, connection);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(tabela);

            janela.add(painelSuperior, BorderLayout.NORTH);
            janela.add(scrollPane);

            janela.setSize(1280, 720);
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setVisible(true);
        });

    }
    
    public static void abrirJanelaAtividades(Connection con , String id) {
        JFrame janela = new JFrame("Tela de Atividades");
        janela.setSize(1280, 720);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());

        
        JButton cadastrarAtividade = new JButton("Criar nova atividade");
        
        
        cadastrarAtividade.addActionListener(e -> {

            // Criar a janela de cadastro de atividade
            JFrame janelaAtividade = new JFrame("Cadastro de Atividade");
            janelaAtividade.setSize(500, 500);
            janelaAtividade.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janelaAtividade.setLayout(null);

            // Painel para os campos da atividade
            JPanel painelCampos = new JPanel();
            painelCampos.setLayout(null);
            painelCampos.setBounds(10, 0, 500, 500);

            // Campo Nome
            JLabel labelNome = new JLabel("Nome:");
            labelNome.setBounds(10, 10, 80, 25);
            JTextField campoNome = new JTextField();
            campoNome.setBounds(100, 10, 300, 25);
            painelCampos.add(labelNome);
            painelCampos.add(campoNome);

            // Campo Descrição
            JLabel labelDescricao = new JLabel("Descrição:");
            labelDescricao.setBounds(10, 40, 80, 25);
            JTextField campoDescricao = new JTextField();
            campoDescricao.setBounds(100, 40, 300, 25);
            painelCampos.add(labelDescricao);
            painelCampos.add(campoDescricao);

            // Campo Data
            JLabel labelData = new JLabel("Data:");
            labelData.setBounds(10, 70, 80, 25);
            JTextField campoData = new JTextField();
            campoData.setBounds(100, 70, 300, 25);
            painelCampos.add(labelData);
            painelCampos.add(campoData);

         
            JButton cadastrarAtividadeBtn = new JButton("Cadastrar");
            cadastrarAtividadeBtn.setBounds(10, 100, 100, 25);

            cadastrarAtividadeBtn.addActionListener(event -> {
                
                String nome = campoNome.getText();
                String descricao = campoDescricao.getText();
                java.util.Date data = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    data = dateFormat.parse(campoData.getText());
                } catch (ParseException ex) {
                	JOptionPane.showMessageDialog( janelaAtividade, "Houve um erro ao cadastrar, campo incorreto err : " + ex.getMessage());
                    ex.printStackTrace();
                }

              AtividadeController.cadastrarAtividade(janelaAtividade, con, nome, descricao, data, id);
                janelaAtividade.dispose();
            });

            
            janelaAtividade.add(painelCampos, BorderLayout.CENTER);
            janelaAtividade.add(cadastrarAtividadeBtn, BorderLayout.SOUTH);

            janelaAtividade.setVisible(true);

        });

        painelPrincipal.add(cadastrarAtividade, BorderLayout.WEST);
        
        System.out.println(id);
        ArrayList<Atividades> atividades = AtividadeController.lerAtividades(con, id);
        DefaultTableModel tableModel = new DefaultTableModel();

        JTable tabelaAtividades = new JTable(tableModel);
        tabelaAtividades.setBackground(new Color(255, 255, 204));

        tableModel.addColumn("Nome");
        tableModel.addColumn("Descricao");
        tableModel.addColumn("Data");
       
        
    

        for (Atividades atividade : atividades) {
        	
            Object[] rowData = { atividade.getNome(), atividade.getDescricao(), atividade.getData()};
            tableModel.addRow(rowData);
        }

        tabelaAtividades.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaAtividades.rowAtPoint(e.getPoint());
                int column = tabelaAtividades.columnAtPoint(e.getPoint());
                if (row >= 0 && column >= 0) {
                	int atividadeId = atividades.get(row).getId();
                    minhaFuncao(con , atividadeId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaAtividades);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        janela.getContentPane().add(painelPrincipal);
        janela.setVisible(true);
    }

    public static void minhaFuncao(Connection con, int atividadeId) {
    	
        Atividades atividade = AtividadeController.lerAtividade(con, atividadeId);

        JFrame janela = new JFrame("Cadastro de Atividade");
        janela.setSize(500, 500);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painel = new JPanel();
        painel.setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(atividade.getNome());
        lblNome.setBounds(10, 10, 80, 25);
        txtNome.setBounds(100, 10, 300, 25);

        JLabel lblDescricao = new JLabel("Descrição:");
        JTextField txtDescricao = new JTextField(atividade.getDescricao());
        lblDescricao.setBounds(10, 40, 80, 25);
        txtDescricao.setBounds(100, 40, 300, 25);

        JLabel lblData = new JLabel("Data:");
        JTextField txtData = new JTextField(atividade.getData().toString());
        lblData.setBounds(10, 70, 80, 25);
        txtData.setBounds(100, 70, 300, 25);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(10, 100, 100, 25);
        btnAtualizar.addActionListener(e -> {
        	
            AtividadeController.atualizarAtividade(janela , con, atividade);
        });

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(150, 100, 100, 25);
        btnExcluir.addActionListener(e -> {
            AtividadeController.deletarAtividade(janela , con, atividadeId);
        });

        painel.add(lblNome);
        painel.add(txtNome);
        painel.add(lblDescricao);
        painel.add(txtDescricao);
        painel.add(lblData);
        painel.add(txtData);
        painel.add(btnAtualizar);
        painel.add(btnExcluir);
        janela.add(painel);
        janela.setVisible(true);
    }



    
    private static void abrirJanelaCadastro(Connection con, JFrame janelaPrincipal, String professorId) {
    	
    	Professor professor  = new Professor( ); 
    	
        JOptionPane.showMessageDialog(janelaPrincipal, "Abrir formulário do professor ");

        JFrame janelaFormulario = new JFrame("Novo Professor");
        janelaFormulario.setSize(500,500);

        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(null);
        int alturaPainelSuperior = 50 + 10 + 30 + 10;
        int larguraPainel = 400;
        int alturaPainel = 350;
        int xPainel = (janelaFormulario.getWidth() - larguraPainel) / 2;
        int yPainel = (janelaFormulario.getHeight() - alturaPainel) / 2;
        painelFormulario.setBackground(new Color(255, 255, 204));
        
        

        JLabel idLabel = new JLabel("CPF:");
        idLabel.setBounds(50, 50, 100, 30);
        JTextField idTextField = new JTextField(professor.getId());
        idTextField.setBounds(160, 50, 200, 30);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(50, 100, 100, 30);
        JTextField nomeTextField = new JTextField(professor.getName());
        nomeTextField.setBounds(160, 100, 200, 30);

        JLabel grauAcademicoLabel = new JLabel("Grau Acadêmico:");
        grauAcademicoLabel.setBounds(50, 150, 100, 30);
        JTextField grauAcademicoTextField = new JTextField(professor.getAcademicDegree());
        grauAcademicoTextField.setBounds(160, 150, 200, 30);

        JLabel salarioLabel = new JLabel("Salário:");
        salarioLabel.setBounds(50, 200, 100, 30);
        JTextField salarioTextField = new JTextField(String.valueOf(professor.getSalary()));
        salarioTextField.setBounds(160, 200, 200, 30);

        JLabel areaLabel = new JLabel("Área:");
        areaLabel.setBounds(50, 250, 100, 30);
        JTextField areaTextField = new JTextField(professor.getArea());
        areaTextField.setBounds(160, 250, 200, 30);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarProfessor( janelaFormulario, con , nomeTextField.getText(), idTextField.getText(), grauAcademicoTextField.getText(),
                        Double.parseDouble(salarioTextField.getText()), areaTextField.getText());
                janelaFormulario.dispose();
            }
        });
        cadastrarButton.setBounds(160, 300, 100, 30);
        

        painelFormulario.add(nomeLabel);
        painelFormulario.add(nomeTextField);
        painelFormulario.add(idLabel);
        painelFormulario.add(idTextField);
        painelFormulario.add(grauAcademicoLabel);
        painelFormulario.add(grauAcademicoTextField);
        painelFormulario.add(salarioLabel);
        painelFormulario.add(salarioTextField);
        painelFormulario.add(areaLabel);
        painelFormulario.add(areaTextField);
        painelFormulario.add(cadastrarButton);
        janelaFormulario.add(painelFormulario);
        janelaFormulario.setVisible(true);
    }

    private static void cadastrarProfessor(JFrame j ,Connection con, String nome, String id, String grauAcademico, double salario, String area) {
        Professor professor = new Professor();
        professor.setName(nome);
        professor.setId(id);
        professor.setAcademicDegree(grauAcademico);
        professor.setSalary(salario);
        professor.setArea(area);
        Crud.cadastrarProfessor(j, con ,  professor.getName(), professor.getId(), professor.getAcademicDegree(), professor.getSalary(), professor.getArea());
    }
    
    private static void abrirJanelaFormulario(JFrame j  , String id, Connection connection) {
        
        Disciplina disciplina = DisciplinaController.lerDisciplina(connection, id);
          
        JFrame janelaFormulario = new JFrame("Formulário de Disciplina");
        janelaFormulario.setSize(500, 500);
        janelaFormulario.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelPrincipalFormulario = new JPanel();
        painelPrincipalFormulario.setLayout(new GridLayout(7, 2));

        JLabel labelNome = new JLabel("Nome:");
        JTextField campoNome = new JTextField(disciplina.getNome());

        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField(disciplina.getDescricao());

        JLabel labelCargaHoraria = new JLabel("Carga Horária:");
        JTextField campoCargaHoraria = new JTextField(String.valueOf(disciplina.getCargaHoraria()));

        JLabel labelSalaAula = new JLabel("Sala de Aula:");
        JTextField campoSalaAula = new JTextField(disciplina.getSalaAula());

        JLabel labelHorario = new JLabel("Horário:");
        JTextField campoHorario = new JTextField(disciplina.getHorario());

        JButton botaoAtualizar = new JButton("Atualizar");
        botaoAtualizar.addActionListener(e -> {
            // Lógica para atualizar a disciplina
            // ...

            janelaFormulario.dispose(); // Fechar a janela de formulário
        });

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.addActionListener(e -> {
            // Lógica para excluir a disciplina
            // ...

            janelaFormulario.dispose(); // Fechar a janela de formulário
        });

        painelPrincipalFormulario.add(labelNome);
        painelPrincipalFormulario.add(campoNome);
        painelPrincipalFormulario.add(labelDescricao);
        painelPrincipalFormulario.add(campoDescricao);
        painelPrincipalFormulario.add(labelCargaHoraria);
        painelPrincipalFormulario.add(campoCargaHoraria);
        painelPrincipalFormulario.add(labelSalaAula);
        painelPrincipalFormulario.add(campoSalaAula);
        painelPrincipalFormulario.add(labelHorario);
        painelPrincipalFormulario.add(campoHorario);
        painelPrincipalFormulario.add(botaoAtualizar);
        painelPrincipalFormulario.add(botaoExcluir);

        janelaFormulario.getContentPane().add(painelPrincipalFormulario);
        janelaFormulario.setVisible(true);
    }
    
    private static void abrirJanelaCadastroDisciplina(Connection connection, String id) {
        JFrame janelaCadastro = new JFrame("Cadastro de Disciplina");
        janelaCadastro.setSize(500, 500);
        janelaCadastro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(20);

        JLabel lblDescricao = new JLabel("Descrição:");
        JTextField txtDescricao = new JTextField(20);

        JLabel lblCargaHoraria = new JLabel("Carga Horária:");
        JTextField txtCargaHoraria = new JTextField(20);

        JLabel lblSalaAula = new JLabel("Sala de Aula:");
        JTextField txtSalaAula = new JTextField(20);

        JLabel lblHorario = new JLabel("Horário:");
        JTextField txtHorario = new JTextField(20);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(event -> {
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();
            String cargaHorariaText = txtCargaHoraria.getText();
            String salaAula = txtSalaAula.getText();
            String horario = txtHorario.getText();

            // Verificar se algum campo está vazio
            if (nome.isEmpty() || descricao.isEmpty() || cargaHorariaText.isEmpty() || salaAula.isEmpty() || horario.isEmpty()) {
                JOptionPane.showMessageDialog(janelaCadastro, "Preencha todos os campos antes de cadastrar a disciplina.");
                return;
            }

            int cargaHoraria = Integer.parseInt(cargaHorariaText);

            Disciplina disciplina = new Disciplina(nome, descricao, cargaHoraria, salaAula, horario);

            DisciplinaController.cadastrarDisciplina(janelaCadastro, connection, disciplina, id);

            JOptionPane.showMessageDialog(janelaCadastro, "Disciplina cadastrada com sucesso!");
        });


        painelCadastro.add(lblNome);
        painelCadastro.add(txtNome);
        painelCadastro.add(lblDescricao);
        painelCadastro.add(txtDescricao);
        painelCadastro.add(lblCargaHoraria);
        painelCadastro.add(txtCargaHoraria);
        painelCadastro.add(lblSalaAula);
        painelCadastro.add(txtSalaAula);
        painelCadastro.add(lblHorario);
        painelCadastro.add(txtHorario);
        painelCadastro.add(new JLabel()); // Espaço vazio para layout
        painelCadastro.add(btnCadastrar);

        janelaCadastro.getContentPane().add(painelCadastro);
        janelaCadastro.setVisible(true);
    }

    
    private static void abrirJanelaNova(JFrame janelaAnterior, String id, Connection con) {
        int larguraAnterior = janelaAnterior.getWidth();
        int alturaAnterior = janelaAnterior.getHeight();

        JFrame janelaNova = new JFrame("Janela Nova");
        janelaNova.setSize(larguraAnterior, alturaAnterior);

        janelaNova.getContentPane().setBackground(new Color(255, 255, 204));
        JPanel painelSuperiorNova = new JPanel();
        painelSuperiorNova.setLayout(null);
        painelSuperiorNova.setBackground(new Color(255, 255, 204));

        int alturaPainelSuperior = 50 + 10 + 30 + 10;
        painelSuperiorNova.setBounds(0, 0, larguraAnterior, alturaPainelSuperior);

        janelaNova.add(painelSuperiorNova);

        JLabel tituloRIT = new JLabel("Relatório individual de trabalho");
        tituloRIT.setBounds((larguraAnterior - 250) / 2, 10, 250, 50);
        tituloRIT.setFont(new Font("Arial", Font.BOLD, 16));
        tituloRIT.setHorizontalAlignment(SwingConstants.CENTER);
        painelSuperiorNova.add(tituloRIT);
        
        JButton botaoVerAtividades = new JButton("Consultar Coordenação");
        botaoVerAtividades.setBounds(larguraAnterior - 500, 70, 150, 30);
        painelSuperiorNova.add(botaoVerAtividades);
        
        JButton botaoVerDisciplinas = new JButton("Consultar Disciplinas");
        botaoVerDisciplinas.setBounds(larguraAnterior - 720, 70, 200, 30);
        painelSuperiorNova.add(botaoVerDisciplinas);
        
        

        botaoVerDisciplinas.addActionListener(e -> {
            // Criar a janela de consulta de disciplinas
            JFrame janelaDisciplina = new JFrame("Disciplinas");
            janelaDisciplina.setSize(1280, 720);
            janelaDisciplina.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel painelPrincipalDisciplina = new JPanel();
            painelPrincipalDisciplina.setLayout(new BorderLayout());

            DefaultTableModel tableModel = new DefaultTableModel();
            JTable tabelaDisciplinas = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Desabilitar edição das células
                }
            };
            JScrollPane scrollPane = new JScrollPane(tabelaDisciplinas);

            tableModel.addColumn("Código");
            tableModel.addColumn("Nome");
            tableModel.addColumn("Descrição");
            tableModel.addColumn("Carga Horária");
            tableModel.addColumn("Sala de Aula");
            tableModel.addColumn("Horário");

            ArrayList<Disciplina> disciplinas = DisciplinaController.listarDisciplinas(janelaDisciplina, con, id);

            for (Disciplina disciplina : disciplinas) {
            	
                Object[] rowData = {disciplina.getCodigo(), disciplina.getNome(), disciplina.getDescricao(),
                        disciplina.getCargaHoraria(), disciplina.getSalaAula(), disciplina.getHorario()};
                tableModel.addRow(rowData);
            }

            tabelaDisciplinas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = tabelaDisciplinas.rowAtPoint(e.getPoint());
                    int column = tabelaDisciplinas.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        int codigo = (int) tabelaDisciplinas.getValueAt(row, 0);
                        abrirJanelaFormulario(janelaNova, id, con);
                        
                    }
                }
            });


            JButton botaoNovaDisciplina = new JButton("Nova Disciplina");
            botaoNovaDisciplina.setBounds(larguraAnterior - 720, 30, 200, 30);

            botaoNovaDisciplina.addActionListener(event -> {
              
                abrirJanelaCadastroDisciplina(con, id);

                janelaDisciplina.dispose();
            });

            painelPrincipalDisciplina.add(scrollPane, BorderLayout.CENTER);
            painelPrincipalDisciplina.add(botaoNovaDisciplina, BorderLayout.NORTH);

            janelaDisciplina.getContentPane().add(painelPrincipalDisciplina);
            janelaDisciplina.setVisible(true);
        });



        JButton botaoGerarRelatorio = new JButton("Gerar Relatório");
        botaoGerarRelatorio.setBounds(larguraAnterior - 320, 70, 140, 30);
        painelSuperiorNova.add(botaoGerarRelatorio);

        JButton botaoDeletar = new JButton("Excluir registro");
        botaoDeletar.setBounds(larguraAnterior - 160, 70, 140, 30);
        painelSuperiorNova.add(botaoDeletar);
        botaoDeletar.addActionListener(event -> {
            
        	Crud.deletarProfessor(janelaNova, con, id);
        });
        
        botaoVerAtividades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaAtividades(con, id);
            }
        });

        
        Connection connection = Conexao.obterConexao();
        Professor professor = Crud.lerProfessor(connection, id);

    
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(50, 100, 100, 30);
        JTextField nomeTextField = new JTextField(professor.getName());
        nomeTextField.setBounds(160, 100, 200, 30);

        JLabel grauAcademicoLabel = new JLabel("Grau Acadêmico:");
        grauAcademicoLabel.setBounds(50, 150, 100, 30);
        JTextField grauAcademicoTextField = new JTextField(professor.getAcademicDegree());
        grauAcademicoTextField.setBounds(160, 150, 200, 30);

        JLabel salarioLabel = new JLabel("Salário:");
        salarioLabel.setBounds(50, 200, 100, 30);
        JTextField salarioTextField = new JTextField(String.valueOf(professor.getSalary()));
        salarioTextField.setBounds(160, 200, 200, 30);

        JLabel areaLabel = new JLabel("Área:");
        areaLabel.setBounds(50, 250, 100, 30);
        JTextField areaTextField = new JTextField(professor.getArea());
        areaTextField.setBounds(160, 250, 200, 30);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.setBounds(50, 300, 100, 30);

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                professor.setName(nomeTextField.getText());
                professor.setAcademicDegree(grauAcademicoTextField.getText());
                professor.setSalary(Double.parseDouble(salarioTextField.getText()));
                professor.setArea(areaTextField.getText());
                
                Crud.atualizarProfessor(janelaNova, connection, id, professor);
                
                JOptionPane.showMessageDialog(janelaNova, "Professor atualizado com sucesso!");
            }
        });

        janelaNova.add(nomeLabel);
        janelaNova.add(nomeTextField);
        janelaNova.add(grauAcademicoLabel);
        janelaNova.add(grauAcademicoTextField);
        janelaNova.add(salarioLabel);
        janelaNova.add(salarioTextField);
        janelaNova.add(areaLabel);
        janelaNova.add(areaTextField);
        janelaNova.add(atualizarButton);

        janelaNova.setSize(larguraAnterior, alturaAnterior);
        janelaNova.setLocationRelativeTo(null);

        janelaNova.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        janelaNova.setLayout(null);

        JButton setaVoltar = new JButton("<");
        setaVoltar.setBounds(10, 10, 50, 50);
        setaVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        janelaNova.add(setaVoltar);
        setaVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                janelaNova.dispose();
                janelaAnterior.setVisible(true);
            }
        });

        janelaNova.setVisible(true);

        janelaAnterior.setVisible(false);
        }



}

