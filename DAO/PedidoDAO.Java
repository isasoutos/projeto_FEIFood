/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author isaso
 */

public class PedidoDAO {
    private Connection conexao; 

    public PedidoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public int inserirPedido(int idCliente, int idEstabelecimento, double valorFinal) throws SQLException {
        String sql = "INSERT INTO pedido (id_cliente, id_estabelecimento, valor_final) " +
                     "VALUES (?, ?, ?) RETURNING id_pedido"; 
        
        int idPedido = -1;

        try (PreparedStatement statement = conexao.prepareStatement(sql)) { 
            statement.setInt(1, idCliente); 
            statement.setInt(2, idEstabelecimento); 
            statement.setDouble(3, valorFinal);
            statement.execute();

            try (ResultSet rs = statement.getResultSet()) {
                if (rs.next()) {
                    idPedido = rs.getInt("id_pedido"); 
                }
            }
        }
        
        if (idPedido == -1) {
             throw new SQLException("Falha ao obter ID do pedido após a inserção.");
        }
        return idPedido;
    }

    public void inserirItemPedido(int idPedido, int idProduto, int quantidade, 
                                  double precoUnitario) throws SQLException {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, "
                + "quantidade, preco_unitario) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setInt(1, idPedido);
            statement.setInt(2, idProduto);
            statement.setInt(3, quantidade);
            statement.setDouble(4, precoUnitario);
            
            statement.execute(); 
        }
    }
}