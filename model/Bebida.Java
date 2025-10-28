/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author isaso
 */

public class Bebida extends Alimento implements ImpostoAlcool {
    private boolean isAlcoolica;
    
    @Override
    public double calcularImposto() {
        if (isAlcoolica) {
            return getPreco() * TAXA_ALCOOL;
        }
        return 0.0;
    }

    @Override
    public double calcularPrecoFinal() {
        return getPreco() + calcularImposto(); 
    }
}