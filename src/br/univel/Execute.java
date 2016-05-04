package br.univel;

import java.sql.SQLException;

public class Execute {

	public static void main(String[] args) throws SQLException {
		Dao<Cliente, Cliente> d = new Daoimpl<Cliente, Cliente>();
		Cliente c = new Cliente(1,"mau","1234","naosei", EstadoCivil.CASADO.getidestado());
		Cliente c2 = new Cliente(2,"fa","4321","naosei", EstadoCivil.VIUVO.getidestado());
		Cliente c3 = new Cliente(3,"patty","9876","naosei", EstadoCivil.SOLTEIRO.getidestado());
		
		d.delete(c);
		d.create(c);
		d.salvar(c);
		d.salvar(c2);
		d.salvar(c3);
		d.listarTodos();
		d.buscar(c);
		d.atualizar(c);
		d.excluir(c2);
		d.listarTodos();
	}
	
}
