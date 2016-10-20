package br.im.android.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.im.android.entidades.Endereco;
import br.im.android.exceptions.ConexaoException;

public class EnderecoDB {
	
	public void salvar(Endereco endereco) throws ConexaoException, SQLException, ClassNotFoundException {
		
		Conexao.initConnection();
			
		String sql = "INSERT INTO endereco (endereco_endereco, endereco_numero, endereco_cep, endereco_estado, "
				+ "endereco_cidade, endereco_bairro, endereco_complemento) VALUES(?, ?, ?, ?, ?, ?, ?);";
		
		PreparedStatement pstmt = Conexao.prepare(sql);
		
		pstmt.setString(1, endereco.getEndereco());
		pstmt.setString(2, endereco.getNumero());
		pstmt.setString(3, endereco.getCep());
		pstmt.setString(4, endereco.getEstado());
		pstmt.setString(5, endereco.getCidade());
		pstmt.setString(6, endereco.getBairro());
		pstmt.setString(7, endereco.getComplemento());
		
		int linhasAfetadas = pstmt.executeUpdate();
		
    	if (linhasAfetadas == 0) {
    		Conexao.rollBack();
    		Conexao.closeConnection();
			throw new ConexaoException();
		} else {
			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					endereco.setId(generatedKeys.getLong(1));
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
}
