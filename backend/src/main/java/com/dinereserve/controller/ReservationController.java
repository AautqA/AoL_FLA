package com.dinereserve.controller;

import com.dinereserve.domain.Reservation;
import com.dinereserve.service.ReservationService;
import com.dinereserve.util.ApiException;
import com.dinereserve.util.HttpUtils;
import com.dinereserve.util.JsonUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationController implements HttpHandler {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtils.handleOptions(exchange);
            return;
        }

        try {
            String method = exchange.getRequestMethod();
            String remaining = HttpUtils.remainingPath("/api/reservations", exchange.getRequestURI());

            if (remaining.isEmpty()) {
                if ("POST".equalsIgnoreCase(method)) {
                    Map<String, String> body = JsonUtils.parseObject(HttpUtils.readBody(exchange));
                    Reservation reservation = reservationService.createReservation(
                            Integer.parseInt(body.getOrDefault("user_id", "0")),
                            Integer.parseInt(body.getOrDefault("table_id", "0")),
                            body.get("booking_date"),
                            body.get("booking_time"),
                            Integer.parseInt(body.getOrDefault("people_count", "0")));
                    HttpUtils.sendJson(exchange, 201, JsonUtils.toJson(reservation.toMap()));
                    return;
                }

                if ("GET".equalsIgnoreCase(method)) {
                    List<Map<String, Object>> reservations = reservationService.getAllReservations().stream()
                            .map(Reservation::toMap)
                            .collect(Collectors.toList());
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(reservations));
                    return;
                }
            }

            if (remaining.startsWith("user/") && "GET".equalsIgnoreCase(method)) {
                int userId = Integer.parseInt(remaining.substring("user/".length()));
                List<Map<String, Object>> reservations = reservationService.getReservationHistory(userId).stream()
                        .map(Reservation::toMap)
                        .collect(Collectors.toList());
                HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(reservations));
                return;
            }

            if (remaining.endsWith("/cancel") && "PUT".equalsIgnoreCase(method)) {
                String reservationIdPart = remaining.substring(0, remaining.indexOf('/'));
                int reservationId = Integer.parseInt(reservationIdPart);
                Reservation reservation = reservationService.cancelReservation(reservationId);
                HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(reservation.toMap()));
                return;
            }

            if (!remaining.isEmpty()) {
                String[] parts = remaining.split("/");
                int reservationId = Integer.parseInt(parts[0]);

                if (parts.length == 1 && "GET".equalsIgnoreCase(method)) {
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(reservationService.getReservationById(reservationId).toMap()));
                    return;
                }

                if (parts.length == 2 && "cancel".equalsIgnoreCase(parts[1]) && "PUT".equalsIgnoreCase(method)) {
                    Reservation reservation = reservationService.cancelReservation(reservationId);
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(reservation.toMap()));
                    return;
                }
            }

            HttpUtils.sendError(exchange, 404, "Reservation endpoint not found");
        } catch (ApiException exception) {
            HttpUtils.sendError(exchange, exception.getStatusCode(), exception.getMessage());
        } catch (Exception exception) {
            HttpUtils.sendError(exchange, 500, "Internal server error");
        }
    }
}