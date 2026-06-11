package com.dinereserve;

import com.dinereserve.controller.AuthController;
import com.dinereserve.controller.ReservationController;
import com.dinereserve.controller.TableController;
import com.dinereserve.repository.InMemoryDatabase;
import com.dinereserve.repository.ReservationRepository;
import com.dinereserve.repository.TableRepository;
import com.dinereserve.repository.UserRepository;
import com.dinereserve.service.AuthService;
import com.dinereserve.service.ReservationService;
import com.dinereserve.service.TableService;
import com.dinereserve.util.HttpUtils;
import com.dinereserve.util.JsonUtils;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class DineReserveApplication {
    public static void main(String[] args) throws IOException {
        int port = 5001;
        String envPort = System.getenv("DINE_RESERVE_PORT");
        if (envPort != null && !envPort.isBlank()) {
            port = Integer.parseInt(envPort);
        } else if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        InMemoryDatabase database = InMemoryDatabase.getInstance();

        UserRepository userRepository = new UserRepository(database);
        TableRepository tableRepository = new TableRepository(database);
        ReservationRepository reservationRepository = new ReservationRepository(database);

        AuthService authService = new AuthService(userRepository);
        TableService tableService = new TableService(tableRepository, reservationRepository);
        ReservationService reservationService = new ReservationService(reservationRepository, tableRepository, userRepository);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/auth", new AuthController(authService));
        server.createContext("/api/tables", new TableController(tableService));
        server.createContext("/api/reservations", new ReservationController(reservationService));
        server.createContext("/", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                HttpUtils.handleOptions(exchange);
                return;
            }
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "DineReserve backend is running. Use /api/health for the health check.");
            response.put("status", "ok");
            HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(response));
        });
        server.createContext("/api/health", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                HttpUtils.handleOptions(exchange);
                return;
            }
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "DineReserve backend is running");
            response.put("status", "ok");
            HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(response));
        });
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();

        System.out.println("DineReserve backend running on http://localhost:" + port);
    }
}