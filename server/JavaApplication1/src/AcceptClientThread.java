
import java.net.ServerSocket;
import java.util.LinkedList;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class AcceptClientThread extends Thread{
    private int freeIdClient = 0;
    private ServerSocket serverSocket;
    private LinkedList <OneClientController> listClients;
    private final Object clientsLock;
    private volatile boolean flagRunning;
    public AcceptClientThread(ServerSocket objServerSocket, 
                              Object objSyncList,
                              LinkedList<OneClientController> objlistClients,
                              boolean flagServerWork) throws ServerException{
        serverSocket = objServerSocket;
        clientsLock = objSyncList;
        listClients = objlistClients;
        flagRunning = flagServerWork;
        try {
            serverSocket.setSoTimeout(500);
        }
        catch (SocketException excS) {
            throw new ServerException("AcceptClientThread.AcceptClientThread()",
                                      "КРИТИЧЕСКАЯ ОШИБКА. НЕ УДАЛОСЬ УСТАНОВИТЬ ТАЙМАУТ");
        }
    }
    @Override
    public void run() {
        System.out.println("Запущен поток подключения клиентов.\n\t Таймаут : 0.5 Секунд");
        while (flagRunning == true) {
            try {
                Socket newSocket = serverSocket.accept();
                OneClientController newOneClientControler = new OneClientController(newSocket, freeIdClient);
                freeIdClient++;
                synchronized (clientsLock) {
                    listClients.add(newOneClientControler);
                }
            }
            catch(SocketTimeoutException excST) {
                continue;
            }
            catch (SocketException excS){
                break;
            }
            catch (IOException excIO){
                if (flagRunning) {
                    System.out.println("Поток подключения клиентов: выкинуто исключение IOException");
                }
                break;
            }
        }
        System.out.println("Потока подключения клиентов (ЗАВЕРШЕН)");
    }
}
