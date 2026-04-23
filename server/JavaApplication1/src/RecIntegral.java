/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
//import java.io.Serializable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.Externalizable;
        
public class RecIntegral implements Externalizable {
    private long timeCalculating;
    private long serialVersionUID = 1L;
    private double max_x = 0.0;
    private double min_x = 0.0;
    private double step = 0.0;
    private double integral = 0.0;
    
    boolean flag_prev_mathing = false;
    public RecIntegral(){}
    public RecIntegral(double _max_x, double _min_x, double _step) {
        max_x = _max_x;
        min_x = _min_x;
        step = _step;
    }
    public RecIntegral(double _max_x, double _min_x, double _step, double _integral){
        max_x = _max_x;
        min_x = _min_x;
        step = _step;
        integral = _integral;
        flag_prev_mathing = true;
    }
    public double get_integral() {
        if (flag_prev_mathing == false) {
            CalculatingWorker objCalcCalculatingWorker = new CalculatingWorker(min_x, max_x, step);
            integral = objCalcCalculatingWorker.operation();
            timeCalculating = objCalcCalculatingWorker.getTimeCalculating();
            flag_prev_mathing = true;
            //MathThread obj_MathFunction = new MathThread(min_x, max_x, step);
            //integral = obj_MathFunction.get_result();
        }
        return integral;
    }
    public long getTimeCalculating () {
        return timeCalculating;
    }
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(max_x);
        out.writeDouble(min_x);
        out.writeDouble(step);
        out.writeDouble(integral);
    }
    public void readExternal(ObjectInput in) throws IOException {
        max_x = in.readDouble();
        min_x = in.readDouble();
        step = in.readDouble();
        integral = in.readDouble();
    }
    public double get_max_x(){return max_x;}
    public double get_min_x(){return min_x;}
    public double get_step(){return step;}
    
}
