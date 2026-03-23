/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
public class InputValueException extends Exception {
    private String exc_message = "";
    private double ex_double_value;
    public InputValueException (String ex_mes, double ex_val) {
        exc_message = ex_mes;
        ex_double_value = ex_val;
    }
    public String get_exc_message () {return exc_message;}
    public double get_ex_value() {return ex_double_value;}
}
