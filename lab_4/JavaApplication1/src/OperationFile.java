
import java.util.LinkedList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class OperationFile {
    private String header_line = "file_with_table_data\n";
    private String header_line_without_space = "file_with_table_data";
    private String file_absolute_path = "";
    private boolean flag_bin_file = false;
    private void parsing_text_line(String Line, double _max_x, double _min_x, double _step, double _integral) {
        
    }
    public OperationFile(String _file_absolute_path){file_absolute_path = _file_absolute_path;}
    
    public LinkedList <RecIntegral> get_table_data_from_text_file() throws OpenSaveFileException {
        LinkedList <RecIntegral> LinkedListRecIntegral;
        ParsingDataString this_ParsingDataString;
        File this_file = new File(file_absolute_path);
        FileWriter this_file_writer;
        boolean result_creating = false;
        String line = "";
        double this_max_x;
        double this_min_x;
        double this_step;
        double this_integral;
        try {
            if (!this_file.isFile()) {throw new OpenSaveFileException("OPERATIONFILE.get_table_data_from_text_file-> ", file_absolute_path, " # Файл не существует #");}
            FileReader this_FileReader = new FileReader(this_file);
            BufferedReader this_BufferedReader = new BufferedReader(this_FileReader);
            line = this_BufferedReader.readLine();
            if (line == null || line.isEmpty()) {throw new OpenSaveFileException("OPERATIONFILE.get_table_data_from_text_file-> ",file_absolute_path, " # Данный файл пуст #");}
            if (!line.equals(header_line_without_space)) {throw new OpenSaveFileException("OPERATIONFILE.get_table_data_from_text_file-> ",file_absolute_path, " # Данный файл не является файлом, сохраненным через это приложение #");}
            LinkedListRecIntegral = new LinkedList <RecIntegral> ();
            while (true) {
                line = this_BufferedReader.readLine();
                if (line == null) {break;}
                this_ParsingDataString = new ParsingDataString(line);
                int result = this_ParsingDataString.calculating_DataRowValue_from_textString();
                if (result == 1) {throw new OpenSaveFileException("PASINGDATASTRING.calculating_DataRowValue_from_textString-> ", file_absolute_path, " # В файле найдена некоректная строка с данными #");}
                this_max_x = this_ParsingDataString.get_max_x();
                this_min_x = this_ParsingDataString.get_min_x();
                this_step = this_ParsingDataString.get_step();
                this_integral = this_ParsingDataString.get_integral();
                RecIntegral temp_RecIntegral = new RecIntegral(this_max_x, this_min_x, this_step, this_integral);
                LinkedListRecIntegral.add(temp_RecIntegral);
            }
        }
        catch (IOException exc) {
            throw new OpenSaveFileException("OPERATIONFILE.get_table_data_from_text_file-> ", file_absolute_path, " # Возникла ошибка непосредсвенно связанная с FileReader #");
        }
        return LinkedListRecIntegral;
    }
    public LinkedList <RecIntegral> get_table_data_from_bin_file() throws OpenSaveFileException {
        LinkedList <RecIntegral> LinkedListRecIntegral;
        LinkedListRecIntegral = new LinkedList <RecIntegral> ();
        try {
            ObjectInputStream this_ObjectInputStream = new ObjectInputStream(new FileInputStream(file_absolute_path));
            String prev_line = this_ObjectInputStream.readUTF();
            if (!prev_line.equals(header_line)) {throw new OpenSaveFileException("OPERATIONFILE.get_table_data_from_bin_file-> ", file_absolute_path, " # Данный файл не является файлом, сохраненным через это приложение # ");}
            int version = this_ObjectInputStream.readInt();
            int size = this_ObjectInputStream.readInt();
            for (int i = 0; i < size ; i++) {
                RecIntegral obj_RecIntegral = (RecIntegral) this_ObjectInputStream.readObject();
                LinkedListRecIntegral.add(obj_RecIntegral);
            }
        }
        catch (ClassNotFoundException e) {
            throw new OpenSaveFileException("get_table_data_from_bin_file-> ",file_absolute_path, " # Класс RecIntegral не найден! # ");
        }
        catch (IOException exc) {
            throw new OpenSaveFileException("get_table_data_from_bin_file-> ", file_absolute_path, " # Ошибка, связанная с работой FileInputStream #");
        }
        return LinkedListRecIntegral;
    }
    public void write_table_data_in_text_file(LinkedList <RecIntegral> LinkedListRecIntegral) throws OpenSaveFileException {
        File this_file = new File(file_absolute_path);
        FileWriter this_file_writer = null;
        boolean result_creating = false;
        String out_string = "";
        double this_max_x;
        double this_min_x;
        double this_step;
        double this_integral;
        try {
            result_creating = this_file.createNewFile();
            if (result_creating == false) {throw new OpenSaveFileException("OPERATIONFILE.write_table_data_in_text_file-> ",file_absolute_path," # Ошибка создания нового файла #");}
            this_file_writer = new FileWriter(this_file);
            this_file_writer.write(header_line);
            for (int i = 0;  i < LinkedListRecIntegral.size(); i++) {
                this_max_x = LinkedListRecIntegral.get(i).get_max_x();
                this_min_x = LinkedListRecIntegral.get(i).get_min_x();
                this_step = LinkedListRecIntegral.get(i).get_step();
                this_integral = LinkedListRecIntegral.get(i).get_integral();
                out_string = Double.toString(this_max_x) + "\\" + Double.toString(this_min_x) + "\\" + Double.toString(this_step) + "\\" + Double.toString(this_integral) + "\n";
                this_file_writer.write(out_string);
            }
            this_file_writer.flush();
        }
        catch (IOException exc){
            throw new OpenSaveFileException("OPERATIONFILE.write_table_data_in_text_file-> ", file_absolute_path, "# Ошибка, связанная с работой FileWriter #");
        }
        finally {
            if (this_file_writer != null) {
            try {
                this_file_writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка закрытия файла: " + e.getMessage());
            }
        }
        }   
        return;
    }
    public void write_table_data_in_bin_file(LinkedList <RecIntegral> LinkedListRecIntegral) throws OpenSaveFileException {
        try {
            ObjectOutputStream obj_ObjectOutputStream = new ObjectOutputStream(new FileOutputStream(file_absolute_path));
            obj_ObjectOutputStream.writeUTF(header_line);
            obj_ObjectOutputStream.writeInt(1);
            obj_ObjectOutputStream.writeInt(LinkedListRecIntegral.size());
            for (int i = 0; i < LinkedListRecIntegral.size(); i++) {
                obj_ObjectOutputStream.writeObject(LinkedListRecIntegral.get(i));
            }
        }
        catch (IOException exc) {
            throw new OpenSaveFileException ("write_table_data_in_bin_file-> ", file_absolute_path, " # Возникла проблема, связанная с FileOutputStream #");
        }
//        catch (ClassNotFoundException exc) {
//            throw new OpenSaveFileException("OPERATIONFILE.write_table_data_in_text_file-> ", file_absolute_path, "# Класс RecIntegral не был найден при десериализации #");
//        }
        return;
    }
}
