package com.lankaice.project.Validation;

import com.lankaice.project.dto.ShiftInfoDto;
import com.lankaice.project.model.AttendanceModel;
import com.lankaice.project.dto.AttendanceDto;
<<<<<<< HEAD
=======
import com.sun.net.httpserver.HttpServer;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
<<<<<<< HEAD
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

=======
import java.util.HashMap;
import java.util.Map;

>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
import static com.lankaice.project.util.ShiftUtil.getShiftBasedOnTime;

public class QRHttpServer {
    public interface ResponseListener {
        void onResponse(String response);
    }

    private static ResponseListener listener;
    private static HttpServer server;
<<<<<<< HEAD
    public static String message;
    public static String response;
=======
    public static String  message;
    public static String  response;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    private static boolean isRunning = false;

    public static void setResponseListener(ResponseListener rl) {
        listener = rl;
    }

    public static void startServer() throws IOException {
        if (isRunning) {
            response = "⚠️ Server is already running.";
            return;
        }
<<<<<<< HEAD

=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        AttendanceModel attendanceModel = new AttendanceModel();
        server = HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/scan", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                URI requestUri = exchange.getRequestURI();
                Map<String, String> params = queryToMap(requestUri.getRawQuery());

                String empId = params.get("id");
                ShiftInfoDto shiftInfo = getShiftBasedOnTime();
                String date = shiftInfo.getDate();
<<<<<<< HEAD
                String shift = shiftInfo.getShift();

                try {
                    AttendanceDto existing = attendanceModel.getAttendance(empId, date, shift);
                    if (existing != null) {
 // -----------------------------------------------------------------------------------------------------------------------------------
                       /* if (existing.getOutTime() == null) {
                            // Mark out_time
                            boolean result = attendanceModel.updateOutTime(empId, date, shift, LocalTime.now());
                            response = result
                                    ? "✅ Out-time marked for " + empId + " (" + shift + " shift)"
                                    : "❌ Failed to mark out-time.";
                            sendResponse(exchange, 200, response);
                            return;
                        }*/
// -----------------------------------------------------------------------------------------------------------------------------------
                        if (existing.getOutTime() == null) {
                            LocalTime now = LocalTime.now();
                            LocalTime inTime = existing.getInTime();

                            if (inTime != null && java.time.Duration.between(inTime, now).toHours() >= 4) {
                                boolean result = attendanceModel.updateOutTime(empId, date, shift, now);
                                response = result
                                        ? "✅ Out-time marked for " + empId + " (" + shift + " shift)"
                                        : "❌ Failed to mark out-time.";
                                sendResponse(exchange, 200, response);
                            } else {
                                response = "⚠️ Minimum 4 hours must pass from in-time to mark out-time. Try again later.";
                                sendResponse(exchange, 400, response);
                            }
                            return;
                        }
 // -----------------------------------------------------------------------------------------------------------------------------------
                        else {
                            // Already has in_time and out_time
                            response = "⚠️ Attendance already fully marked for empId " + empId + " (" + shift + " shift)";
                            sendResponse(exchange, 400, response);
                            return;
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    response = "❌ DB Error: " + e.getMessage();
                    sendResponse(exchange, 500, response);
                    return;
=======
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
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
                }


                if (empId != null && !empId.isEmpty()) {
<<<<<<< HEAD
                    AttendanceDto dto = new AttendanceDto(
                            empId,
                            LocalDate.parse(date),
                            shift,
                            "Present",
                            LocalTime.now(),
                            null  // Out time can be handled separately later
                    );

                    try {
                        boolean result = attendanceModel.markAttendance(dto);
                        response = result
                                ? "✅ Attendance marked for " + empId + " (" + shift + " shift)"
                                : "❌ Failed to mark attendance.";
                        sendResponse(exchange, 200, response);
                    } catch (SQLException | ClassNotFoundException e) {
                        response = "❌ Server error: " + e.getMessage();
                        sendResponse(exchange, 500, response);
                    }
                } else {
                    response = "⚠️ Invalid or missing employee ID";
                    sendResponse(exchange, 400, response);
                }

            } else {
                response = "❌ Only GET method is supported.";
                sendResponse(exchange, 405, response);
=======
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
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
            }
        });

        server.start();
        isRunning = true;
        message = "✅ QR Attendance Server started";
<<<<<<< HEAD
    }

    public static void stopServer() {
        if (server != null && isRunning) {
            server.stop(0);
            isRunning = false;
            message = "🛑 QR Attendance Server stopped";
        } else {
            message = "⚠️ Server is already stopped.";
=======

    }
    public static void stopServer() {
        if (server != null && isRunning) {
            server.stop(0);
            isRunning = false; // <-- update flag
            message = "🛑 QR Attendance Server stopped";
        } else {
            message ="⚠️ Server is already stopped.";
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        }
    }

    public static boolean isServerRunning() {
        return isRunning;
    }

<<<<<<< HEAD
    // Utility: Send response and notify listener
    private static void sendResponse(com.sun.net.httpserver.HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
        if (listener != null) listener.onResponse(message);
    }

    // Utility: Parse query string into map
=======
    // Helper: Converts query string to Map with URL decoding
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
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
<<<<<<< HEAD
}

=======

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
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
