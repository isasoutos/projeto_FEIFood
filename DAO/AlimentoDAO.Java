/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author isaso
 */
public class AlimentoDAO {
    private Connection conexao;

    public AlimentoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public ResultSet buscarProduto(String nomeProduto) throws SQLException {
        String sql = "SELECT p.nome AS produto_nome, p.preco, e.nome AS estabelecimento_nome " +
                     "FROM produto p " +
                     "JOIN estabelecimento e ON p.id_estabelecimento = e.id_estabelecimento " +  
                     "WHERE p.nome ILIKE ?";
        
        PreparedStatement statement = conexao.prepareStatement(
        sql, 
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY      
    );
    
        statement.setString(1, "%" + nomeProduto + "%"); 
    
        statement.execute();
        return statement.getResultSet();
    }
}