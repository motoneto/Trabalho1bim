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
	private EstadoCivil estadoCivil;
	public Cliente() {
		this(0, null, null, null, null);
	}

	public Cliente(int id, String nome, String endereço, String telefone, EstadoCivil estadocivil) {
		super();
		this.id = id;
		this.nome = nome;
		this.endereço = endereço;
		this.telefone = telefone;
		this.estadoCivil = estadocivil;
	}	
}
