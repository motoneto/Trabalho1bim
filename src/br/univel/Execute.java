package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Execute {

	public static void main(String[] args) throws SQLException {
		Dao d = new Daoimpl();
		Cliente c = new Cliente();
		c.setId(1);
		c.setNome("Mau");
		c.setTelefone("123456");
		c.setEndereço("Nao sei");
		
		d.excluir(c);
		d.create(c);
		d.salvar(c);
		d.salvar(c);
		d.salvar(c);
		d.listarTodos();
		d.buscar(c);
		d.atualizar(c);
		d.delete(c);
		d.listarTodos();		
	}
	
}
