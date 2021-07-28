package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/deleteAll",
		"/report" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// teste de conexao
		// dao.testeConexao();

		String action = request.getServletPath();
		System.out.println(action);

		if (action.equals("/main")) {
			contatos(request, response);
		}else if(action.equals("/insert")) {
			novoContato(request, response);
		} else if(action.equals("/select")) {
			listarContato(request, response);
		}else if(action.equals("/update")) {
			editarContato(request, response);
		}else if(action.equals("/delete")) {
			excluirContato(request, response);
		}else if(action.equals("/deleteAll")) {
			excluirTodos(request, response);
		}else if(action.equals("/report")) {
			gerarRelatorio(request, response);
		}else {
			response.sendRedirect("index.html");
		}

	}
	//listar contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// criando um objeto que irá receber os dados javabeans
		ArrayList<JavaBeans> lista = dao.listarContatos();
		// encaminhar a lista ao documento agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
	}
	// novo contato
	protected void novoContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		dao.inserirContato(contato);
		// redirecionar para o doc agenda.jsp
		response.sendRedirect("main");
	}
	// editar contato
	protected void listarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// recebimento do id
		String idcon = request.getParameter("idcon");
		// setar a variavel javabeans
		contato.setIdcon(idcon);
		// executar o metodo selecionar contato
		dao.selecionarContato(contato);
		// setar os atributos do formulario com o conteudo javabeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		// encaminhar ao docmento editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
	}
	// editar o contato method
	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// setar as variaveis java beans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		dao.alterarContado(contato);
		
		// redirect to agenda.jsp
		response.sendRedirect("main");
	}
	
	protected void excluirContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idcon = request.getParameter("idcon");
		contato.setIdcon(idcon);
		dao.excluirContato(contato);
		// redirect to agenda.jsp
		response.sendRedirect("main");
	}
	
	protected void excluirTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dao.excluirTodos(contato);
		response.sendRedirect("main");
	}
	
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Document documento = new Document();
		
		try {
			response.setContentType("apllication/pdf");
			// nome do documento
			response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
			//criar o documento
			PdfWriter.getInstance(documento, response.getOutputStream());
			//abrir o documento
			documento.open();
			documento.add(new Paragraph("Lista de contatos: "));
			documento.add(new Paragraph(" "));
			// criar uma tabela
			PdfPTable tabela = new PdfPTable(3);
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("Email"));
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			
			ArrayList<JavaBeans> lista = dao.listarContatos();
			for(int i =0; i<lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			
			documento.add(tabela);
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
	}
	
}
