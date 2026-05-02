/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import java.io.File;

public class ControlerTableData {
    private String basic_path_dir = "/home/admin_/Документы";
    private DefaultTableModel table_model;
    private LinkedList <RecIntegral> LinkedListRecIntegral;
  
    public ControlerTableData(DefaultTableModel _table_model){
        table_model = _table_model;
        LinkedListRecIntegral = new LinkedList <RecIntegral> ();
    }
    public void show_last_integral(){
        double local_integral = LinkedListRecIntegral.getLast().get_integral();
        table_model.setValueAt(local_integral, LinkedListRecIntegral.size()-1,4);
    }
    public void push_back_input_data(double max_x, double min_x, double step, double integral){
        RecIntegral obj_RecIntegral = new RecIntegral(max_x, min_x, step, integral);
        LinkedListRecIntegral.add(obj_RecIntegral);
        table_model.insertRow(LinkedListRecIntegral.size() - 1, new Object []{LinkedListRecIntegral.size(),max_x,min_x,step, integral});
    }
}
