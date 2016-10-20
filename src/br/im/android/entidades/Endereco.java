package br.im.android.entidades;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import br.im.android.banco.EnderecoDB;
import br.im.android.exceptions.ConexaoException;

public class Endereco {
	
	private long id;
	private String endereco;
	private String numero;
	private String cep;
	private String estado;
	private String cidade;
	private String bairro;
	private String complemento;
	
	public Endereco(String endereco, String numero, String cidade) {
		this.endereco = endereco;
		this.numero = numero;
		this.cidade = cidade;
	}
	
	public Endereco(int id, String endereco, String numero, String cidade) {
		this.id = id;
		this.endereco = endereco;
		this.numero = numero;
		this.cidade = cidade;
	}
	
	public String toJson() throws JSONException {
		JSONObject json = new JSONObject();

        json.put("endereco", this.endereco);
        json.put("numero", this.numero);
        json.put("cidade", this.cidade);
        json.put("cep", this.cep);
        json.put("estado", this.estado);
        json.put("bairro", this.bairro);
        json.put("complemento", this.complemento);

        return json.toString();
	}
	
	public void salvar() throws ClassNotFoundException, ConexaoException, SQLException {
		EnderecoDB db = new EnderecoDB();
		db.salvar(this);
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getEndereco() {
		return endereco;
	}

	public String getNumero() {
		return numero;
	}

	public String getCidade() {
		return cidade;
	}
	
	public long getId() {
		return id;
	}

}
