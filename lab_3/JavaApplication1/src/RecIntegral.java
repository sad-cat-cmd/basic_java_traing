/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */

public class RecIntegral {
    private double max_x = 0.0;
    private double min_x = 0.0;
    private double step = 0.0;
    private double integral = 0.0;
    boolean flag_prev_mathing = false;
    
    public RecIntegral(double _max_x, double _min_x, double _step) {
        max_x = _max_x;
        min_x = _min_x;
        step = _step;
    }
    public double get_integral() {
        if (flag_prev_mathing == false) {
            MathFunction obj_MathFunction = new MathFunction(min_x, max_x, step);
            integral = obj_MathFunction.get_result();
            flag_prev_mathing = true;
        }
        return integral;
    }
    public double get_max_x(){return max_x;}
    public double get_min_x(){return min_x;}
    public double get_step(){return step;}
}
