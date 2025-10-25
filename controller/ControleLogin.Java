/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.LoginDAO;
import DAO.Conexao;
import model.Login1;
import view.Login;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import view.TelaInicial;


/**
 *
 * @author unicisouza
 */
public class ControleLogin {
    private Login tela1, tela2;

    public ControleLogin(Login tela1) {
        this.tela1 = tela1;
    }
    
    public void loginCliente(){
        Login1 login1 = new Login1(null, 
                                tela1.getTxtUsuario().getText(),
                                tela1.getTxtSenha().getText());
        Conexao conexao = new Conexao();
        try{
            Connection conn = conexao.getConnection();
            LoginDAO dao = new LoginDAO(conn);
            ResultSet res = dao.consultar(login1);
            if(res.next()){
                JOptionPane.showMessageDialog(tela1,
                                            "Login Efetuado",
                                            "Sucesso",
                                            JOptionPane.INFORMATION_MESSAGE);
                TelaInicial tela2 = new TelaInicial();
                tela2.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(tela1,
                                            "Login NAO Efetuado",
                                            "Falha",
                                            JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e){
        e.printStackTrace(); // <--- ISTO É VITAL, APARECE NO CONSOLE DO NETBEANS
        
        JOptionPane.showMessageDialog(tela1,
                                     "ERRO REAL: " + e.getMessage(), // <-- ISTO MOSTRA NO POP-UP
                                     "Falha",
                                     JOptionPane.ERROR_MESSAGE);
    }

    }
}