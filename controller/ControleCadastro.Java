/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import view.Cadastro;
import DAO.Conexao;
import DAO.LoginDAO;
import model.Login1;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author isaso
 */

public class ControleCadastro {
    private Cadastro tela3;
    
    public ControleCadastro(Cadastro tela3){
        this.tela3 = tela3;
    }
    
    public void salvarCliente(){
        String nome = tela3.getTxtNome().getText();
        String usuario = tela3.getTxtUsuario().getText();
        String senha = tela3.getTxtSenha().getText();
        Login1 login1 = new Login1(nome, usuario, senha);
        
        Conexao conexao = new Conexao();
        try {
            Connection conn = conexao.getConnection();
            LoginDAO dao = new LoginDAO(conn);
            dao.inserir(login1);
            JOptionPane.showMessageDialog(tela3, "Usuario Cadastrado!","Aviso", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela3, "Usuário não cadastrado!","Erro", 
                                        JOptionPane.ERROR_MESSAGE);
        }
    }
}