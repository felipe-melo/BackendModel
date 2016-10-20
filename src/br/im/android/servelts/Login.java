package br.im.android.servelts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import br.im.android.entidades.Usuario;
import br.im.android.exceptions.ConexaoException;

@WebServlet("/login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			Usuario usuario = this.logar(req);
			jsonResult.put("sucesso", 1);
			jsonResult.put("resposta", usuario.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				jsonResult.put("sucesso", 0);
				jsonResult.put("resposta", "Erro na requisição");
			}catch (JSONException je) {}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			try {
				jsonResult.put("sucesso", 0);
				jsonResult.put("resposta", "Erro no banco de dados");
			}catch (JSONException je) {}
		} catch (ConexaoException e) {
			e.printStackTrace();
			try {
				jsonResult.put("sucesso", 0);
				jsonResult.put("resposta", "Erro de conexão");
			}catch (JSONException je) {}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				jsonResult.put("sucesso", 0);
				jsonResult.put("resposta", "Erro ao tentar criar novo usuário");
			}catch (JSONException je) {}
		} finally {
			PrintWriter out = resp.getWriter();  
			out.print(jsonResult);
			out.flush();
		}
	}
	
	private Usuario logar(HttpServletRequest req) throws IOException, JSONException, ClassNotFoundException, ConexaoException, SQLException {
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		BufferedReader reader = req.getReader();
		while ((line = reader.readLine()) != null)
			jb.append(line);

		JSONObject json = new JSONObject(jb.toString());
		
		return Usuario.logar(json.getString("email"), json.getString("senha"));
	}
}
