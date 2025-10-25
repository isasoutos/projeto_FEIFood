/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.SQLException;
import java.sql.Connection;
import model.Login1;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
/**
 *
 * @author unicisouza
 */
public class LoginDAO {
    private Connection conexao;

    public LoginDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    public ResultSet consultar(Login1 login1) throws SQLException{
        String sql = "select * from login where usuario = ? and senha = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, login1.getUsuario());
        statement.setString(2, login1.getSenha());
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }
}