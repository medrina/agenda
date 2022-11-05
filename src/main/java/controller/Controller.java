package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.Contato;

// url mapeada que vai acessar o Servlet
@WebServlet("/controller")
public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	
	public Controller() {
	}
    
	// quando a requisição http vier por GET, o método doGet será acionado
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	// resposta de acesso de domínio concedido a: http://localhost, e por método GET
    	response.addHeader("Access-Control-Allow-Origin","http://localhost");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		String str = request.getParameter("prm");
				
		// vai fazer a consulta de listar todos os contatos no Banco
		if(str.equals("listar")) {
			List<Contato> contatos = new ArrayList<>();
			contatos = listarTodos();
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(converteParaJSON(contatos));
			out.flush();
		}
		
		// vai fazer a consulta pelo id, e retornará um único contato
		else if(str.equals("unico")) {
			Long id = Long.parseLong(request.getParameter("id"));
			List<Contato> unicoContato = new ArrayList<>();
			unicoContato.add(listarUnicoContato(id));
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(converteParaJSON(unicoContato));
			out.flush();
		}
		
		// vai gerar um  relatório PDF mostrando todos os contatos da Agenda
		else if(str.equals("pdf")) {
			Document documento = new Document();
			try {
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "inline; filename=contatos.pdf");
				PdfWriter.getInstance(documento, response.getOutputStream());
				documento.open();
				documento.add(new Paragraph("Lista de contatos"));
				documento.add(new Paragraph(" "));// quebra de linha
				PdfPTable tabela = new PdfPTable(3);
				
				// cabeçalho
				PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
				PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
				PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
				tabela.addCell(col1);
				tabela.addCell(col2);
				tabela.addCell(col3);
				
				// conteúdo da tabela. Preenchimento da tabela com os contatos
				ArrayList<Contato> lista = dao.listarContatos();
				
				for(int i=0; i<lista.size(); i++) {
					
					// exibir o resultado no documento pdf, usar o objeto tabela adicionando as linhas da tabela
					tabela.addCell(lista.get(i).getNome());// 1ª coluna
					tabela.addCell(lista.get(i).getFone());// 2ª coluna
					tabela.addCell(lista.get(i).getEmail());// 3ª coluna
				}
				
				// preenche os dados dos contatos no documento pdf
				documento.add(tabela);
				
				// fecha o documento pdf
				documento.close();
			
			} catch(Exception e) {
				System.out.println(e);
				documento.close();
			}
		}
	}
	
	// quando a requisição http vier por POST, o método doPost será acionado
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// resposta de acesso de domínio concedido a: http://localhost, e por método POST
		response.addHeader("Access-Control-Allow-Origin","http://localhost");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		String str = request.getParameter("prm");
		Contato novoContato = new Contato();
		novoContato.setNome(request.getParameter("nom"));
		novoContato.setFone(request.getParameter("fon"));
		novoContato.setEmail(request.getParameter("mail"));
		
		// criar novo contato
		if(str.equals("criar")) {
			dao.inserirContato(novoContato);
		}
		// editar contato já existente
		else if(str.equals("editar")) {
			novoContato.setId(Long.parseLong(request.getParameter("id")));
			dao.alterarContato(novoContato);
		}
		// apagar contato
		else if(str.equals("apagar")) {
			long id = Long.parseLong(request.getParameter("id"));
			dao.deletarContato(id);
		}
	}
	
	// faz a consulta e retorna uma lista de contatos da Agenda
	private List<Contato> listarTodos() {
		ArrayList<Contato> contatos = dao.listarContatos();
		return contatos;
	}
	
	// lista um único contato
	private Contato listarUnicoContato(Long id) {
		Contato user = dao.selecionarContato(id);
		return user;
	}
	
	// faz a conversÃ£o do objeto contato em formato JSON.
	private String converteParaJSON(List lista) {
		
		// biblioteca de conversÃ£o para JSON.  gson-2.8.2.jar
		Gson gson = new Gson();
		String json = gson.toJson(lista);
		return json;
	}
	
}