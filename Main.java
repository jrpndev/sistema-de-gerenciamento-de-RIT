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
        //fernandacodes and jrpncodes system
        Connection connection = Conexao.obterConexao();

        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Tela inicial");
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

            String[] colunas = { "Nome do Professor", "ID", "Grau Academico", "Salario", "Area" };

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
                        String professorId = (String) tabela.getValueAt(linhaSelecionada, 1); // Obtém o ID do professor
                                                                                              // da célula selecionada
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

    public static void abrirJanelaAtividades(Connection con, String id) {
        JFrame janela = new JFrame("Tela de Atividades");
        janela.setSize(1280, 720);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());

        JButton cadastrarAtividade = new JButton("Criar nova atividade");

        cadastrarAtividade.addActionListener(e -> {
            // Criar a janela de cadastro de atividade
            JFrame janelaAtividade = new JFrame("Cadastro de Atividade");
            janelaAtividade.setSize(500, 500);
            janelaAtividade.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janelaAtividade.setLayout(new BorderLayout());

            // Painel para os campos da atividade
            JPanel painelCampos = new JPanel();
            painelCampos.setLayout(null);

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
                    JOptionPane.showMessageDialog(janelaAtividade,
                            "Houve um erro ao cadastrar, campo incorreto err : " + ex.getMessage());
                    ex.printStackTrace();
                }

                AtividadeController.cadastrarAtividade(janelaAtividade, con, nome, descricao, data, id);
                janelaAtividade.dispose();
            });

            painelCampos.add(cadastrarAtividadeBtn);

            janelaAtividade.add(painelCampos, BorderLayout.CENTER);

            janelaAtividade.setVisible(true);
        });


        painelPrincipal.add(cadastrarAtividade, BorderLayout.WEST);
        ArrayList<Atividades> atividades = AtividadeController.lerAtividades(con, id);
        DefaultTableModel tableModel = new DefaultTableModel();

        JTable tabelaAtividades = new JTable(tableModel);
        tabelaAtividades.setBackground(new Color(255, 255, 204));

        tableModel.addColumn("Nome");
        tableModel.addColumn("Descricao");
        tableModel.addColumn("Data");

        for (Atividades atividade : atividades) {

            Object[] rowData = { atividade.getNome(), atividade.getDescricao(), atividade.getData() };
            tableModel.addRow(rowData);
        }

        tabelaAtividades.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaAtividades.rowAtPoint(e.getPoint());
                int column = tabelaAtividades.columnAtPoint(e.getPoint());
                if (row >= 0 && column >= 0) {
                    int atividadeId = atividades.get(row).getId();
                    minhaFuncao(con, atividadeId);
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

            AtividadeController.atualizarAtividade(janela, con, atividade);
        });

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(150, 100, 100, 25);
        btnExcluir.addActionListener(e -> {
            AtividadeController.deletarAtividade(janela, con, atividadeId);
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

        Professor professor = new Professor();

        JFrame janelaFormulario = new JFrame("Novo Professor");
        janelaFormulario.setSize(500, 500);

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

        JLabel areaLabel = new JLabel("Area:");
        areaLabel.setBounds(50, 250, 100, 30);
        JTextField areaTextField = new JTextField(professor.getArea());
        areaTextField.setBounds(160, 250, 200, 30);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarProfessor(janelaFormulario, con, nomeTextField.getText(), idTextField.getText(),
                        grauAcademicoTextField.getText(),
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

    private static void cadastrarProfessor(JFrame j, Connection con, String nome, String id, String grauAcademico,
            double salario, String area) {
        Professor professor = new Professor();
        professor.setName(nome);
        professor.setId(id);
        professor.setAcademicDegree(grauAcademico);
        professor.setSalary(salario);
        professor.setArea(area);
        Crud.cadastrarProfessor(j, con, professor.getName(), professor.getId(), professor.getAcademicDegree(),
                professor.getSalary(), professor.getArea());
    }

    private static void abrirJanelaFormulario(JFrame j, int id, Connection connection) {

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
            DisciplinaController.atualizarDisciplina(janelaFormulario, connection, disciplina);
        });

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.addActionListener(e -> {
            
        	DisciplinaController.deletarDisciplina(janelaFormulario, connection, id);
            janelaFormulario.dispose();
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
            if (nome.isEmpty() || descricao.isEmpty() || cargaHorariaText.isEmpty() || salaAula.isEmpty()
                    || horario.isEmpty()) {
                JOptionPane.showMessageDialog(janelaCadastro,
                        "Preencha todos os campos antes de cadastrar a disciplina.");
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

    private static void setAluno(String id, Connection con) {
        // Criar a janela do formulário
        JFrame janelaFormulario = new JFrame("Formulário do Aluno");
        janelaFormulario.setSize(500, 500);

        // Carregar os dados do aluno
        Aluno aluno = AlunoController.lerAluno(con, id, janelaFormulario);

        // Criar os componentes do formulário
        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(aluno.getMatricula());
        JLabel lblMatricula = new JLabel("Matrícula:");
        JTextField txtMatricula = new JTextField(aluno.getNome());
        JLabel lblNomeProjeto = new JLabel("Nome do Projeto:");
        JTextField txtNomeProjeto = new JTextField(aluno.getNomeProjeto());
        JLabel lblTipo = new JLabel("Tipo:");
        JTextField txtTipo = new JTextField(aluno.getTipo());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Atualizar os dados do aluno com base nos campos do formulário
                aluno.setNome(txtNome.getText());
                aluno.setMatricula(txtMatricula.getText());
                aluno.setNomeProjeto(txtNomeProjeto.getText());
                aluno.setTipo(txtTipo.getText());

                // Chamar a função de atualizar aluno do AlunoController
                AlunoController.atualizarAluno(con, aluno, janelaFormulario);
                JOptionPane.showMessageDialog(null, "Aluno atualizado com sucesso!");

                // Fechar a janela do formulário
                janelaFormulario.dispose();
            }
        });

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AlunoController.deletarAluno(con, aluno.getNome(), janelaFormulario);
                JOptionPane.showMessageDialog(null, "Aluno excluído com sucesso!");

                janelaFormulario.dispose();
            }
        });

        janelaFormulario.setLayout(new GridLayout(5, 2));
        janelaFormulario.add(lblNome);
        janelaFormulario.add(txtNome);
        janelaFormulario.add(lblMatricula);
        janelaFormulario.add(txtMatricula);
        janelaFormulario.add(lblNomeProjeto);
        janelaFormulario.add(txtNomeProjeto);
        janelaFormulario.add(lblTipo);
        janelaFormulario.add(txtTipo);
        janelaFormulario.add(btnAtualizar);
        janelaFormulario.add(btnExcluir);

        // Exibir a janela do formulário
        janelaFormulario.setVisible(true);
    }
    
    public static void abrirJanelaEditarArtigo(int idArtigo, Connection con) {
        // Ler as informações do artigo através do artigoController
        Artigo artigo = artigoController.lerArtigo(con, idArtigo);

        // Criar a janela de edição do artigo
        JFrame janelaEditarArtigo = new JFrame();
        janelaEditarArtigo.setTitle("Editar Artigo");
        janelaEditarArtigo.setSize(500, 500);
        janelaEditarArtigo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaEditarArtigo.setLayout(new GridLayout(4, 2));

        // Campos do formulário
        JLabel labelTitulo = new JLabel("Título:");
        JTextField campoTitulo = new JTextField();
        campoTitulo.setText(artigo.getTitulo());

        JLabel labelResumo = new JLabel("Resumo:");
        JTextField campoResumo = new JTextField();
        campoResumo.setText(artigo.getResumo());

        // Botões de atualizar e excluir
        JButton botaoAtualizar = new JButton("Atualizar");
        JButton botaoExcluir = new JButton("Excluir");

        // Adicionando os componentes à janela de edição
        janelaEditarArtigo.add(labelTitulo);
        janelaEditarArtigo.add(campoTitulo);

        janelaEditarArtigo.add(labelResumo);
        janelaEditarArtigo.add(campoResumo);

        janelaEditarArtigo.add(botaoAtualizar);
        janelaEditarArtigo.add(botaoExcluir);

        // Ação do botão Atualizar
        botaoAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Atualizar as informações do artigo com base nos valores dos campos do formulário
                artigo.setTitulo(campoTitulo.getText());
                artigo.setResumo(campoResumo.getText());

                // Chamar o método de atualização do ArtigoController
                artigoController.atualizarArtigo(con, artigo , janelaEditarArtigo);

                // Fechar a janela de edição após a atualização ser concluída
                janelaEditarArtigo.dispose();
            }
        });

        // Ação do botão Excluir
        botaoExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chamar o método de exclusão do ArtigoController
                artigoController.deletarArtigo(con, idArtigo , janelaEditarArtigo);

                janelaEditarArtigo.dispose();
            }
        });

        // Exibir a janela de edição
        janelaEditarArtigo.setVisible(true);
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

        JButton botaoVerAtividades = new JButton("Coordenação");
        botaoVerAtividades.setBounds(larguraAnterior - 500, 70, 150, 30);
        painelSuperiorNova.add(botaoVerAtividades);

        JButton botaoVerDisciplinas = new JButton("Consultar Disciplinas");
        botaoVerDisciplinas.setBounds(larguraAnterior - 720, 70, 200, 30);
        painelSuperiorNova.add(botaoVerDisciplinas);

        JButton botaoVerAlunos = new JButton("Consultar Alunos");
        botaoVerAlunos.setBounds(larguraAnterior - 950, 70, 200, 30);
        painelSuperiorNova.add(botaoVerAlunos);

        botaoVerAlunos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Criar a janela
                JFrame janelaConsultaAlunos = new JFrame("Consulta de Alunos");
                janelaConsultaAlunos.setSize(1280, 720);

                // Criar o label "Lista de Alunos"
                JLabel labelListaAlunos = new JLabel("Lista de Alunos");
                labelListaAlunos.setBounds(10, 10, 200, 30);
                janelaConsultaAlunos.getContentPane().add(labelListaAlunos);

                // Criar o botão "Novo Aluno"
                JButton botaoNovoAluno = new JButton("Novo Aluno");
                botaoNovoAluno.setBounds(10, 50, 120, 30);
                botaoNovoAluno.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Abrir janela para cadastrar novo aluno
                        JFrame janelaNovoAluno = new JFrame("Novo Aluno");
                        janelaNovoAluno.setSize(500, 500);

                        // Criar os componentes do formulário
                        JLabel lblNome = new JLabel("Nome:");
                        JTextField txtNome = new JTextField();
                        JLabel lblMatricula = new JLabel("Matrícula:");
                        JTextField txtMatricula = new JTextField();
                        JLabel lblNomeProjeto = new JLabel("Nome do Projeto:");
                        JTextField txtNomeProjeto = new JTextField();
                        JLabel lblTipo = new JLabel("Tipo:");
                        JTextField txtTipo = new JTextField();

                        JButton btnCadastrar = new JButton("Cadastrar");

                        // Posicionar os componentes na janela
                        JPanel panel = new JPanel();
                        panel.setLayout(new GridLayout(6, 2));
                        panel.add(lblNome);
                        panel.add(txtNome);
                        panel.add(lblMatricula);
                        panel.add(txtMatricula);
                        panel.add(lblNomeProjeto);
                        panel.add(txtNomeProjeto);
                        panel.add(lblTipo);
                        panel.add(txtTipo);
                        panel.add(btnCadastrar);

                        janelaNovoAluno.getContentPane().add(panel);

                        // Ação do botão cadastrar
                        btnCadastrar.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String nome = txtNome.getText();
                                String matricula = txtMatricula.getText();
                                String nomeProjeto = txtNomeProjeto.getText();
                                String tipo = txtTipo.getText();

                                // Chamar a função de cadastrar aluno
                                AlunoController.cadastrarAluno(con, nome, id, matricula, nomeProjeto, tipo, "aluno",
                                        janelaNovoAluno);
                            }
                        });

                        janelaNovoAluno.setVisible(true);
                    }
                });
                

                janelaConsultaAlunos.getContentPane().add(botaoNovoAluno);

                // Criar a tabela
                JTable tabelaAlunos = new JTable();
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Matrícula");
                model.addColumn("Nome");
                model.addColumn("Nome do Projeto");
                model.addColumn("Tipo");

                // Ler os alunos e preencher a tabela
                ArrayList<Aluno> alunos = AlunoController.lerAlunos(con, id, janelaConsultaAlunos);
                for (Aluno aluno : alunos) {
                    model.addRow(new Object[] { aluno.getNome(), aluno.getMatricula(), aluno.getNomeProjeto(),
                            aluno.getTipo() });
                }

                tabelaAlunos.setModel(model);

                tabelaAlunos.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        int linhaSelecionada = tabelaAlunos.getSelectedRow();
                        int colunaID = 0;
                        String idAluno = (String) tabelaAlunos.getValueAt(linhaSelecionada, colunaID);
                        setAluno(idAluno, con);
                    }
                });

                // Adicionar a tabela a um JScrollPane
                JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
                scrollPane.setBounds(10, 90, 1250, 560);
                janelaConsultaAlunos.getContentPane().add(scrollPane);

                // Exibir a janela
                janelaConsultaAlunos.setLayout(null);
                janelaConsultaAlunos.setVisible(true);
            }
        });

        JButton botaoVerArtigos = new JButton("Consultar Artigos");
        botaoVerArtigos.setBounds(larguraAnterior - 1200, 70, 200, 30);
        painelSuperiorNova.add(botaoVerArtigos);
        
        

        botaoVerArtigos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chamar o método para listar os artigos
                ArrayList<Artigo> artigos = artigoController.listarArtigosPorProfessor(con, id);

                // Criar uma tabela para exibir os artigos
                String[] colunas = {"ID", "Título", "Resumo"};
                Object[][] dados = new Object[artigos.size()][3];

                for (int i = 0; i < artigos.size(); i++) {
                    Artigo artigo = artigos.get(i);
                    dados[i][0] = artigo.getId();
                    dados[i][1] = artigo.getTitulo();
                    dados[i][2] = artigo.getResumo();
                }

                DefaultTableModel model = new DefaultTableModel(dados, colunas);
                JTable tabelaArtigos = new JTable(model);

                tabelaArtigos.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int row = tabelaArtigos.rowAtPoint(e.getPoint());
                        int column = tabelaArtigos.columnAtPoint(e.getPoint());
                        
                        // Verificar se o clique foi em uma célula válida
                        if (row >= 0 && column >= 0) {
                            // Obter o ID do artigo na célula selecionada
                            int artigoId = (int) tabelaArtigos.getValueAt(row, 0);
                            
                            // Chamar o método abrirJanelaEditarArtigo(id)
                            abrirJanelaEditarArtigo(artigoId, con);
                        }
                    }
                });

                // Exibir a tabela em uma nova janela
                JFrame janelaArtigos = new JFrame("Lista de Artigos");
                janelaArtigos.setSize(1280, 720);

                JLabel labelTitulo = new JLabel("Lista de Artigos");
                labelTitulo.setBounds(10, 10, 200, 30);
                janelaArtigos.add(labelTitulo);

                JButton botaoNovoArtigo = new JButton("Novo Artigo");
                botaoNovoArtigo.setBounds(10, 50, 150, 30);
                janelaArtigos.add(botaoNovoArtigo);

                botaoNovoArtigo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Obtendo o ID do professor passado por parâmetro (substitua "professorId" pelo valor real)
                        String professorId = "professorId";
                        // Criando a janela do formulário de cadastro
                        JFrame janelaCadastro = new JFrame();
                        janelaCadastro.setTitle("Cadastro de Artigo");
                        janelaCadastro.setSize(500, 500);
                        janelaCadastro.setLayout(new GridLayout(4, 2));
                        // Campos do formulário
                        JLabel labelTitulo = new JLabel("Título:");
                        JTextField campoTitulo = new JTextField();

                        JLabel labelResumo = new JLabel("Resumo:");
                        JTextField campoResumo = new JTextField();

                        // Botão Cadastrar
                        JButton botaoCadastrar = new JButton("Cadastrar");

                        // Adicionando os componentes à janela de cadastro
                        janelaCadastro.add(labelTitulo);
                        janelaCadastro.add(campoTitulo);

                        janelaCadastro.add(labelResumo);
                        janelaCadastro.add(campoResumo);

                        janelaCadastro.add(new JLabel());
                        janelaCadastro.add(botaoCadastrar);

                        // Ação do botão Cadastrar
                        botaoCadastrar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Obtendo os valores dos campos do formulário
                                String titulo = campoTitulo.getText();
                                String resumo = campoResumo.getText();

                                // Criando o objeto Artigo
                                Artigo artigo = new Artigo();

                                artigo.setTitulo(titulo);
                                artigo.setResumo(resumo);

                                artigoController.cadastrarArtigo(con, artigo.getTitulo(), artigo.getResumo(), id , janelaCadastro);

                                janelaCadastro.dispose();
                            }
                        });

                        // Exibindo a janela de cadastro
                        janelaCadastro.setVisible(true);
                    }
                });

                JScrollPane scrollPane = new JScrollPane(tabelaArtigos);
                scrollPane.setBounds(10, 90, 1250, 580);
                janelaArtigos.add(scrollPane);

                janelaArtigos.setLayout(null);
                janelaArtigos.setVisible(true);
            }});




        botaoVerDisciplinas.addActionListener(e -> {
            // Criar a janela de consulta de disciplinas
            JFrame janelaDisciplina = new JFrame("Disciplinas");
            janelaDisciplina.setSize(1280, 720);
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

                Object[] rowData = { disciplina.getCodigo(), disciplina.getNome(), disciplina.getDescricao(),
                        disciplina.getCargaHoraria(), disciplina.getSalaAula(), disciplina.getHorario() };
                tableModel.addRow(rowData);
            }

            tabelaDisciplinas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = tabelaDisciplinas.rowAtPoint(e.getPoint());
                    int column = tabelaDisciplinas.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        int codigo = (int) tabelaDisciplinas.getValueAt(row, 0);
                        abrirJanelaFormulario(janelaNova, codigo, con);

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
        
        botaoGerarRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFrame janela = new JFrame();
                    janela.setTitle("Relatório");
                    janela.setSize(1280, 720);
                    janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    janela.setLayout(new GridLayout(0, 1)); // Layout para organizar as tabelas verticalmente
                    int espacamentoPadrao = 10; // Espaçamento entre as tabelas

                    // Preencher a lista de atividades
                    ArrayList<Atividades> atividades = AtividadeController.lerAtividades(con, id);
                    String[] atividadesColumnNames = {"ID", "Nome", "Descrição", "Data"};
                    Object[][] atividadesData = new Object[atividades.size()][4];
                    for (int i = 0; i < atividades.size(); i++) {
                        Atividades atividade = atividades.get(i);
                        atividadesData[i][0] = atividade.getId();
                        atividadesData[i][1] = atividade.getNome();
                        atividadesData[i][2] = atividade.getDescricao();
                        atividadesData[i][3] = atividade.getData();
                    }

                    // Tabela de atividades
                    JLabel labelAtividades = new JLabel("Atividades");
                    janela.add(labelAtividades);
                    JTable tabelaAtividades = new JTable(atividadesData, atividadesColumnNames);
                    JScrollPane scrollAtividades = new JScrollPane(tabelaAtividades);
                    janela.add(scrollAtividades);

                    // Espaçamento entre as tabelas
                    janela.add(Box.createRigidArea(new Dimension(0, espacamentoPadrao)));

                    // Preencher a lista de alunos
                    ArrayList<Aluno> alunos = AlunoController.lerAlunos(con, id, janela);
                    String[] alunosColumnNames = {"Nome", "Matrícula", "Nome do Projeto", "Tipo"};
                    Object[][] alunosData = new Object[alunos.size()][4];
                    for (int i = 0; i < alunos.size(); i++) {
                        Aluno aluno = alunos.get(i);
                        alunosData[i][0] = aluno.getNome();
                        alunosData[i][1] = aluno.getMatricula();
                        alunosData[i][2] = aluno.getNomeProjeto();
                        alunosData[i][3] = aluno.getTipo();
                    }

                    // Tabela de alunos
                    JLabel labelAlunos = new JLabel("Alunos");
                    janela.add(labelAlunos);
                    JTable tabelaAlunos = new JTable(alunosData, alunosColumnNames);
                    JScrollPane scrollAlunos = new JScrollPane(tabelaAlunos);
                    janela.add(scrollAlunos);

                    // Espaçamento entre as tabelas
                    janela.add(Box.createRigidArea(new Dimension(0, espacamentoPadrao)));

                    // Preencher a lista de artigos
                    ArrayList<Artigo> artigos = artigoController.listarArtigosPorProfessor(con, id);
                    String[] artigosColumnNames = {"ID", "Título", "Resumo"};
                    Object[][] artigosData = new Object[artigos.size()][3];
                    for (int i = 0; i < artigos.size(); i++) {
                        Artigo artigo = artigos.get(i);
                        artigosData[i][0] = artigo.getId();
                        artigosData[i][1] = artigo.getTitulo();
                        artigosData[i][2] = artigo.getResumo();
                    }

                    // Tabela de artigos
                    JLabel labelArtigos = new JLabel("Artigos");
                    janela.add(labelArtigos);
                    JTable tabelaArtigos = new JTable(artigosData, artigosColumnNames);
                    JScrollPane scrollArtigos = new JScrollPane(tabelaArtigos);
                    janela.add(scrollArtigos);

                    // Espaçamento entre as tabelas
                    janela.add(Box.createRigidArea(new Dimension(0, espacamentoPadrao)));

                    // Preencher a lista de disciplinas
                    ArrayList<Disciplina> disciplinas = DisciplinaController.listarDisciplinas(janela, con, id);
                    String[] disciplinasColumnNames = {"Código", "Nome", "Descrição", "Carga Horária", "Sala de Aula", "Horário"};
                    Object[][] disciplinasData = new Object[disciplinas.size()][6];
                    for (int i = 0; i < disciplinas.size(); i++) {
                        Disciplina disciplina = disciplinas.get(i);
                        disciplinasData[i][0] = disciplina.getCodigo();
                        disciplinasData[i][1] = disciplina.getNome();
                        disciplinasData[i][2] = disciplina.getDescricao();
                        disciplinasData[i][3] = disciplina.getCargaHoraria();
                        disciplinasData[i][4] = disciplina.getSalaAula();
                        disciplinasData[i][5] = disciplina.getHorario();
                    }

                    // Tabela de disciplinas
                    JLabel labelDisciplinas = new JLabel("Disciplinas");
                    janela.add(labelDisciplinas);
                    JTable tabelaDisciplinas = new JTable(disciplinasData, disciplinasColumnNames);
                    JScrollPane scrollDisciplinas = new JScrollPane(tabelaDisciplinas);
                    janela.add(scrollDisciplinas);

                    // Exibir a janela
                    janela.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " + ex.getMessage());
                }
            }
        });

      


        
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
        nomeLabel.setBounds(150, 130, 100, 30);
        JTextField nomeTextField = new JTextField(professor.getName());
        nomeTextField.setBounds(260, 130, 250, 30);

        JLabel grauAcademicoLabel = new JLabel("Grau Acadêmico:");
        grauAcademicoLabel.setBounds(150, 180, 130, 30);
        JTextField grauAcademicoTextField = new JTextField(professor.getAcademicDegree());
        grauAcademicoTextField.setBounds(290, 180, 250, 30);

        JLabel salarioLabel = new JLabel("Salario:");
        salarioLabel.setBounds(150, 230, 100, 30);
        JTextField salarioTextField = new JTextField(String.valueOf(professor.getSalary()));
        salarioTextField.setBounds(260, 230, 250, 30);

        JLabel areaLabel = new JLabel("Area:");
        areaLabel.setBounds(150, 280, 100, 30);
        JTextField areaTextField = new JTextField(professor.getArea());
        areaTextField.setBounds(260, 280, 250, 30);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.setBounds(150, 330, 150, 30);

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                professor.setName(nomeTextField.getText());
                professor.setAcademicDegree(grauAcademicoTextField.getText());
                professor.setSalary(Double.parseDouble(salarioTextField.getText()));
                professor.setArea(areaTextField.getText());

                Crud.atualizarProfessor(janelaNova, connection, id, professor);
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
