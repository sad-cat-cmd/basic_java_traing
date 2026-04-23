/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */

public class MathThread extends Thread {
    private double start_unacceptability_value = Math.PI / 2;
    private double step_unacceptability_period = Math.PI;
    private static double EPSILON = 1e-10;
    private double max_x = 0;
    private double min_x = 0;
    private double step = 0;
    private double result;
    private double next_x_position = 0;
    // возвращает точный необходимый шаг
    
    private double checking_interval_for_unacceptability(double this_x, double this_step) {
        double true_next_x = this_x + this_step;
        double temp_start_unacceptability_value = start_unacceptability_value;
        while (true) {
            if (temp_start_unacceptability_value > true_next_x){
                break;
            }
            if (temp_start_unacceptability_value < true_next_x && temp_start_unacceptability_value > this_x) {
                next_x_position = temp_start_unacceptability_value + 0.01;
                return this_step - (true_next_x - temp_start_unacceptability_value) - 0.001;
            }
            temp_start_unacceptability_value += step_unacceptability_period;
        }
        while (true) {
            if (temp_start_unacceptability_value < this_x){
                break;
            }
            if (temp_start_unacceptability_value < true_next_x && temp_start_unacceptability_value > this_x) {
                next_x_position = temp_start_unacceptability_value + 0.01;
                return this_step - (true_next_x - temp_start_unacceptability_value) - 0.001;
            }
            temp_start_unacceptability_value -= step_unacceptability_period;
        }
        return this_step;
    }  
    private double get_true_step(double this_x, double this_step){
        double this_part_area = 0;
        double temp_step = this_step;
        next_x_position = this_x + temp_step;
        if (this_x + this_step > max_x) {
            temp_step = max_x - this_x;
            next_x_position = max_x;
        }
        if (isUnacceptablePoint(this_x + temp_step)){
            next_x_position = temp_step + this_x + 0.001;
            temp_step = temp_step - 0.001;
        }
        temp_step = checking_interval_for_unacceptability(this_x, temp_step);
        return temp_step;
    }
    private double mathing_part_area(double this_x, double this_step){
        double this_x_plus_step = this_x + this_step;
        double funtion_at_this_x = Math.sin(this_x)/Math.cos(this_x);
        double function_at_this_x_plus_step = Math.sin(this_x_plus_step)/Math.cos(this_x_plus_step);
        return this_step * (funtion_at_this_x + function_at_this_x_plus_step) / 2;
    }
    private boolean isUnacceptablePoint(double x) {
        double k = (x - Math.PI/2) / Math.PI;
        double nearestInteger = Math.round(k);
        return Math.abs(k - nearestInteger) < EPSILON;
    }
    private double calculation_result(){
        if (isUnacceptablePoint(min_x)) {
            min_x += 0.001;
        }
        // проверка min_x и max_x на некоректное значение
        double this_x_position = min_x;
        double local_step = 0;
        double result_area = 0;
        
        while (true){
            if (this_x_position == max_x ){
                break;
            }
            local_step = get_true_step(this_x_position, step);
            result_area += mathing_part_area(this_x_position, local_step);
            this_x_position = next_x_position;
        }
        return result_area;
    }
    
    public MathThread(double _min_x, double _max_x, double _step) {
        max_x = _max_x;
        min_x = _min_x;
        step = _step;
    }
    public double get_result(){
        result = calculation_result();
        return result;
    }
    @Override
    public void run() {
        calculation_result();
    }
}
