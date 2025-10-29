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
public class EstabelecimentoDAO {
    private Connection conexao;

    public EstabelecimentoDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    public int buscarIdPorNome(String nomeEstabelecimento) throws SQLException {
        String sql = "SELECT id_estabelecimento "
                + "FROM estabelecimento "
                + "WHERE nome ILIKE ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nomeEstabelecimento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_estabelecimento");
                }
            }
        }
        return -1;
    }
}