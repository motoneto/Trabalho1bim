package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Daoimpl<T, K> implements Dao<T, K> {
	private Object[] vetor = new Object[100];
	private static Connection abrirConexao() throws SQLException {

		String url = "jdbc:h2:~/BancoMauricio";
		String user = "sa";
		String pass = "sa";
		Connection con = DriverManager.getConnection(url, user, pass);
		return con;

	}
	@Override
	public void salvar(T t) {//getSqlInsert
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert = s.getSqlInsert(con, t);
			psInsert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void atualizar(T t) {//getSqlUpdateById
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert = s.getSqlUpdateById(con, t);
			psInsert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void delete(T t) {//droptable
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert =s.getDropTable(con, t);
			psInsert.execute();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<T> listarTodos(){//listatodos
		Cliente t = new Cliente(0, null, null, null, 0);
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		List<Cliente> array = new ArrayList<Cliente>();
		try {
			con = abrirConexao();
			PreparedStatement ps = s.getSqlSelectAll(con, t);
			ResultSet result = null;
			try {
				result = ps.executeQuery();
				
				while (result.next()) {
					int id = result.getInt(1);
					String nome = result.getString("CLNOME");
					String end = result.getString("CLENDEREÇO");
					String tel = result.getString("CLTELEFONE");
					int est = result.getInt("CLESTADOCIVIL");
					String estado = null;
					if(est == 1){
						estado = EstadoCivil.SOLTEIRO.getestadoCiv();
					}else if(est == 2){
						estado = EstadoCivil.CASADO.getestadoCiv();
					}else if(est == 3){
						estado = EstadoCivil.VIUVO.getestadoCiv();
					}
					array.add(new Cliente(id, nome,end,tel,est));
				for (Cliente l: array) {
		            System.out.println("ID: \t\t\t" + l.getId());
		            System.out.println("NOME: \t\t\t" + l.getNome());
		            System.out.println("ENDERECO: \t\t" + l.getEndereço());
		            System.out.println("TELEFONE: \t\t" + l.getTelefone());
		            System.out.println("ESTADO CIVIL: \t\t" + estado);
		        }
				}
			} finally {
				if (ps != null) ps.close();
				if (result != null) result.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	@Override
	public void create(T t) {//getcratetable
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert = s.getCreateTable(con, t);
			psInsert.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void excluir(K k) {//deletebyid
		SqlGen s = new SqlGenImpl();
		Cliente c = new Cliente(0, null, null, null, 0);
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert = s.getSqlDeleteById(con, k);
			psInsert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void buscar(K k) {//selectByid
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			s.getSqlSelectById(con, k);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
