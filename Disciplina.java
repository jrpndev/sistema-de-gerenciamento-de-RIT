public class Disciplina {
    private int codigo;
    private String nome;
    private String descricao;
    private int cargaHoraria;
    private String salaAula;
    private String horario;

    public Disciplina(String nome, String descricao, int cargaHoraria, String salaAula, String horario) {
        
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.salaAula = salaAula;
        this.horario = horario;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getSalaAula() {
        return salaAula;
    }

    public void setSalaAula(String salaAula) {
        this.salaAula = salaAula;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
