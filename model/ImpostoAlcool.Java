/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author isaso
 */

public class ItemPedido {
    
    private int idProduto, quantidade;   
    private String nomeProduto, categoria;
    private double precoUnitario;  
    
    private static final double TAXA_ALCOOL = 0.20; 

    public ItemPedido(int idProduto, String nomeProduto, double precoBase, int quantidade, String categoria) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.categoria = categoria;
        
        double imposto = calcularImposto(precoBase);
        this.precoUnitario = precoBase + imposto;
        this.precoUnitario = arredondar(this.precoUnitario);
    }

    private double calcularImposto(double precoBase) {
        if (this.categoria != null && this.categoria.toLowerCase().contains("bebida alcolica")) {
            return precoBase * TAXA_ALCOOL;
        }
        return 0.0;
    }
    
    private double arredondar(double valor) {
        BigDecimal bd = BigDecimal.valueOf(valor);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public double getSubtotal() {
        return arredondar(this.precoUnitario * this.quantidade);
    }
    
    public int getIdProduto() { return idProduto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public String getNomeProduto() { return nomeProduto; }
    public String getCategoria() { return categoria; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}