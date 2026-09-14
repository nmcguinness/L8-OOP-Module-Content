package assessments.gca.gca2.server;

import assessments.gca.gca2.dao.TaskDAO;
import assessments.gca.gca2.db.DatabaseConnection;
import assessments.gca.gca2.service.ClientDispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multithreaded TCP server for the Task management system.
 * Accepts client connections on a fixed port and hands each off to a ClientHandler
 * thread via a fixed-size ExecutorService thread pool.
 *
 * Run this class before TaskClient.
 *
 * @author OOP Teaching Team
 */
public class TaskServer {

    // === Constants ===
    private static final String URL     = "jdbc:mysql://localhost:3306/gca2_support_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final int    PORT    = 9_300;
    private static final int    THREADS = 10;

    // === Fields ===
    private int              _port;
    private ClientDispatcher _dispatcher;
    private ObjectMapper     _mapper;

    // === Constructors ===

    // Creates: a TaskServer bound to the given port using the given dispatcher
    public TaskServer(int port, ClientDispatcher dispatcher, ObjectMapper mapper) {
        _port       = port;
        _dispatcher = dispatcher;
        _mapper     = mapper;
    }

    // === Public API ===

    // Starts: the accept loop; submits each new connection to the thread pool until interrupted
    public void start() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            System.out.println("TaskServer listening on port " + _port + " ...");
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(new ClientHandler(clientSocket, _dispatcher, _mapper));
            }
        }
        finally {
            pool.shutdown();
        }
    }

    // === Entry point ===

    // Creates: and starts a TaskServer; run this before TaskClient
    public static void main(String[] args) throws Exception {
        DatabaseConnection dbConn     = new DatabaseConnection(URL, DB_USER, DB_PASS);
        TaskDAO            taskDAO    = new TaskDAO(dbConn);
        ClientDispatcher   dispatcher = new ClientDispatcher(taskDAO);
        ObjectMapper       mapper     = new ObjectMapper();
        new TaskServer(PORT, dispatcher, mapper).start();
    }
}
