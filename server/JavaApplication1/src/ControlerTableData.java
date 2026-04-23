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
    private NetworkControlerServ networkController;
    private String basic_path_dir = "/home/admin_/Документы";
    private DefaultTableModel table_model;
    private LinkedList <RecIntegral> LinkedListRecIntegral;
    private long lastTimeIntervalNs;
    private long lastTimeIntervalMs;
    
    private int checkCorrectnesInterval(double minX, double maxX) {
        double unacceptabilityPeriadValue = Math.PI / 2;
        double periodFunc = Math.PI;
        while (true) {
            if (unacceptabilityPeriadValue > minX && unacceptabilityPeriadValue < maxX ) {return 1;}
            if (unacceptabilityPeriadValue > minX && unacceptabilityPeriadValue > maxX ) {return 0;}
            unacceptabilityPeriadValue += periodFunc;
        }
    }
//    private int check_correctness_fname_for_save_bin_file(String f_name, String local_absolute_f_path, String added_msg) {
//        return 0;
//    }
    public ControlerTableData(DefaultTableModel _table_model) throws ServerException{
        table_model = _table_model;
        LinkedListRecIntegral = new LinkedList <RecIntegral> ();
        networkController = new NetworkControlerServ();
        try {
            networkController.startServer();
        }
        catch (ServerException exc) {
            throw exc;
        }
    }
    public long getLastTimeIntervalNs() {return lastTimeIntervalNs;}
    public long getLastTimeIntervalMs() {return lastTimeIntervalMs;}
    public void remove_last(){
        if (LinkedListRecIntegral.size() > 0) {
            table_model.removeRow(LinkedListRecIntegral.size() - 1);
            LinkedListRecIntegral.removeLast();
        }
        return;
    }
    public void remove_row_at_index(int index_Row) {
        table_model.removeRow(index_Row);
        LinkedListRecIntegral.remove(index_Row);
    }
    
    public boolean check_invalid_value (double value_double) {
        if (value_double < 0.000001 || value_double > 1000000.0) {return true;}
        return false;
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
    public void show_integral_at_index(int indexRow){
        //TimeController newTimeControler = new TimeController();
        double local_integral = LinkedListRecIntegral.get(indexRow).get_integral();
        table_model.setValueAt(local_integral, indexRow, 4);
        lastTimeIntervalNs = LinkedListRecIntegral.get(indexRow).getTimeCalculating();
        //lastTimeIntervalNs = newTimeControler.getTimeIntervalNs();
        //lastTimeIntervalMs = newTimeControler.getTimeIntervalMs();
    }
    public void push_back_input_data(double max_x, double min_x, double step) throws InputValueException{
        if (check_invalid_value(max_x)){throw new InputValueException("Uncorectable max_x value:" , max_x);}
        if (check_invalid_value(min_x)){throw new InputValueException("Uncorectable min_x value: ", min_x);}
        if (check_invalid_value(step)){throw new InputValueException("Uncorectable step value: ", step);}
        if (max_x < min_x) {throw new InputValueException("min_x > max_x, min_x: ", min_x);}
        if (step > max_x - min_x ) {throw new InputValueException("step > dif max_x and min_x, step: ", step);}
        if (checkCorrectnesInterval(min_x, max_x) == 1) {throw new InputValueException("interval include uncorecteble value", 0);}
        RecIntegral obj_RecIntegral = new RecIntegral(max_x, min_x, step);
        LinkedListRecIntegral.add(obj_RecIntegral);
        table_model.addRow(new Object []{LinkedListRecIntegral.size(),max_x,min_x,step, null});
    }
    // функция записи данных таблицы в текстовый файл
    public void write_table_data_in_text_file() throws OpenSaveFileException{
        if (LinkedListRecIntegral.size() == 0) {throw new OpenSaveFileException("write_table_data_in_text_file-> ", "Путь к файлу не задан. ", "Таблица пуста, нечего сохранять. ");}
        JFileChooser chooser_window = new JFileChooser(basic_path_dir);
        int result_operation = chooser_window.showSaveDialog(null);
        if (result_operation == JFileChooser.APPROVE_OPTION) {
            File selected_file = chooser_window.getSelectedFile();
            String absolute_path = selected_file.getAbsolutePath();
            String local_file_name = selected_file.getName();
            CheckCorrecntessFileName obj_CheckCorrecntessFileName = new CheckCorrecntessFileName(absolute_path, local_file_name);
            int result_correctness_fname = obj_CheckCorrecntessFileName.check_correctness_fname_for_save_text_file();
            String mes_added_info = obj_CheckCorrecntessFileName.get_added_msg();
            
            if (result_correctness_fname == 1) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Введенное вами имя файла является пустым. Пример имени файла : out.txt #");}
            if (result_correctness_fname == 2) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла отсуствует расширение. Пример имени файла : out.txt #");}
            if (result_correctness_fname == 3) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла расширение указано неправильно: " + mes_added_info + " вместо txt. Пример имени файла : out.txt #");}
            if (result_correctness_fname == 4) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла присутсвует запрещенный символ: " + mes_added_info + " . Пример имени файла: out.txt #");}
            if (result_correctness_fname == 5) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Имя с введенным вами именем уже существует. Попробуйте ввести другое. #");}
            
            OperationFile this_OperationFile = new OperationFile(absolute_path);
            try {
                this_OperationFile.write_table_data_in_text_file(LinkedListRecIntegral);
            }
            catch (OpenSaveFileException exc) {
                throw exc;
            }
        }
        return;
    }
    // функция записи данных таблицы в бинарный файл
    public void write_table_data_in_bin_file() throws OpenSaveFileException{
        if (LinkedListRecIntegral.size() == 0) {throw new OpenSaveFileException("write_table_data_in_text_file-> ", "Путь к файлу не задан. ", "Таблица пуста, нечего сохранять. ");}
        JFileChooser chooser_window = new JFileChooser(basic_path_dir);
        int result_operation = chooser_window.showSaveDialog(null);
        if (result_operation == JFileChooser.APPROVE_OPTION) {
            File selected_file = chooser_window.getSelectedFile();
            String absolute_path = selected_file.getAbsolutePath();
            String local_file_name = selected_file.getName();
            CheckCorrecntessFileName obj_CheckCorrecntessFileName = new CheckCorrecntessFileName(absolute_path, local_file_name);
            int result_correctness_fname = obj_CheckCorrecntessFileName.check_correctness_fname_for_save_bin_file();
            String mes_added_info = obj_CheckCorrecntessFileName.get_added_msg();
            
            if (result_correctness_fname == 1) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Введенное вами имя файла является пустым. Пример имени файла : out.dat #");}
            if (result_correctness_fname == 2) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла отсуствует расширение. Пример имени файла : out.dat #");}
            if (result_correctness_fname == 3) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла расширение указано неправильно: " + mes_added_info + " вместо .dat. Пример имени файла : out.dat #");}
            if (result_correctness_fname == 4) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла присутсвует запрещенный символ: " + mes_added_info + " . Пример имени файла: out.dat #");}
            if (result_correctness_fname == 5) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Имя с введенным вами именем уже существует. Попробуйте ввести другое. #");}
            
            OperationFile this_OperationFile = new OperationFile(absolute_path);
            try {
                this_OperationFile.write_table_data_in_bin_file(LinkedListRecIntegral);
            }
            catch (OpenSaveFileException exc) {
                throw exc;
            }
        }
        return;
    }
    // функция получения данных таблицы из текстового файла
    public void download_table_data_from_text_file() throws OpenSaveFileException{
        JFileChooser chooser_window = new JFileChooser(basic_path_dir);
        int result_operation = chooser_window.showOpenDialog(null);
        if (result_operation == JFileChooser.APPROVE_OPTION){
            File selected_file = chooser_window.getSelectedFile();
            String absolute_path = selected_file.getAbsolutePath();
            String local_file_name = selected_file.getName();
            CheckCorrecntessFileName obj_CheckCorrecntessFileName = new CheckCorrecntessFileName(absolute_path, local_file_name);
            int result_correctness_fname = obj_CheckCorrecntessFileName.check_correctness_fname_for_download_from_text_file();
            String mes_added_info = obj_CheckCorrecntessFileName.get_added_msg();
            
            if (result_correctness_fname == 1) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Введенное вами имя файла является пустым. Пример имени файла : out.txt #");}
            if (result_correctness_fname == 2) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла отсуствует расширение. Пример имени файла : out.txt #");}
            if (result_correctness_fname == 3) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла расширение указано неправильно: " + mes_added_info + " вместо txt. Пример имени файла : out.txt #");}
            if (result_correctness_fname == 4) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла присутсвует запрещенный символ: " + mes_added_info + " . Пример имени файла: out.txt #");}
            if (result_correctness_fname == 5) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Имя с введенным вами именем не существует. Попробуйте ввести другое. #");} 
            
            OperationFile this_OperationFile = new OperationFile(absolute_path);
            LinkedList <RecIntegral> local_LinkedList;
            try {
                local_LinkedList = this_OperationFile.get_table_data_from_text_file();
            }
            catch (OpenSaveFileException exc) {
                throw exc;
            }
            for (int i = 0; i < local_LinkedList.size(); i++ ) {
                LinkedListRecIntegral.add(local_LinkedList.get(i));
            }
            clear_table_data();
            show_all_data_rows();
        }
    }
    // функция получения данных таблицы из бинарного файла
    public void download_table_data_from_bin_file() throws OpenSaveFileException{
        JFileChooser chooser_window = new JFileChooser(basic_path_dir);
        int result_operation = chooser_window.showOpenDialog(null);
        if (result_operation == JFileChooser.APPROVE_OPTION){
            File selected_file = chooser_window.getSelectedFile();
            String absolute_path = selected_file.getAbsolutePath();
            String local_file_name = selected_file.getName();
            CheckCorrecntessFileName obj_CheckCorrecntessFileName = new CheckCorrecntessFileName(absolute_path, local_file_name);
            int result_correctness_fname = obj_CheckCorrecntessFileName.check_correctness_fname_for_download_from_bin_file();
            String mes_added_info = obj_CheckCorrecntessFileName.get_added_msg();
            
            if (result_correctness_fname == 1) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Введенное вами имя файла является пустым. Пример имени файла : out.dat #");}
            if (result_correctness_fname == 2) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла отсуствует расширение. Пример имени файла : out.dat #");}
            if (result_correctness_fname == 3) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла расширение указано неправильно: " + mes_added_info + " вместо .dat .Пример имени файла : out.dat #");}
            if (result_correctness_fname == 4) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # В веденном вами имени файла присутсвует запрещенный символ: " + mes_added_info + " . Пример имени файла: out.dat  #");}
            if (result_correctness_fname == 5) {throw new OpenSaveFileException("CHECKCORRECTNTESSFILENAME-> ", absolute_path, " # Имя с введенным вами именем не существует. Попробуйте ввести другое. #");} 
            
            LinkedList <RecIntegral> local_LinkedList;
            OperationFile this_OperationFile = new OperationFile(absolute_path);
            try {
                local_LinkedList = this_OperationFile.get_table_data_from_bin_file();
            }
            catch (OpenSaveFileException exc) {
                throw exc;
            }
            for (int i = 0; i < local_LinkedList.size(); i++ ) {
                LinkedListRecIntegral.add(local_LinkedList.get(i));
            }
            clear_table_data();
            show_all_data_rows();
        }
    }
}
