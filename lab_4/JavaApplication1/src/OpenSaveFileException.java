/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */

public class OpenSaveFileException extends Exception {
    private String file_path_exc = "";
    private String name_function_with_exc = "";
    private String msg_decision = "";
    
    public OpenSaveFileException (String _funcname_with_exc, String _fpath_error, String _msg_decision) {
        file_path_exc = _fpath_error;
        name_function_with_exc = _funcname_with_exc;
        msg_decision = _msg_decision;
    }
    public String get_file_path_exc() {return file_path_exc;}
    public String get_name_function_with_exc() {return name_function_with_exc;}
    public String get_msg_decision () {return msg_decision;}
}
