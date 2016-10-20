package br.im.android.servelts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import br.im.android.entidades.Endereco;
import br.im.android.entidades.Usuario;
import br.im.android.exceptions.ConexaoException;

@WebServlet("/novoUsuario")
public class NovoUsuario extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		
		JSONObject jsonResult = new JSONObject();
		
		try {
			this.novoUsuario(req);
			jsonResult.put("sucesso", 1);
			jsonResult.put("resposta", "Cadastro realizado com sucesso");
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
	
	private void novoUsuario(HttpServletRequest req) throws IOException, JSONException, ClassNotFoundException, ConexaoException, SQLException {
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		BufferedReader reader = req.getReader();
		while ((line = reader.readLine()) != null)
			jb.append(line);

		JSONObject json = new JSONObject(jb.toString());
		
		JSONObject jsonEnd = json.getJSONObject("endereco");
		
		Endereco endereco = new Endereco(jsonEnd.getString("endereco"), jsonEnd.getString("numero"), jsonEnd.getString("cidade"));
		endereco.setBairro(jsonEnd.getString("bairro"));
		endereco.setComplemento(jsonEnd.getString("complemento"));
		endereco.setCep(jsonEnd.getString("cep"));
		endereco.setEstado(jsonEnd.getString("estado"));
		
		Usuario usuario = new Usuario(json.getString("nome"), json.getString("email"), json.getString("senha"), endereco);
		
		byte[] foto = Base64.decodeBase64(json.getString("foto"));
		usuario.setFoto(foto);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			usuario.setDataNascimento(df.parse(json.getString("dataNascimento")));
		} catch (ParseException e) {
			e.printStackTrace();
			usuario.setDataNascimento(new Date());
		}finally{
			usuario.salvar();
		}
	}

}
