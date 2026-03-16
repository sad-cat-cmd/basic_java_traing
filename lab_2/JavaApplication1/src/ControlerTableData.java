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

public class ControlerTableData {
    private DefaultTableModel table_model;
    private LinkedList <RecIntegral> LinkedListRecIntegral;
    
    public ControlerTableData(DefaultTableModel _table_model){
        table_model = _table_model;
        LinkedListRecIntegral = new LinkedList <RecIntegral> ();
    }
    public void remove_last(){
        if (LinkedListRecIntegral.size() > 0) {
            table_model.removeRow(LinkedListRecIntegral.size() - 1);
            LinkedListRecIntegral.removeLast();
        }
        return;
    }
    public void show_all_data_rows(){
        table_model.setRowCount(0);
        for (int i = 0; i < LinkedListRecIntegral.size(); i++) {
            double local_max_x = LinkedListRecIntegral.get(i).get_max_x();
            double local_min_x = LinkedListRecIntegral.get(i).get_min_x();
            double local_step = LinkedListRecIntegral.get(i).get_step();
            double local_integral = LinkedListRecIntegral.get(i).get_integral();
            table_model.addRow(new Object []{i + 1,local_max_x,local_min_x,local_step,local_integral});
            table_model.fireTableDataChanged();
        }
        table_model.fireTableDataChanged();
        return;
    }
    public void clear_table_data() {
        table_model.setRowCount(0);
        return;
    }
    public void show_last_integral(){
        double local_integral = LinkedListRecIntegral.getLast().get_integral();
        table_model.setValueAt(local_integral, LinkedListRecIntegral.size()-1,4);
    }
    public void push_back_input_data(double max_x, double min_x, double step){
        RecIntegral obj_RecIntegral = new RecIntegral(max_x, min_x, step);
        LinkedListRecIntegral.add(obj_RecIntegral);
        table_model.addRow(new Object []{LinkedListRecIntegral.size(),max_x,min_x,step, null});
    }
    
}
