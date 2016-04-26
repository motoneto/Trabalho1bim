package br.univel;

@Tabela("CAD_CLIENTE")
public class Cliente {
	@Coluna(pk=true)
	private int id;
	@Coluna(nome="CLNOME")
	private String nome;
	@Coluna(nome="CLENDERE�O")
	private String endere�o;
	@Coluna(nome="CLTELEFONE")
	private String telefone;
	private EstadoCivil estadoCivil;
	public Cliente() {
		this(0, null, null, null, null);
	}

	public Cliente(int id, String nome, String endere�o, String telefone, EstadoCivil estadocivil) {
		super();
		this.id = id;
		this.nome = nome;
		this.endere�o = endere�o;
		this.telefone = telefone;
		this.estadoCivil = estadocivil;
	}	
}
