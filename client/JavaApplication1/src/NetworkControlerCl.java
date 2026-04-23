/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.net.*;
/**
 *
 * @author admin_
 */
public class NetworkControlerCl {
    private PrintWriter printWriter;
    private BufferedReader bufferReader;
    private String strInputMatchingValue;
    private boolean flagConnection = false;
    private InetAddress addr;
    private Socket socket;
    private int port = 8080;
    private double inputMathingValue[];
    
    private void getInputMathingValue() {
        String parts [] = strInputMatchingValue.split("\\\\");
        for (int i = 0; i < parts.length; i++) {
            inputMathingValue[i] = Double.parseDouble(parts[i]);
        }
    }
    
    public NetworkControlerCl() throws ClientException{
        inputMathingValue = new double [3];
        try{
            addr = InetAddress.getByName(null); 
        }
        catch (UnknownHostException exc){
            System.out.println("Ошибка: InetAddress.getByName(null)");
            throw new ClientException("NetworkControler.NetworkControler()", exc.getMessage());
        }
        try {
            socket = new Socket(addr, port);
            System.out.println("Клиент подключен к серверу");
        }
        catch (IOException exc) {
            System.out.println("Ошибка : Socket(addr, port)");
            throw new ClientException("NetworkControler.NetworkControler()", "Error Socket Install");
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            strInputMatchingValue = in.toString();
            CalculatingWorker newCalculatingWorker = new CalculatingWorker(inputMathingValue[0], inputMathingValue[1], inputMathingValue[2]);
            newCalculatingWorker.operation();
            newCalculatingWorker.getResult();
        }
        catch (IOException exc) {
            System.out.println("Ошибка: При получении буфера");
            throw new ClientException("NetworkControler.NetworkControler()", exc.getMessage());
        }
        
        
    }
    
    
}
