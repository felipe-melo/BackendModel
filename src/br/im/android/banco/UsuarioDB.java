package br.im.android.banco;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.im.android.entidades.Endereco;
import br.im.android.entidades.Usuario;
import br.im.android.exceptions.ConexaoException;

public class UsuarioDB {
	
	public void salvar(Usuario usuario) throws ConexaoException, SQLException, ClassNotFoundException {
		
		Conexao.initConnection();
			
		String sql = "INSERT INTO usuario (usuario_nome, usuario_foto, usuario_senha, usuario_email, usuario_data_nascimento, "
				+ "endereco_id) VALUES(?, ?, ?, ?, ?, ?);";
		
		PreparedStatement pstmt = Conexao.prepare(sql);
		
		pstmt.setString(1, usuario.getNome());
		pstmt.setBytes(2, usuario.getFoto());
		pstmt.setString(3, usuario.getSenha());
		pstmt.setString(4, usuario.getEmail());
		pstmt.setDate(5, new Date(usuario.getDataNascimento().getTime()));
		pstmt.setLong(6, usuario.getEndereco().getId());
		
		int linhasAfetadas = pstmt.executeUpdate();
		
    	if (linhasAfetadas == 0) {
    		Conexao.rollBack();
    		Conexao.closeConnection();
			throw new ConexaoException();
		}else{
			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					usuario.setId(generatedKeys.getLong(1));
					System.out.println("Salvou usuario");
					Conexao.commit();
					Conexao.closeConnection();
				} else {
					Conexao.rollBack();
					Conexao.closeConnection();
					throw new SQLException();
				}
			}
		}
	}
	
	public Usuario logar(String email, String senha) throws ConexaoException, SQLException, ClassNotFoundException {
		
		Conexao.initConnection();
			
		String sql = "SELECT * from usuario u INNER JOIN endereco WHERE u.usuario_email = ? and u.usuario_senha = ?;";
		
		PreparedStatement pstmt = Conexao.prepare(sql);
		
		pstmt.setString(1, email);
		pstmt.setString(2, senha);
		
		ResultSet result = pstmt.executeQuery();
		
    	if (!result.next()) {
    		Conexao.closeConnection();
			throw new ConexaoException();
		}else{
			String endereco = result.getString("endereco_endereco");
			String numero = result.getString("endereco_numero");
			String cidade = result.getString("endereco_cidade");
			String bairro = result.getString("endereco_bairro");
			String estado = result.getString("endereco_estado");
			String cep = result.getString("endereco_cep");
			String complemento = result.getString("endereco_complemento");
			
			Endereco usuEndereco = new Endereco(endereco, numero, cidade);
			usuEndereco.setBairro(bairro);
			usuEndereco.setCep(cep);
			usuEndereco.setComplemento(complemento);
			usuEndereco.setEstado(estado);
			
			String nome = result.getString("usuario_nome");
			String dataNascimento = result.getString("usuario_data_nascimento");
			Usuario usuario = new Usuario(nome, email, senha, usuEndereco);
			
			usuario.setFoto(result.getBytes("usuario_foto"));
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				usuario.setDataNascimento(df.parse(dataNascimento));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return usuario;
		}
	}
}
