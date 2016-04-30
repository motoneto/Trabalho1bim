package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Daoimpl<T, K> implements Dao<T, K> {
	private static Connection abrirConexao() throws SQLException {

		String url = "jdbc:h2:~/BancoMauricio";
		String user = "sa";
		String pass = "sa";
		Connection con = DriverManager.getConnection(url, user, pass);
		return con;

	}
	@Override
	public void salvar(T t) {
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
	public void atualizar(T t) {
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
	public void delete(T t) {
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
	@Override
	public List<T> listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T buscar(K k) {
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert = s.getSqlSelectAll(con, k);
			psInsert.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void create(T t) {
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
	public void excluir(K k) {
		SqlGen s = new SqlGenImpl();
		Connection con = null;
		try {
			con = abrirConexao();
			PreparedStatement psInsert = s.getSqlDeleteById(con, k);
			psInsert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
