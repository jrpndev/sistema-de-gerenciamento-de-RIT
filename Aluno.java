public class Aluno {
    private String nome;
    private String matricula;
    private String nomeProjeto;
    private String tipo;

    public Aluno(String nome, String matricula, String nomeProjeto, String tipo) {
        this.nome = nome;
        this.matricula = matricula;
        this.nomeProjeto = nomeProjeto;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
