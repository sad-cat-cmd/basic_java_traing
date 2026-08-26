/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import com.mycompany.weblogger.WebLogger;
import com.mycompany.weblogger.ExceptionInitLog;
import com.mycompany.weblogger.SendedLog;
import com.mycompany.weblogger.ExceptionLoggerWork;
/**
 *
 * @author admin_
 */
public class NetworkControlerServ implements AutoCloseable{
    private boolean flagServerWork = false;
    private int countClient = 0;
    private int port = 8080;
    private ServerSocket serverSocket;
    private LinkedList <OneClientController> listClients;
    private AcceptClientThread acceptThread;
    private WebLogger logger;
    
    private final Object clientsLock = new Object();
    
    private void sendLog(String msg, String status){
        try {
            SendedLog log = new SendedLog(msg, status);
            if (logger == null) {
                System.out.println("WebLogger = null, невозможно отправить сообщение");
                return;
            }
            logger.sendLog(log);   
        }
        catch (ExceptionInitLog excIL) {
            System.out.println("ОШИБКА ПРИ ИНИЦИАЛИЗАЦИИ ЛОГА: " + excIL.getMsg() + ", time: " + excIL.getTime());
        }
        catch (ExceptionLoggerWork excLW) {
            System.out.println("ОШИБКА ПРИ ОТПРАВКЕ ЛОГА: " + excLW.getMsg() + ", time: " + excLW.getTime());
        }
    }
    public void printInfoStatusStopingJoinDataClients(int idClient, String StrStatus) {
        System.out.println("Клиент " + idClient  + "\n" +
                          StrStatus);
    }
    private void printInfoStatusWorkClientThread (int idClient, double partIntegral, String strStatusWork) {
        System.out.println("Клиент " + idClient + "\n"+
                           strStatusWork + "\n" +
                           "Интеграл: " + partIntegral);
    }
    private void printInfoStatusClient(int idClient, boolean status, String info) {
        System.out.println("\tКлиент " + idClient + "\n\t\t" + 
                           status + "\n\t\t" +
                           info);
    }
    private int removeClient (int idRemovedClient) {
        int resultClosing = 0;
        for (int i = 0; i < listClients.size(); i++) {
            if (idRemovedClient == listClients.get(i).idClient){
                resultClosing = listClients.get(i).close();
                listClients.remove(i);
            }
        }
        return resultClosing;
    }
    
    public int pingAllClients () {
        boolean flagErrorThread = false;
        this.sendLog("Запуск проверки доступности всех клиентов...", "INFO");
        System.out.println("Запуск проверки доступности всех клиентов...");
        
        TimeInterval timeIntervalPingAllClients = new TimeInterval();
        LinkedList <ClientPingThread> listClientThreadForPing = new LinkedList <ClientPingThread>();
        LinkedList <Integer> listIdClientForRemove = new LinkedList <Integer>();
        int countReadyClient = 0;
        int countStopingClient = 0;
        for (int i = 0; i < listClients.size(); i++) {
            ClientPingThread newClientThreadForPing = new ClientPingThread(listClients.get(i).out,
                                                                           listClients.get(i).in,
                                                                           listClients.get(i).idClient);
            listClientThreadForPing.add(newClientThreadForPing);
            listClientThreadForPing.getLast().start();
        }
        this.sendLog("Начало проверки статусов клиентов...", "INFO");
        System.out.println("Начало проверки статусов клиентов...");
        for (int i = 0; i < listClients.size(); i++) {
            try {
                listClientThreadForPing.get(i).join();
            }
            catch (InterruptedException excI) {
                this.sendLog("Проверка доступности клиентов была преравана авариайно. Ошибка. Какой-то из потоков обработки клиентов был прерван." +
                             "Время выполнения: " + timeIntervalPingAllClients.getTimeIntervalMs() + " ms.", "FATAL");
                System.out.println("Проверка доступности клиентов была преравана авариайно \n\tОшибка. Какой-то из потоков был прерван.\n\t" +
                                   "Время выполнения: " + timeIntervalPingAllClients.getTimeIntervalMs() + " ms.");
                flagErrorThread = true;
            }
            if (listClientThreadForPing.get(i).getStatus() == true) {
                countReadyClient++;
            }
            else {
                countStopingClient++;
                listIdClientForRemove.add(listClientThreadForPing.get(i).getIdClient());
            }
            printInfoStatusClient(listClientThreadForPing.get(i).getIdClient(),
                                  listClientThreadForPing.get(i).getStatus(),
                                  listClientThreadForPing.get(i).getInfo());
        }
        
        this.sendLog("Начало закрытия нитей обработки отключившихся клиентов...", "INFO");
        System.out.println("Начало закрытия нитей обработки отключившихся клиентов...");
        for (int i = 0; i < listIdClientForRemove.size(); i++) {
            int idForRemove = listIdClientForRemove.get(i);
            int resultRemove = removeClient(idForRemove);
            if (resultRemove == 1) {
                
                System.out.println("Для клиента " + idForRemove + "не получилось закрыть BufferedReader автоматически, процесс его очистки возложен на GC");
            }
        }
//        this.sendLog("Конец закрытия нитей обработки отключившихся клиентов", "INFO");
//        System.out.println("Конец закрытия нитей обработки отключившихся клиентов");
        
        this.sendLog("Проверка доступности клиентов была осуществлена коректно" + 
                     ". Количество доступных клиентов: " + countReadyClient + 
                     ". Количество удаленных клиентов: " + countStopingClient +
                     ". Время выполнения: " + timeIntervalPingAllClients.getTimeIntervalMs() + " ms", "INFO");
        System.out.println("Проверка доступности клиентов была осуществлена коректно.\n\t" +
                           "Количество доступных клиентов: " + countReadyClient + "\n\t" +
                           "Количество удаленных клиентов: " + countStopingClient + "\n\t" + 
                           "Время выполнения: " + timeIntervalPingAllClients.getTimeIntervalMs() + " ms");
        if (flagErrorThread == true) {
            return -1;
        }
        return countReadyClient;
    }
    public double calculatingIntegral(double maxX, double minX, double step) {
        double partIntegral = 0.0;
        boolean flagErrorOperation = false;
        double integral = 0.0;
        LinkedList <ClientCalculatingThread> listClientCalculatingThread = new LinkedList <ClientCalculatingThread> ();
        double arrMinX [] = new double [countClient];
        double arrMaxX [] = new double [countClient];
        double difCalculating = (maxX - minX) / countClient;
        double localMinX = minX;
        double localMaxX = minX;
            
        for (int i = 0; i < countClient; i++) {
            localMaxX += difCalculating;
            arrMinX[i] = localMinX;
            arrMaxX[i] = localMaxX;
            localMinX = localMaxX;
        }
        for (int i = 0; i < listClients.size(); i++) {
            ClientCalculatingThread newClientCalculatingThread = new ClientCalculatingThread(arrMinX[i], arrMaxX[i], step,
                                                                                             listClients.get(i).out,listClients.get(i).in);
            listClientCalculatingThread.add(newClientCalculatingThread);
            listClientCalculatingThread.getLast().start();
        }
        for (int i = 0; i < listClients.size(); i++) {
            try {
                listClientCalculatingThread.get(i).join();
            }
            catch (InterruptedException excI) {
                flagErrorOperation = true;
                continue;
            }
            partIntegral = listClientCalculatingThread.get(i).getIntegral();
            integral += partIntegral;
            printInfoStatusWorkClientThread(listClients.get(i).idClient, integral, listClientCalculatingThread.get(i).getStrStatusCalculating());
        }
        if (flagErrorOperation == true) {
            return -1.1;
        }
        return integral;
    }
    public int stopingJoinDataClients(){
        boolean flagErrorThread = false;
        LinkedList <ClientStopJoinDataThread> listClientStopJoinDataThread = new LinkedList <ClientStopJoinDataThread>();
        for (int i = 0; i < listClients.size(); i++) {
            ClientStopJoinDataThread newClientStopJoinDataThread = new ClientStopJoinDataThread(listClients.get(i).out, listClients.get(i).in);
            listClientStopJoinDataThread.add(newClientStopJoinDataThread);
            listClientStopJoinDataThread.getLast().start();
        }
        for (int i = 0; i < listClientStopJoinDataThread.size(); i++) {
            try {
                listClientStopJoinDataThread.get(i).join();
            }
            catch(InterruptedException excI){
                flagErrorThread = true;
                continue;
            }
            printInfoStatusStopingJoinDataClients(listClients.get(i).idClient, 
                                                  listClientStopJoinDataThread.get(i).getStrAddedInfoStatusStoping());
        }
        if (flagErrorThread == true) {
            return 1;
        }
        return 0;
    }
    public double getIntegral(double maxX, double minX, double step) throws ServerException {
        this.sendLog("Вычисление интеграла", "INFO");
        System.out.println("Вычисления интеграла...");
        
        TimeInterval timeIntervalCalculationIntegral = new TimeInterval();
        double resultIntegral;
        synchronized (clientsLock) {
            countClient = pingAllClients();
            if (countClient == 0) {
                this.sendLog("Не получилось вычислить интеграл. Количество клиентов = 0", "WARN");
                throw new ServerException("NetworkController.getIntegral()",
                                          "Не получилось вычислить интеграл. Количество клиентов = 0");
            }
            if (countClient == -1) {
                this.sendLog("Отправка сообщений об окончании ожидания...", "INFO");
                System.out.println("Отправка сообщений об окончании ожидания...");
                
                int resultStoping = stopingJoinDataClients();
                System.out.println();
                if (resultStoping == 1) {
                    this.sendLog("НЕ УДАЛОСЬ ВЫЧИСЛИТЬ ИНТЕГРАЛ. КРИТИЧЕСКАЯ ОШИБКА ПРИ ОТПРАВКЕ ЗАПРОСОВ ПРЕКРАЩЕНИЯ ОЖИДАНИЯ ДАННЫХ.", "FATAL");
                    throw new ServerException ("NetworkController.getIntegral()",
                                               "НЕ УДАЛОСЬ ВЫЧИСЛИТЬ ИНТЕГРАЛ. КРИТИЧЕСКАЯ ОШИБКА ПРИ ОТПРАВКЕ ЗАПРОСОВ ПРЕКРАЩЕНИЯ ОЖИДАНИЯ ДАННЫХ.\nПерезапустите приложение");
                }
                this.sendLog("Отправка сообщений об окончании ожидания ЗАВЕРШЕНО", "INFO");
                System.out.println("Отправка сообщений об окончании ожидания ЗАВЕРШЕНО");
                
                this.sendLog("НЕ УДАЛОСЬ ВЫЧИЛСИТЬ ИНТЕГРАЛ. КРИТИЧЕСКАЯ ОШИБКА ПРИ ПОПЫТКЕ ПРОВЕРИТЬ СТАТУС КЛИЕНТОВ", "FATAL");
                throw new ServerException("NetworkController.getIntegral()",
                                          "НЕ УДАЛОСЬ ВЫЧИЛСИТЬ ИНТЕГРАЛ. КРИТИЧЕСКАЯ ОШИБКА ПРИ ПОПЫТКЕ ПРОВЕРИТЬ СТАТУС КЛИЕНТОВ.\n Перезапуститие приложение");
            }
            resultIntegral = calculatingIntegral(maxX, minX, step);
            if (resultIntegral == -1.1) {
                this.sendLog("НЕ УДАЛОСЬ ВЫЧИСЛИТЬ ИНТЕГРАЛ. КРИТИЧЕСКАЯ ОШИБКА ПРИ ПОДЧЕТЕ ИНТЕРВАЛА.", "FATAL");
                throw new ServerException("NetworkController.getIntegral()",
                                          "КРИТИЧЕСКАЯ ОШИБКА ПРИ ПОДЧЕТЕ ИНТЕРВАЛА.\n Перезапуститие приложение");
            }
            this.sendLog("Вычисления интеграла ЗАВЕРШЕНО. Время:  " + timeIntervalCalculationIntegral.getTimeIntervalMs() + "ms", "INFO");
            System.out.println("Вычисления интеграла ЗАВЕРШЕНО\n\t" +
                               "Время: " + timeIntervalCalculationIntegral.getTimeIntervalMs() + " ms");
            return resultIntegral;
        }
    }
    public void startServer () throws ServerException {
        if (logger == null) {
            try {
                logger = new WebLogger("admin_",
                                       "serverCalculateIntegral");
                logger.initProcess();
            }
            catch (ExceptionLoggerWork excLW) {
                logger = null;
                System.out.println("NetworkControler.startServer()" + excLW.getMsg() + "\n\t" + "time: " + excLW.getTime() + "\n\t restart this program");
                throw new ServerException("NetworkControler.startServer()", excLW.getMsg() + ", time: " + excLW.getTime() + ", restart this program");
            }
        }
        if (flagServerWork) {
            this.sendLog("Сервер уже запущен",
                         "INFO");
            System.out.println("Сервер уже запущен");
            return;
        }
        try {
            serverSocket = new ServerSocket(port); 
        }
        catch (IOException exc) {
            this.sendLog("Ошибка при инициализации сокета сервера (ServerSocket)", "FATAL");
            throw new ServerException("NetworkControler.startServer()", exc.getMessage());
        }
        flagServerWork = true;
        try {
            acceptThread = new AcceptClientThread(serverSocket,
                                              clientsLock,
                                              listClients,
                                              flagServerWork);
        } 
        catch (ServerException excS) {
            this.sendLog("Ошибка при инициализации нити (thread) приема подключений", "FATAL");
            throw excS;
        }
        acceptThread.start();
    }
    
    public NetworkControlerServ() {
        listClients = new LinkedList <OneClientController>();
    }
    
    @Override
    public void close() {
        if (flagServerWork == false) {
            this.sendLog("Сервер не закрыт. Так как сервер не запущен", "INFO");
            System.out.println("Сервер еще не запущен.");
            return;
        }
        flagServerWork = false;
        synchronized (clientsLock) {
            for (int i = 0; i < listClients.size(); i++) {
                listClients.get(i).close();
            }
        }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (IOException excIO) {
                    this.sendLog("Ошибка при закрытии сокета сервера (Server Socket) ", "WARN");
                    System.out.println("При закрытии сокета произошла ошибка.");
                    return;
            }
        }
        this.sendLog("Сокет сервера (ServerSocket) был успешно закрыт", "INFO");
        System.out.println("Сокет сервера удачно закрыт");
    }
}