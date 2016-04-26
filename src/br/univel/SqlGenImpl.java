package br.univel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.univel.EstadoCivil;

public class SqlGenImpl extends SqlGen{

	/*private Connection con;
	
	private void StartConnection() throws SQLException {

		String url = "jdbc:h2:C:/Users/admins/Desktop/Banco de dados/agenda";
		String user = "sa";
		String pass = "sa";

		setCon(DriverManager.getConnection(url, user, pass));

	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	private void CloseConnection() throws SQLException {
		getCon().close();
	}*/
	@Override
	protected String getCreateTable(Connection con, Object obj) {
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();
		try {

			StringBuilder sb = new StringBuilder();

				String nomeTabela;
				if (cl.isAnnotationPresent(Tabela.class)) {
					Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
					nomeTabela = anotacaoTabela.value();
				} else {
					nomeTabela = cl.getSimpleName().toUpperCase();
				}
				
				sb.append("CREATE TABLE ").append(nomeTabela).append(" (");

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
						tipoColuna = "INT(1)";	
					} else {
						tipoColuna = "DESCONHECIDO";
					}

					if (i > 0) {
						sb.append(",");
					}

					sb.append("\n\t").append(nomeColuna).append(' ').append(tipoColuna);
				}

				sb.append(",\n\tPRIMARY KEY( ");

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
			
			sb.append("\n);");

			return sb.toString();

		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected String getDropTable(Connection con, Object obj) {
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();
		StringBuilder sb = new StringBuilder();
		
		String nomeTabela;
		
		if (cl.isAnnotationPresent(Tabela.class)) {
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}
		
		sb.append("DROP TABLE ").append(nomeTabela);
		return sb.toString();
	}

	@Override
	protected PreparedStatement getSqlInsert(Connection con, Object obj) {
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();

		StringBuilder sb = new StringBuilder();
		
			String nomeTabela;
			if (cl.isAnnotationPresent(Tabela.class)) {

				Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
				nomeTabela = anotacaoTabela.value();

			} else {
				nomeTabela = cl.getSimpleName().toUpperCase();

			}
			sb.append("INSERT INTO ").append(nomeTabela).append(" (");
		

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

		sb.append(") VALUES (");

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

				} else {
					throw new RuntimeException("Tipo n�o suportado, falta implementar.");

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
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();
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
	protected PreparedStatement getSqlSelectById(Connection con, Object obj) {
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();

		}
		sb.append("SELECT * FROM ").append(nomeTabela).append("WHERE ID = 1");
		
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
	protected PreparedStatement getSqlUpdateById(Connection con, Object obj) {
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();

		}
		sb.append("UPDATE").append(nomeTabela).append("NOME = 'MAURICIO' WHERE ID = 1");
		
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
		@SuppressWarnings("unchecked")
		Class<Cliente> cl = (Class<Cliente>) obj.getClass();
		StringBuilder sb = new StringBuilder();
		String nomeTabela;
		if (cl.isAnnotationPresent(Tabela.class)) {

			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();

		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();

		}
		sb.append("DELETE FROM").append(nomeTabela).append("WHERE ID = 1");
		
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
	public static void main(String[] args){
		
	}
}
