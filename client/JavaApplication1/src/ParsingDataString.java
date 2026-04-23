/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
public class ParsingDataString {
    private double max_x;
    private double min_x;
    private double step;
    private double integral;
    String str_for_parsing;
    public ParsingDataString(String _str_for_parsing) {str_for_parsing = _str_for_parsing; }
    public int calculating_DataRowValue_from_textString () {
        String [] parts = str_for_parsing.split("\\\\");
        if (parts.length == 4) {
            max_x = Double.parseDouble(parts[0]);
            min_x = Double.parseDouble(parts[1]);
            step = Double.parseDouble(parts[2]);
            integral = Double.parseDouble(parts[3]);
        }
        else {return 1;}
        return 0;
    }
    public double get_max_x(){return max_x;}
    public double get_min_x(){return min_x;}
    public double get_step(){return step;}
    public double get_integral(){return integral;}
}
