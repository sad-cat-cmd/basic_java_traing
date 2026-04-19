import java.io.File;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
public class CheckCorrecntessFileName {
    private String array_uncorrectness_chars [] = new String [] {"#", "!", "№", "@", "$", "%", "^", "&", "*", "(", ")", "-", "+", ":", ";", "=", "'", "/", "}",
                                                             "?", "{", ">", "<", "]", "[", "|", ",", "\\", "\"", "\n", "\t", " ", "\r", "\f", "\b", "`", "."};
    private String correctness_file_extension_for_text_file = ".txt";
    private String correctness_file_extension_for_bin_file = ".dat";
    private String this_extension = "";
    private String this_name_without_extension = "";
    private String absolute_path = "";
    private String fname = "";
    private String added_msg = "not";
    
    public CheckCorrecntessFileName (String _absolute_path, String _fname) {
        absolute_path = _absolute_path;
        fname = _fname;
    }
    public int check_correctness_fname_for_save_text_file() {
        if (fname.isEmpty() || fname == null) {return 1;}
        int last_dot_Index = fname.lastIndexOf('.');
        if (last_dot_Index == -1 || last_dot_Index == fname.length() - 1) {return 2;}
        this_extension = fname.substring(last_dot_Index);
        if (!this_extension.equals(correctness_file_extension_for_text_file)) {
            added_msg = this_extension;
            return 3;
        }
        this_name_without_extension = fname.substring(0, last_dot_Index);
        for (int i = 0; i < array_uncorrectness_chars.length; i++) {
            if (this_name_without_extension.contains(array_uncorrectness_chars[i])) {
                added_msg = array_uncorrectness_chars[i];
                return 4;
            }
        }
        File test_file = new File(absolute_path);
        if (test_file.exists()) {return 5;}
        return 0;
    }
    public int check_correctness_fname_for_save_bin_file() {
        if (fname.isEmpty() || fname == null) {return 1;}
        int last_dot_Index = fname.lastIndexOf('.');
        if (last_dot_Index == -1 || last_dot_Index == fname.length() - 1) {return 2;}
        this_extension = fname.substring(last_dot_Index);
        if (!this_extension.equals(correctness_file_extension_for_bin_file)) {
            added_msg = this_extension;
            return 3;
        }
        this_name_without_extension = fname.substring(0, last_dot_Index);
        for (int i = 0; i < array_uncorrectness_chars.length; i++) {
            if (this_name_without_extension.contains(array_uncorrectness_chars[i])) {
                added_msg = array_uncorrectness_chars[i];
                return 4;
            }
        }
        File test_file = new File(absolute_path);
        if (test_file.exists()) {return 5;}
        return 0;
    }
    public int check_correctness_fname_for_download_from_text_file() {
        if (fname.isEmpty() || fname == null) {return 1;}
        int last_dot_Index = fname.lastIndexOf('.');
        if (last_dot_Index == -1 || last_dot_Index == fname.length() - 1) {return 2;}
        this_extension = fname.substring(last_dot_Index);
        if (!this_extension.equals(correctness_file_extension_for_text_file)) {
            added_msg = this_extension;
            return 3;
        }
        this_name_without_extension = fname.substring(0, last_dot_Index);
        for (int i = 0; i < array_uncorrectness_chars.length; i++) {
            if (this_name_without_extension.contains(array_uncorrectness_chars[i])) {
                added_msg = array_uncorrectness_chars[i];
                return 4;
            }
        }
        File test_file = new File(absolute_path);
        if (!test_file.exists()) {return 5;}
        return 0;
    }
    public int check_correctness_fname_for_download_from_bin_file() {
        if (fname.isEmpty() || fname == null) {return 1;}
        int last_dot_Index = fname.lastIndexOf('.');
        if (last_dot_Index == -1 || last_dot_Index == fname.length() - 1) {return 2;}
        this_extension = fname.substring(last_dot_Index);
        if (!this_extension.equals(correctness_file_extension_for_bin_file)) {
            added_msg = this_extension;
            return 3;
        }
        this_name_without_extension = fname.substring(0, last_dot_Index);
        for (int i = 0; i < array_uncorrectness_chars.length; i++) {
            if (this_name_without_extension.contains(array_uncorrectness_chars[i])) {
                added_msg = array_uncorrectness_chars[i];
                return 4;
            }
        }
        File test_file = new File(absolute_path);
        if (!test_file.exists()) {return 5;}
        return 0;
    }
    public String get_added_msg () {return added_msg;}
    
}
