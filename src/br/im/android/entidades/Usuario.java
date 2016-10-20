package br.im.android.entidades;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import br.im.android.banco.UsuarioDB;
import br.im.android.exceptions.ConexaoException;

public class Usuario {
	
	private long id;
	private String nome;
	private byte[] foto;
	private String senha;
	private String email;
	private Date dataNascimento;
	private Endereco endereco;
	
	public Usuario(String nome, String email, String senha, Endereco endereco) {
		this.nome = nome;
		this.senha = senha;
		this.email = email;
		this.endereco = endereco;
	}
	
	public String toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("nome", this.nome);
        json.put("senha", this.senha);
        json.put("email", this.email);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
        json.put("dataNascimento", sdf.format(this.dataNascimento));
        json.put("endereco", this.endereco.toJson());
        
        String base64String = Base64.encodeBase64String(this.foto);
        
        json.put("foto", base64String);
        
        System.out.println(base64String);

        return json.toString();
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void salvar() throws ClassNotFoundException, ConexaoException, SQLException {
		this.endereco.salvar();
		UsuarioDB db = new UsuarioDB();
		db.salvar(this);
	}
	
	public static Usuario logar(String email, String senha) throws ClassNotFoundException, ConexaoException, SQLException {
		UsuarioDB db = new UsuarioDB();
		return db.logar(email, senha);
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getNome() {
		return nome;
	}

	public String getSenha() {
		return senha;
	}

	public String getEmail() {
		return email;
	}

}
