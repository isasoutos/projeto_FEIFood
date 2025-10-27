/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.AlimentoDAO;
import DAO.Conexao;
import view.TelaInicial;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;

/**
 *
 * @author isaso
 */
public class ControleBusca {
    private TelaInicial tela2;

    public ControleBusca(TelaInicial tela2) {
        this.tela2 = tela2;
    }
    
    public void buscaEExibir() {
        String termoBusca = tela2.getTxtBuscarPedido().getText();
        
        if (termoBusca.trim().isEmpty()) {
             JOptionPane.showMessageDialog(tela2, "Digite o nome do produto para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
             return;
        }
        
        Connection conn = null;
        DefaultListModel<String> model = new DefaultListModel<>();

        try {
            conn = new Conexao().getConnection();

            if (conn == null) { 
                JOptionPane.showMessageDialog(tela2, "Falha crítica na conexão.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            AlimentoDAO dao = new AlimentoDAO(conn); 

            ResultSet rs = dao.buscarProduto(termoBusca);
            
            if (!rs.isBeforeFirst() && !rs.next()) {

                model.addElement("Nenhum produto encontrado.");
            } else {
                rs.beforeFirst(); 
                
                while (rs.next()) {
                    String nomeProd = rs.getString("produto_nome");
                    String preco = String.format("R$ %.2f", rs.getDouble("preco"));
                    String estab = rs.getString("estabelecimento_nome");
                    
                    String linha = String.format("%s | Preço: %s | Loja: %s", nomeProd, preco, estab);
                    model.addElement(linha);
                }
            }
            
            tela2.getListBusca().setModel(model); 
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(tela2, "Erro SQL: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }
}