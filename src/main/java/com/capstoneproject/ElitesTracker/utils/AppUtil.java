package com.capstoneproject.ElitesTracker.utils;

import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.exceptions.NoAttendanceOnWeekendsException;
import fiftyone.devicedetection.DeviceDetectionPipelineBuilder;
import fiftyone.devicedetection.shared.DeviceData;
import fiftyone.pipeline.core.data.FlowData;
import fiftyone.pipeline.core.flowelements.Pipeline;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.NO_ATTENDANCE_ON_WEEKENDS_EXCEPTION;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.NO_MAC_ADDRESS_FOUND_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;


public class AppUtil {


    public static String getCurrentTimeStampUsingLocalDateTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return currentTime.format(formatter);
    }
    public static String getCurrentTimeStampUsingZonedDateTime(){
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return currentTime.format(formatter);
    }
    public static String getCurrentDayOfWeek(){
        LocalDateTime currentDayOfWeek = LocalDateTime.now();
        DayOfWeek dayOfWeek = currentDayOfWeek.getDayOfWeek();
        return dayOfWeek.toString();
    }
    public static String getCurrentTimeForAttendance() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT_FOR_ATTENDANCE);
        return currentTime.format(formatter);
    }
    public static String getCurrentDateForAttendance() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_FOR_ATTENDANCE);
        return currentTime.format(formatter);
    }
    public static String getCurrentDateToCompareAttendanceObject() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_TO_COMPARE_ATTENDANCE);
        return currentTime.format(formatter);
    }
    public static String stringDateToString(String userInput) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate dateToFormat = LocalDate.parse(userInput, dateFormatter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return dateToFormat.format(formatter);
    }
    public static LocalDate stringToLocalDate(String userInput) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(userInput, dateFormatter);
    }
    public static String localDateTodayToString() {
        LocalDate dateToFormat = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return dateToFormat.format(dateFormatter);
    }
    public static String localDateToString(LocalDate currentTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return currentTime.format(formatter);
    }
    public static LocalTime stringToLocalTime(String timeFormat){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return LocalTime.parse(timeFormat, formatter);
    }
    public static String localTimeToString(LocalTime timeFormat){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return timeFormat.format(formatter);
    }
    public static String zonedTimeToString(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return zonedDateTime.format(formatter);
    }
    public static String changeDateFormatFromFrontendForReports(String frontendDate){
        DateTimeFormatter frontendFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate formattedDate = LocalDate.parse(frontendDate, frontendFormat);
        return formattedDate.format(desiredFormat);
    }
    public static String changeDateFormatFromFrontendForAttendance(String frontendDate){
        DateTimeFormatter frontendFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate formattedDate = LocalDate.parse(frontendDate, frontendFormat);
        return formattedDate.format(desiredFormat);
    }
    public static String subStringDate(String date) {
        int indexOfSpace = date.indexOf(' ');

        if (indexOfSpace != -1) {
            return date.substring(0, indexOfSpace);
        } else {
            return date;
        }
    }
    public static String subStringRealIp(String ipAddress) {
        if (ipAddress != null && ipAddress.length() >= 9) {
            return ipAddress.substring(0, 7);
        } else {
            return ipAddress;
        }
    }
    public static String subStringTestIp(String ipAddress) {
        if (ipAddress != null && ipAddress.length() >= 9) {
            return ipAddress.substring(0, 9);
        } else {
            return ipAddress;
        }
    }

    public static String welcomeMessage(String name) {
        return String.format(WELCOME_MESSAGE,name);
    }
    public static String nativeNotFoundMessage(String cohort) {
        return String.format(NATIVE_NOT_FOUND,cohort);
    }
    public static String cohortNotFoundMessage(String cohort) {
        return String.format(COHORT_NOT_FOUND_MESSAGE,cohort);
    }
    public static String userAlreadyExistsMessage(String email) {
        return String.format(USER_EXIST_MESSAGE,email);
    }

    public static String beforeAttendanceMessage(String time) {
        return String.format(BEFORE_ATTENDANCE_TIME_MESSAGE,time);
    }
    public static String afterAttendanceMessage(String endTime, String startTime) {
        return String.format(AFTER_ATTENDANCE_TIME_MESSAGE,endTime,startTime);
    }

    public static String savedNativeMessage(String firstName, String lastName, String cohort){
        return String.format(SAVED_NATIVE_MSG, firstName, lastName, cohort);
    }
    public static String savedAdminMessage(String firstName, String lastName){
        return String.format(SAVED_ADMIN_MSG, firstName, lastName);
    }
    public static String normalAttendanceMessage(String firstName){
        return String.format(NORMAL_ATTENDANCE_MESSAGE,firstName);
    }
    public static String tardyAttendanceMessage(String firstName){
        return String.format(TARDY_ATTENDANCE_MESSAGE,firstName);
    }
    public static void noAttendanceOnWeekendsCheck(){
        String today = getCurrentDayOfWeek();
        if(today.equalsIgnoreCase(SATURDAY) || today.equalsIgnoreCase(SUNDAY)){
            throw new NoAttendanceOnWeekendsException(NO_ATTENDANCE_ON_WEEKENDS_EXCEPTION.getMessage());
        }
    }
    public static List<String> getPublicPaths(){
        return List.of("/api/v1/user/register", "/api/v1/user/loginUser", "/api/v1/admin/addNative", "/swagger-ui.html", "api/v1/natives/takeAttendance", "api/v1/user/address");
    }

    public static String retrieveActualIP(HttpServletRequest request){
        String clientIP = request.getHeader("X-Forwarded-For");

        boolean isEmpty = clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP);

        if (isEmpty) {
            clientIP = request.getHeader("Proxy-Client-IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_X_FORWARDED");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_FORWARDED");
        }
        if (isEmpty) {
            clientIP = request.getRemoteAddr();
        }
        return clientIP;
    }

    public static String getMacAddress(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);

            if (networkInterface != null) {
                byte[] macBytes = networkInterface.getHardwareAddress();
                if (macBytes != null) {
                    return formatMacAddress(macBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new EntityDoesNotExistException(NO_MAC_ADDRESS_FOUND_EXCEPTION.getMessage());
    }

    private static String formatMacAddress(byte[] macBytes) {
        StringBuilder macAddress = new StringBuilder();
        try (Formatter formatter = new Formatter(macAddress, Locale.US)) {
            for (byte b : macBytes) {
                formatter.format(FORMAT_MAC_ADDRESS, b);
            }
            return macAddress.substring(0, macAddress.length() - 1);
        }
    }

//    public static String getMACAddressTheSecond(String ip) {
//        String str = "";
//        String macAddress = "";
//        try {
//            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
//            InputStreamReader ir = new InputStreamReader(p.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//            for (int i = 1; i < 100; i++) {
//                str = input.readLine();
//                if (str != null) {
//                    if (str.indexOf("MAC Address") > 1) {
//                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
//                        break;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//        }
//        return macAddress;
//    }
//    static Pattern macpt = null;
//    public static String getMac(String ip) {
//
//        // Find OS and set command according to OS
//        String OS = System.getProperty("os.name").toLowerCase();
//
//        String[] cmd;
//        if (OS.contains("win")) {
//            // Windows
//            macpt = Pattern
//                    .compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
//            String[] a = { "arp", "-a", ip };
//            cmd = a;
//        } else {
//            // Mac OS X, Linux
//            macpt = Pattern
//                    .compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
//            String[] a = { "arp", ip };
//            cmd = a;
//        }
//
//        try {
//            // Run command
//            Process p = Runtime.getRuntime().exec(cmd);
//            p.waitFor();
//            // read output with BufferedReader
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    p.getInputStream()));
//            String line = reader.readLine();
//
//            // Loop trough lines
//            while (line != null) {
//                Matcher m = macpt.matcher(line);
//
//                // when Matcher finds a Line then return it as result
//                if (m.find()) {
//                    System.out.println("Found");
//                    System.out.println("MAC: " + m.group(0));
//                    return m.group(0);
//                }
//
//                line = reader.readLine();
//            }
//
//        } catch (IOException | InterruptedException e1) {
//            e1.printStackTrace();
//        }
//
//        // Return empty string if no MAC is found
//        return "";
//    }

//    public static String getMacAddressFourth() throws Exception {
//        String macAddress = null;
//        String command = "ifconfig";
//
//        String osName = System.getProperty("os.name");
//        System.out.println("Operating System is " + osName);
//
//        if (osName.startsWith("Windows")) {
//            command = "ipconfig /all";
//        } else if (osName.startsWith("Linux") || osName.startsWith("Mac") || osName.startsWith("HP-UX")
//                || osName.startsWith("NeXTStep") || osName.startsWith("Solaris") || osName.startsWith("SunOS")
//                || osName.startsWith("FreeBSD") || osName.startsWith("NetBSD")) {
//            command = "ifconfig -a";
//        } else if (osName.startsWith("OpenBSD")) {
//            command = "netstat -in";
//        } else if (osName.startsWith("IRIX") || osName.startsWith("AIX") || osName.startsWith("Tru64")) {
//            command = "netstat -ia";
//        } else if (osName.startsWith("Caldera") || osName.startsWith("UnixWare") || osName.startsWith("OpenUNIX")) {
//            command = "ndstat";
//        } else {// Note: Unsupported system.
//            throw new Exception("The current operating system '" + osName + "' is not supported.");
//        }
//
//        Process pid = Runtime.getRuntime().exec(command);
//        BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
//        Pattern p = Pattern.compile("([\\w]{1,2}(-|:)){5}[\\w]{1,2}");
//        while (true) {
//            String line = in.readLine();
//            System.out.println("line " + line);
//            if (line == null)
//                break;
//
//            Matcher m = p.matcher(line);
//            if (m.find()) {
//                macAddress = m.group();
//                break;
//            }
//        }
//        in.close();
//        return macAddress;
//    }

    public static String getDeviceId(String resource) throws Exception {
        // create a minimal pipeline to access the cloud engine
        // you only need one pipeline for multiple requests
        // use try-with-resources to free the pipeline when done

        String deviceId = "";
        try (Pipeline pipeline = new DeviceDetectionPipelineBuilder()
                .useCloud(resource)
                .build()) {
            /* get a flow data from the singleton pipeline for each detection */
            // it's important to free the flowData when done
            try (FlowData data = pipeline.createFlowData()) {
                // add user-agent and client hint headers (if any) from the HTTP request
                data.addEvidence("header.user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                "Chrome/98.0.4758.102 Safari/537.36");
                data.addEvidence("header.sec-ch-ua-mobile", "?0");
                data.addEvidence("header.sec-ch-ua",
                        "\" Not A; Brand\";v=\"99\", \"Chromium\";v=\"98\", " +
                                "\"Google Chrome\";v=\"98\"");
                data.addEvidence("header.sec-ch-ua-platform", "\"Windows\"");
                data.addEvidence("header.sec-ch-ua-platform-version", "\"14.0.0\"");
                // process evidence
                data.process();
                // get the results
                DeviceData device = data.get(DeviceData.class);
//                System.out.println("device.DeviceId: " + device.getDeviceId().getValue());
                deviceId = device.getDeviceId().getValue();
            }
        }

        return deviceId;
    }
}
