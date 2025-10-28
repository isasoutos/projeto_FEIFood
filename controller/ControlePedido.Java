/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.Conexao;
import DAO.PedidoDAO;
import DAO.AlimentoDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import model.ItemPedido;
import view.CadastroDePedido;

/**
 *
 * @author isaso
 */
public class ControlePedido {
    private CadastroDePedido tela4;
    private List<ItemPedido> itensDoPedido = new ArrayList<>();
    private DefaultListModel<String> listModel; 
    private final int ID_CLIENTE_FIXO = 1; 
    private final int ID_ESTABELECIMENTO_FIXO = 1; 

    public ControlePedido(CadastroDePedido tela4) {
        this.tela4 = tela4;
        this.listModel = new DefaultListModel<>(); 
        tela4.getListPedido().setModel(this.listModel); 
    }

    public double calcularTotal() {
        return itensDoPedido.stream().
                mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public void adicionarItem() {
        String nomeProduto = tela4.getTxtPedido().getText().trim();
        String qtdText = tela4.getTxtQuantidade().getText().trim();

        if (nomeProduto.isEmpty() || qtdText.isEmpty()) {
            JOptionPane.showMessageDialog(tela4, 
                    "Preencha o Produto e a Quantidade.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantidade;
        try {
            quantidade = Integer.parseInt(qtdText);
            if (quantidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(tela4, "Quantidade inválida.", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            AlimentoDAO alimentoDAO = new AlimentoDAO(conn);
            ResultSet rs = alimentoDAO.buscarDetalheProduto(nomeProduto);
            
            if (rs.next()) {
                int idProduto = rs.getInt("id_produto");
                double precoBase = rs.getDouble("preco");
                String categoria = rs.getString("categoria");
                
                ItemPedido novoItem = new ItemPedido(idProduto, nomeProduto, 
                        precoBase, quantidade, categoria);
                
                itensDoPedido.add(novoItem);
                atualizarCarrinho();
                
                tela4.getTxtPedido().setText("");
                tela4.getTxtQuantidade().setText("");
                
            } else {
                JOptionPane.showMessageDialog(tela4, "Produto '" 
                        + nomeProduto + "' não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(tela4, "Erro SQL: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) { try { conn.close(); 
            } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    private void atualizarCarrinho() {
        listModel.clear();

        double totalGeral = calcularTotal(); 
    
        for (ItemPedido item : itensDoPedido) {
            String linha = String.format("%s (x%d) | Subtotal: R$ %.2f", 
                                      item.getNomeProduto(),
                                      item.getQuantidade(), 
                                      item.getPrecoUnitario(),
                                      item.getSubtotal());
            listModel.addElement(linha);
        }
        String totalFormatado = String.format("TOTAL GERAL: R$ %.2f",
                                totalGeral);

        tela4.getLblTotalGeral().setText(totalFormatado); 
    }

    public void solicitarPedido() {
        if (itensDoPedido.isEmpty()) {
            JOptionPane.showMessageDialog(tela4, 
                    "Adicione itens ao pedido antes de solicitar.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            conn.setAutoCommit(false); 
            PedidoDAO pedidoDAO = new PedidoDAO(conn);

            double totalGeral = calcularTotal();
            int idPedido = pedidoDAO.inserirPedido(ID_CLIENTE_FIXO, 
                    ID_ESTABELECIMENTO_FIXO, totalGeral);

            if (idPedido == -1) throw new SQLException
        ("Falha ao obter ID do pedido.");

            for (ItemPedido item : itensDoPedido) {
                pedidoDAO.inserirItemPedido(idPedido, item.getIdProduto(),
                        item.getQuantidade(), item.getPrecoUnitario());
            }

            conn.commit();
            JOptionPane.showMessageDialog(tela4, 
                    "Pedido entregue com sucesso!", "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            itensDoPedido.clear();
            atualizarCarrinho(); 

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); 
                } catch (SQLException rb) { rb.printStackTrace(); } 
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(tela4, 
                    "Erro no pedido: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) { try { conn.close(); 
            } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    public void excluirItem() {
        JList<String> listaItens = tela4.getListPedido();
    
        int indiceSelecionado = listaItens.getSelectedIndex();

        if (indiceSelecionado == -1) {
            JOptionPane.showMessageDialog(tela4, 
                    "Selecione um item no carrinho para excluir.", 
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int confirmacao = JOptionPane.showConfirmDialog(tela4, 
                "Tem certeza que deseja remover este item?", "Confirmação", 
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                itensDoPedido.remove(indiceSelecionado);

            atualizarCarrinho();
            
                JOptionPane.showMessageDialog(tela4, 
                        "Item removido com sucesso.", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(tela4, 
                        "Erro interno ao remover item. Tente novamente.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void excluirPedido() {
        if (itensDoPedido.isEmpty()) {
            JOptionPane.showMessageDialog(tela4, "O carrinho já está vazio.", 
                                        "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        if (JOptionPane.showConfirmDialog(tela4, 
                "Tem certeza que deseja limpar TODO o carrinho?", 
                "Confirmação Total", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
        
            itensDoPedido.clear();
            atualizarCarrinho();
            JOptionPane.showMessageDialog(tela4, "Carrinho limpo.", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}    