package br.univel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlGenImpl extends SqlGen{

	@Override
	protected PreparedStatement getCreateTable(Connection con, Object obj) {
		Class<?> cl = obj.getClass();
		try {

			StringBuilder sb = new StringBuilder();

				String nomeTabela;
				if (cl.isAnnotationPresent(Tabela.class)) {
					Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
					nomeTabela = anotacaoTabela.value();
				} else {
					nomeTabela = cl.getSimpleName().toUpperCase();
				}
				
				sb.append("CREATE TABLE ").append(nomeTabela).append("(");

			Field[] atributos = cl.getDeclaredFields();

			for (int i = 0; i < atributos.length; i++) {

					Field field = atributos[i];

					String nomeColuna;
					String tipoColuna;

					if (field.isAnnotationPresent(Coluna.class)) {
						Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

						if (anotacaoColuna.nome().isEmpty()) {
							nomeColuna = field.getName().toUpperCase();
						} else {
							nomeColuna = anotacaoColuna.nome();
						}

					} else {
						nomeColuna = field.getName().toUpperCase();
					}

					Class<?> tipoParametro = field.getType();

					if (tipoParametro.equals(String.class)) {
						tipoColuna = "VARCHAR(100)";

					} else if (tipoParametro.equals(int.class)) {
						tipoColuna = "INT";
					} else if (tipoParametro.equals(EstadoCivil.class)) {
						tipoColuna = "VARCHAR(100)";	
					} else {
						tipoColuna = "DESCONHECIDO";
					}

					if (i > 0) {
						sb.append(",");
					}

					sb.append(nomeColuna).append(' ').append(tipoColuna);
				}

				sb.append(", PRIMARY KEY( ");

				for (int i = 0, achou = 0; i < atributos.length; i++) {

					Field field = atributos[i];

					if (field.isAnnotationPresent(Coluna.class)) {

						Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

						if (anotacaoColuna.pk()) {

							if (achou > 0) {
								sb.append(", ");
							}

							if (anotacaoColuna.nome().isEmpty()) {
								sb.append(field.getName().toUpperCase());
							} else {
								sb.append(anotacaoColuna.nome());
							}

							achou++;
						}

					}
				}

				sb.append(" )");
			
			sb.append(" );");
			String strSql = sb.toString();
			System.out.println("SQL GERADO: " + strSql);

			PreparedStatement ps = null;
			try {
				ps = con.prepareStatement(strSql);

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			return ps;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected PreparedStatement getDropTable(Connection con, Object obj) {
		Class<?> cl = obj.getClass();
		StringBuilder sb = new StringBuilder();
		
		String nomeTabela;
		
		if (cl.isAnnotationPresent(Tabela.class)) {
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}
		
		sb.append("DROP TABLE IF EXISTS ").append(nomeTabela);
		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(strSql);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlInsert(Connection con, Object obj) {
		Class<?> cl = obj.getClass();
		StringBuilder sb = new StringBuilder();
		
		
			String nomeTabela;
			if (cl.isAnnotationPresent(Tabela.class)) {

				Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
				nomeTabela = anotacaoTabela.value();

			} else {
				nomeTabela = cl.getSimpleName().toUpperCase();

			}
			sb.append("INSERT INTO ").append(nomeTabela).append("(");
		

		Field[] atributos = cl.getDeclaredFields();

		{
			for (int i = 0; i < atributos.length; i++) {

				Field field = atributos[i];

				String nomeColuna;

				if (field.isAnnotationPresent(Coluna.class)) {
					Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

					if (anotacaoColuna.nome().isEmpty()) {
						nomeColuna = field.getName().toUpperCase();
					} else {
						nomeColuna = anotacaoColuna.nome();
					}

				} else {
					nomeColuna = field.getName().toUpperCase();
				}

				if (i > 0) {
					sb.append(", ");
				}

				sb.append(nomeColuna);
			}
		}

		sb.append(")VALUES(");
		for (int i = 0; i < atributos.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append('?');
		}
		sb.append(')');
		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(strSql);

			for (int i = 0; i < atributos.length; i++) {
				Field field = atributos[i];

				field.setAccessible(true);

				if (field.getType().equals(int.class)) {
					ps.setInt(i + 1, field.getInt(obj));

				} else if (field.getType().equals(String.class)) {
					ps.setString(i + 1, String.valueOf(field.get(obj)));
					
				} else if (field.getType().equals(EstadoCivil.class)) {
					ps.setString(i + 1, String.valueOf(field.get(obj)));	

				} else {
					throw new RuntimeException("Tipo não suportado, falta implementar.");

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlSelectAll(Connection con, Object obj) {
		Class<?> cl = obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}
		sb.append("SELECT * FROM ").append(nomeTabela);
		String strSql = sb.toString();
		//System.out.println("SQL GERADO: " + strSql);
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(strSql);
			
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlSelectById(Connection con, Object obj) {

		Class<?> cl = obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();

		}
		SqlGen s = new SqlGenImpl();
		Cliente t = new Cliente();
		int ids = 0;
		PreparedStatement p = s.getSqlSelectAll(con, t);
		ResultSet res = null;
		try {
			try {
				res = p.executeQuery();
				while (res.next()) {
					if(res.getInt(1) == 1){
					ids = res.getInt(1);
					}
				}
			}finally {
					if (p != null) p.close();
					if (res != null) res.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		sb.append("SELECT * FROM ").append(nomeTabela).append(" WHERE ID = ").append(ids);
		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);
		Statement ps = null;
		ResultSet result = null;

		try {
			ps = con.prepareStatement(strSql);
			try {
				ps = con.createStatement();
				result = ps.executeQuery(""+strSql);
				
				while (result.next()) {
					int id = result.getInt(1);
					String nome = result.getString("CLNOME");
					String end = result.getString("CLENDEREÇO");
					String tel = result.getString("CLTELEFONE");
					String est = result.getString("CLESTADOCIVIL");
					System.out.println(id + " " + nome + " " + end + " " + tel + " " +est);
				}
			} finally {
				if (ps != null) ps.close();
				if (result != null) result.close();
			}
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	}

		return null;
	}
	
	@Override
	protected PreparedStatement getSqlUpdateById(Connection con, Object obj) {
		Class<?> cl = obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();

		}
		SqlGen s = new SqlGenImpl();
		Cliente t = new Cliente();
		int ids = 0;
		PreparedStatement p = s.getSqlSelectAll(con, t);
		ResultSet res = null;
		try {
			try {
				res = p.executeQuery();
				while (res.next()) {
					if(res.getInt(1) == 1){
					ids = res.getInt(1);
					}
				}
			}finally {
					if (p != null) p.close();
					if (res != null) res.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		sb.append("UPDATE ").append(nomeTabela).append(" SET CLNOME = 'ALTERADO' WHERE id = ").append(ids);
		
		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);

		PreparedStatement ps = null;
	try{
		ps = con.prepareStatement(strSql);

	} catch (SQLException e) {
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlDeleteById(Connection con, Object obj) {
		Class<?> cl = obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();

		}
		SqlGen s = new SqlGenImpl();
		Cliente t = new Cliente();
		int id = 0;
		PreparedStatement ps = s.getSqlSelectAll(con, t);
		ResultSet result = null;
		try {
			try {
				result = ps.executeQuery();
				while (result.next()) {
					if(result.getInt(1) == 2){
					id = result.getInt(1);
					}
				}
			}finally {
					if (ps != null) ps.close();
					if (result != null) result.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		sb.append("DELETE FROM ").append(nomeTabela).append(" WHERE ID = ").append(id);
		
		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);

		PreparedStatement s1 = null;
	try{
		ps = con.prepareStatement(strSql);

	} catch (SQLException e) {
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	}

	return ps;
	}
	
}
