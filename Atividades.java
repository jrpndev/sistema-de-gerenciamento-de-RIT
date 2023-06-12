import java.util.Date;

public class Atividades {
    private int id;
    private String nome;
    private String descricao;
    private Date data;

    // Construtor
    public Atividades(int id, String nome, String descricao, Date data) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    // Outros métodos da classe Atividades
}
