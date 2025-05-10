package com.lankaice.project.Validation;

import com.lankaice.project.dto.ShiftInfoDto;
import com.lankaice.project.model.AttendanceModel;
import com.lankaice.project.dto.AttendanceDto;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.lankaice.project.util.ShiftUtil.getShiftBasedOnTime;

public class QRHttpServer {
    public interface ResponseListener {
        void onResponse(String response);
    }

    private static ResponseListener listener;
    private static HttpServer server;
    public static String  message;
    public static String  response;
    private static boolean isRunning = false;

    public static void setResponseListener(ResponseListener rl) {
        listener = rl;
    }

    public static void startServer() throws IOException {
        if (isRunning) {
            response = "⚠️ Server is already running.";
            return;
        }
        AttendanceModel attendanceModel = new AttendanceModel();
        server = HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/scan", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                URI requestUri = exchange.getRequestURI();
                Map<String, String> params = queryToMap(requestUri.getRawQuery());

                String empId = params.get("id");
                ShiftInfoDto shiftInfo = getShiftBasedOnTime();
                String date = shiftInfo.getDate();
                String shift = shiftInfo.getShift(); // Automatically determine shift

                try {
                    if(attendanceModel.isDuplicateAttendance(empId, date, shift)){
                        response = "⚠️ Attendance already marked for empId " + empId + " (" + shift + " shift)";
                        exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
                        if (listener != null) listener.onResponse(response);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(StandardCharsets.UTF_8));
                        }
                        return; // Important to return after sending the response
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


                if (empId != null && !empId.isEmpty()) {
                    AttendanceDto dto = new AttendanceDto(empId, date, shift, "Present");
                    try {
                        boolean result = attendanceModel.markAttendance(dto);

                        response = result
                                ? "✅ Attendance marked for " + empId + " (" + shift + " shift)"
                                : "❌ Failed to mark attendance (maybe already marked)";
                        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                        if (listener != null) listener.onResponse(response);
                    } catch (SQLException | ClassNotFoundException e) {
                        response = "❌ Server error: " + e.getMessage();
                        exchange.sendResponseHeaders(500, response.getBytes(StandardCharsets.UTF_8).length);
                        if (listener != null) listener.onResponse(response);
                    }
                } else {
                    response = "⚠️ Invalid or missing employee ID";
                    exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
                    if (listener != null) listener.onResponse(response);
                }

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }

            } else {
                String response = "❌ Only GET method is supported.";
                exchange.sendResponseHeaders(405, response.getBytes(StandardCharsets.UTF_8).length);
                if (listener != null) listener.onResponse(response);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
        });

        server.start();
        isRunning = true;
        message = "✅ QR Attendance Server started";

    }
    public static void stopServer() {
        if (server != null && isRunning) {
            server.stop(0);
            isRunning = false; // <-- update flag
            message = "🛑 QR Attendance Server stopped";
        } else {
            message ="⚠️ Server is already stopped.";
        }
    }

    public static boolean isServerRunning() {
        return isRunning;
    }

    // Helper: Converts query string to Map with URL decoding
    private static Map<String, String> queryToMap(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        String[] params = query.split("&");
        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                map.put(key, value);
            }
        }
        return map;
    }

}


/*
package com.lankaice.project.Validation;

import com.sun.net.httpserver.HttpServer;
import com.lankaice.project.model.AttendanceModel;
import com.lankaice.project.dto.AttendanceDto;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class QRHttpServer {

    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/scan", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {

                URI requestUri = exchange.getRequestURI();
                Map<String, String> params = queryToMap(requestUri.getQuery()); // ?id=EMP001&shift=Morning

                String empId = params.get("id");
                String shift = params.getOrDefault("shift", "Morning"); // default Morning

                String response;
                if (empId != null && !empId.isEmpty()) {
                    AttendanceDto dto = new AttendanceDto(empId, LocalDate.now().toString(), shift, "Present");
                    boolean result = false;
                    try {
                        AttendanceModel attendanceModel = new AttendanceModel();
                        result = attendanceModel.markAttendance(dto);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    response = result
                            ? "✅ Attendance marked for " + empId + " (" + shift + " shift)"
                            : "❌ Failed to mark attendance (maybe already marked)";
                } else {
                    response = "⚠️ Invalid or missing employee ID";
                }

                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.start();
        System.out.println("✅ QR Attendance Server started on port 8081");
    }

    // Helper: Converts query string to Map
    private static Map<String, String> queryToMap(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        String[] params = query.split("&");
        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }
}



*/
