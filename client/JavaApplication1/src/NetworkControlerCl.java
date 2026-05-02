/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.*;
import java.net.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author admin_
 */
public class NetworkControlerCl implements AutoCloseable{
    private ControlerTableData controlerTableData;
    private PrintWriter out;
    private BufferedReader in;
    private InetAddress addr;
    private Socket socket;
    private int port = 8080;
    private double inputMathingValue[];
    private final Object lockObjectNetworkControler = new Object();
    private final Object lockCalculating = new Object();
    private String debugLine;
    
    private void getInputMathingValue(String strInLine) {
        String parts [] = strInLine.split("\\\\");
        for (int i = 0; i < parts.length; i++) {
            inputMathingValue[i] = Double.parseDouble(parts[i]);
        }
    }
    private String getOutStr(double value){
        return String.valueOf(value);
    }
    public int connection () {
        try {
            addr = InetAddress.getByName(null);
        }
        catch (UnknownHostException excUHE) {
            System.out.println("Произошла ошибка, при попытке подключиться к серверу: \n" +
                               "Дополнительные сведения: InetAddress.getByName(null) \n\t" +
                               excUHE.getMessage());
            return 1;
        }
        catch (SecurityException excSE) {
            System.out.println("Произошла ошибка, при попытке подключиться к серверу: \n" +
                               "Дополнительные сведения: InetAddress.getByName(null) \n\t" +
                               excSE.getMessage());
            return 1;
        }
        
        try {
            socket = new Socket (addr, port);
            socket.setSoTimeout(500);
        }
        catch (IOException excIOX) {
            System.out.println("Произошла ошибка, при попытке подключиться к серверу: \n" +
                               "Дополнительные сведения: Socket (addr, port) \n\t" +
                                excIOX.getMessage());
            return 2;
        }
        
        try {
            out = new PrintWriter(socket.getOutputStream());
        }
        catch (IOException excIO){
            System.out.println("В момент иницилизации out, произошла критическая ошибка");
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException excIO){
            System.out.println("В момент иницилизации in, произошла критическая ошибка ");
        }
        System.out.println("Сервер запущен");
        return 0;
    }
    public int operation () {
        System.out.println("Запущен цикл взаимодействия с сервером");
        String strInLine = "";
        String strOutLine = "";
        
        while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Поток прерван");
                    break;
                }
                try {
                    strInLine= in.readLine();
                    System.out.println("Принято check");
                }
                catch (SocketTimeoutException excST) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    continue;
                }
                catch (InterruptedIOException excIIO) {
                    System.out.println("Операция прервана");
                    break;
                }
                catch (IOException excIO) {
                    System.out.println("Сервер закрыл сокет (аварийно)");
                    break;
                }
                if (strInLine == null) {
                    System.out.println("Сервер закрыл сокет (нормально)");
                    break;
                }
                if (strInLine.equals("check")) {
                    strOutLine = "ready";
                    synchronized (lockCalculating) {
                        out.println(strOutLine);
                        out.flush();
                        System.out.println("Отправлено " + strOutLine);
                        try {
                            strInLine = in.readLine();
                        }
                        catch (IOException excIO) {
                            System.out.println("Сервер закрыл сокет после проверки(авариайно)");
                        break;
                        }
                        if (strInLine == null){
                            System.out.println("Сервер закрыл сокет после проверки(нормально)");
                        }
                        if (strInLine.equals("stopJoin")) {
                            System.out.println("Сервер прервал ожидание данных");
                            continue;
                        }
                        getInputMathingValue(strInLine);
                        CalculatingWorker newCalculatingWorker = new CalculatingWorker(inputMathingValue[0],
                                                                                 inputMathingValue[1],
                                                                                 inputMathingValue[2]);
                        double partIntegral = newCalculatingWorker.operation();
                        strOutLine = getOutStr(partIntegral);
                        out.println(strOutLine);
                        out.flush();
                        controlerTableData.push_back_input_data(inputMathingValue[0],
                                                                inputMathingValue[1],
                                                                inputMathingValue[2],
                                                                partIntegral);
                        controlerTableData.show_last_integral();
                }
            }
        }
        System.out.println("Цикл взаимодействия с сервером (завершено)");
        return 0;
    }
    public int disconnection () {
        synchronized (lockCalculating) {
            try {
                if (in != null) {
                   in.close();
                }
                if (out != null) {
                    out.close();
                }
                in = null;
                out = null;
                socket = null;
            }
            catch (IOException excIO) {
                System.out.println("При закрытие сокета произошла ошибка. Связанная с excIO");
            }
        }
        return 0;
    }
    public NetworkControlerCl(DefaultTableModel tableModel) throws ClientException{
        inputMathingValue = new double [3];
        controlerTableData = new ControlerTableData(tableModel);
    }
    @Override
    public void close(){
        disconnection();
    }
}
