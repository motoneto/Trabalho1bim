package br.univel;

@Tabela("CAD_CLIENTE")
public class Cliente {
	@Coluna(pk=true)
	private int id;
	@Coluna(nome="CLNOME")
	private String nome;
	@Coluna(nome="CLENDEREÇO")
	private String endereço;
	@Coluna(nome="CLTELEFONE")
	private String telefone;
	@Coluna(nome="CLESTADOCIVIL")
	private int estadoCivil;
	

	public Cliente(int id, String nome, String endereço, String telefone, int i) {
		super();
		this.id = id;
		this.nome = nome;
		this.endereço = endereço;
		this.telefone = telefone;
		this.estadoCivil = i;
	}

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

	public String getEndereço() {
		return endereço;
	}

	public void setEndereço(String endereço) {
		this.endereço = endereço;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public int getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(int estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	
}
