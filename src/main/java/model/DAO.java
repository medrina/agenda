package model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

//somente a Classe DAO vai conseguir conectar com o Banco de Dados (através dos métodos públicos)
public class DAO {
	
	/* PARÂMETROS DE CONEXÃO */
	
	// driver do MySQL:  mysql-connector-java-5.1.6-bin.jar
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String password = "";
	
	// -----------------------------------------------------------------------------------------------------------
	/*MÉTODOS DE CONEXÃO */
	
	// método privado que contém os parâmetros de conexão com o Banco. Esse método será utilizado apenas pela classe DAO para trabalhar com o Banco de Dados
	private Connection conectar() {
		Connection con = null;
		
		try {
			Class.forName(driver);
			con = (Connection) DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);// vai imprimir o erro
			return null;
		}
	}
	
	// CRUD - CREATE (método que vai criar um novo contato no Banco de Dados)
	public void inserirContato(Contato novoContato) {
		String create = "INSERT INTO CONTATOS VALUE(NULL,?,?,?)";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(create);
			pst.setString(1, novoContato.getNome());
			pst.setString(2, novoContato.getFone());
			pst.setString(3, novoContato.getEmail());
			pst.executeUpdate();
			con.close();
		}
		catch(Exception e) {
			System.out.println("ERRO: "+ e);
		}
	}
	
	// CRUD - READ (método que será responsável pela listagem de todos os contatos que estão cadastrados no Banco de Dados)
	public ArrayList<Contato> listarContatos(){
		ArrayList<Contato> contatos = new ArrayList<Contato>();
		String read = "SELECT * FROM CONTATOS ORDER BY NOME";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(read);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Long id = rs.getLong(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);
				contatos.add(new Contato(id,nome,fone,email));
			}
			con.close();
			return contatos;
		}
		catch (Exception e) {
			System.out.println("ERRO: "+ e);
			return null;
		}	
	}
	
	// CRUD - READ (método que vai listar apenas um contato pelo seu id)
	public Contato selecionarContato(Long id) {
		String query = "SELECT * FROM CONTATOS WHERE ID = ?";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(query);
			pst.setLong(1, id);
			ResultSet rs = pst.executeQuery();
			Contato user = new Contato();
			while(rs.next()) {
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setFone(rs.getString(3));
				user.setEmail(rs.getString(4));
			}
			con.close();
			return user;
		}
		catch(Exception e) {
			System.out.println("ERRO: "+ e);
			return null;
		}
	}
	
	// CRUD - UPDATE (método que vai atualizar o contato por dados editados pelo usuário)
	public void alterarContato(Contato user) {
		String create = "UPDATE CONTATOS SET NOME = ?, FONE = ?, EMAIL = ? WHERE ID = ?";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(create);
			pst.setString(1, user.getNome());
			pst.setString(2, user.getFone());
			pst.setString(3, user.getEmail());
			pst.setLong(4, user.getId());
			pst.executeUpdate();
			con.close();
		}
		catch(Exception e) {
			System.out.println("ERRO: "+ e);
		}
	}
	
	// CRUD - DELETE (método que vai apagar contato do Banco de Dados através do seu id)
	public void deletarContato(long id) {
		String delete = "DELETE FROM CONTATOS WHERE ID = ?";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(delete);
			pst.setLong(1, id);
			pst.executeUpdate();
			con.close();
		}
		catch(Exception e) {
			System.out.println("ERRO: "+ e);
		}
	}
	
	/*
	// método de teste rápido de conexão com o Banco de Dados 
	public void testeConexao() {
		
		try {
			Connection con = conectar();
			System.out.println(con);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}*/

}