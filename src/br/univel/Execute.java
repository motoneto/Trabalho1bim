package br.univel;

import java.sql.SQLException;

public class Execute {

	public static void main(String[] args) throws SQLException {
		Dao<Cliente, Cliente> d = new Daoimpl<Cliente, Cliente>();
		Cliente c = new Cliente(1,"mau","1234","naosei", EstadoCivil.CASADO);
		Cliente c2 = new Cliente(1,"fa","4321","naosei", EstadoCivil.SOLTEIRO);
		Cliente c3 = new Cliente(1,"patty","9876","naosei", EstadoCivil.VIUVO);
		
		d.excluir(c);
		d.create(c);
		d.salvar(c);
		d.salvar(c2);
		d.salvar(c3);
		d.listarTodos();
		d.buscar(c);
		d.atualizar(c);
		d.delete(c);
		d.listarTodos();		
	}
	
}
