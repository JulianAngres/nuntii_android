package com.nuntiias.nuntiitheone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateTimeComparator {

    public static boolean NoPastMatches(String date) {

        boolean J = false;

        String localDate = ((LocalDate) LocalDate.now()).toString();

        if (TimeDifference(localDate, date, "12:00", "12:00") > -2880) {
            J = true;
        }

        return J;

    }

    public static boolean ReceptionNotYet(String arrivalDate, String arrivalTime) {

        boolean J = true;

        String localDate = ((LocalDate) LocalDate.now()).toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        String localTime = LocalTime.now().format(dtf);

        if (TimeDifference(localDate, arrivalDate, localTime, arrivalTime) <= 0) {
            J = false;
        }

        return J;

    }

    public static boolean ThreeHoursBetweenDifferentAirportsEmptyStrings(boolean beginner, String oldAirport, String oldAirportTime, String newAirport, String newAirportTime) {

        boolean J = true;

        if (beginner == true) {
            J = false;
        } else if (oldAirport == "") {
            J = true;
        } else if (oldAirportTime == "") {
            J = true;
        } else if (newAirport == "") {
            J = true;
        } else if (newAirportTime == "") {
            J = true;
        } else {
            J = false;
        }

        return J;

    }


    public static  boolean FourtyeightHoursBetweenFlights(boolean beginner, String oldAirportTime, String newAirportTime, String oldAirportDate, String newAirportDate) {

        boolean J = true;

        if (beginner == true) {
            J = false;
        } else if (oldAirportTime == "") {
            J = false;
        } else if (newAirportTime == "") {
            J = false;
        } else {

            if (TimeDifference(oldAirportDate, newAirportDate, oldAirportTime, newAirportTime) > 48*60) {
                J = true;
            } else {
                J = false;
            }

        }

        return J;

    }


    public static boolean ThirtyMinutesBetweenSameAirport(boolean beginner, String oldAirport, String oldAirportTime, String newAirport, String newAirportTime, String oldAirportDate, String newAirportDate) {

        boolean J = true;

        if (beginner == true) {
            J = false;
        } else if (oldAirport == "") {
            J = false;
        } else if (oldAirportTime == "") {
            J = false;
        } else if (newAirport == "") {
            J = false;
        } else if (newAirportTime == "") {
            J = false;
        } else if (!oldAirport.equals(newAirport)) {
            J = false;
        } else {
            if (TimeDifference(oldAirportDate, newAirportDate, oldAirportTime, newAirportTime) < 30) {
                J = true;
            } else {
                J = false;
            }
        }

        return J;

    }




    public static boolean ThreeHoursBetweenDifferentAirports(boolean beginner, String oldAirport, String oldAirportTime, String newAirport, String newAirportTime, String oldAirportDate, String newAirportDate) {

        boolean J = true;

        if (beginner == true) {
            J = false;
        } else if (oldAirport == "") {
            J = false;
        } else if (oldAirportTime == "") {
            J = false;
        } else if (newAirport == "") {
            J = false;
        } else if (newAirportTime == "") {
            J = false;
        } else if (oldAirport.equals(newAirport)) {
            J = false;
        } else {
            if (TimeDifference(oldAirportDate, newAirportDate, oldAirportTime, newAirportTime) < 180) {
                J = true;
            } else {
                J = false;
            }

        }

        return J;

    }


    public static boolean NoPastFlights(String dateOrigin) {

        boolean J = true;

        try {
            String localDate = ((LocalDate) LocalDate.now()).toString();
            String year = localDate.substring(0, 4);
            String month = localDate.substring(5, 7);
            String day = localDate.substring(8);

            int intYear = Integer.parseInt(year);
            int intMonth = Integer.parseInt(month);
            int intDay = Integer.parseInt(day);

            String originYear = dateOrigin.substring(0, 4);
            String originMonth = dateOrigin.substring(5, 7);
            String originDay = dateOrigin.substring(8);

            int intOriginYear = Integer.parseInt(originYear);
            int intOriginMonth = Integer.parseInt(originMonth);
            int intOriginDay = Integer.parseInt(originDay);

            if (intYear - intOriginYear > 0) {
                J = true;
            } else if (intYear - intOriginYear == 0) {

                if (intMonth - intOriginMonth > 0) {
                    J = true;
                } else if (intMonth - intOriginMonth < 0) {
                    J = false;
                } else if (intMonth - intOriginMonth == 0) {

                    if (intDay - intOriginDay >= 0) {
                        J = true;
                    } else if (intDay - intOriginDay < 0) {
                        J = false;
                    }

                }

            } else {
                J = false;
            }
        } catch (Exception e) {
            J = false;
        }




        return J;

    }











    public static Long TimeDifference(String date1, String date2, String time1, String time2) {

        String year1 = date1.substring(0, 4);
        String month1 = date1.substring(5, 7);
        String day1 = date1.substring(8);
        String year2 = date2.substring(0, 4);
        String month2 = date2.substring(5, 7);
        String day2 = date2.substring(8);

        String hour1 = time1.substring(0, 2);
        String minute1 = time1.substring(3);
        String hour2 = time2.substring(0, 2);
        String minute2 = time2.substring(3);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime zeitpunkt1 = LocalDateTime.parse(year1 + "-" + month1 + "-" + day1 + " " + hour1 + ":" + minute1 + ":00", formatter);
        LocalDateTime zeitpunkt2 = LocalDateTime.parse(year2 + "-" + month2 + "-" + day2 + " " + hour2 + ":" + minute2 + ":00", formatter);

        long difference = java.time.Duration.between(zeitpunkt1, zeitpunkt2).toMinutes();


        return difference;
    }

    public static boolean OnlyNorwegianAirports(String iata1, String iata2) {


        List<String> norwayIataList = new ArrayList<String>();
        norwayIataList.add("AES");
        norwayIataList.add("ALF");
        norwayIataList.add("ANX");
        norwayIataList.add("BDU");
        norwayIataList.add("BJF");
        norwayIataList.add("BGO");
        norwayIataList.add("BVG");
        norwayIataList.add("BOO");
        norwayIataList.add("BNN");
        norwayIataList.add("VDB");
        norwayIataList.add("FAN");
        norwayIataList.add("FRO");
        norwayIataList.add("FDE");
        norwayIataList.add("DLD");
        norwayIataList.add("GLL");
        norwayIataList.add("HMR");
        norwayIataList.add("HFT");
        norwayIataList.add("EVE");
        norwayIataList.add("HAA");
        norwayIataList.add("HAU");
        norwayIataList.add("HVG");
        norwayIataList.add("QKX");
        norwayIataList.add("KKN");
        norwayIataList.add("KRS");
        norwayIataList.add("KSU");
        norwayIataList.add("LKL");
        norwayIataList.add("LKN");
        norwayIataList.add("MEH");
        norwayIataList.add("MQN");
        norwayIataList.add("MOL");
        norwayIataList.add("MJF");
        norwayIataList.add("RYG");
        norwayIataList.add("OSY");
        norwayIataList.add("NVK");
        norwayIataList.add("NTB");
        norwayIataList.add("OLA");
        norwayIataList.add("HOV");
        norwayIataList.add("FBU");
        norwayIataList.add("OSL");
        norwayIataList.add("RRS");
        norwayIataList.add("RVK");
        norwayIataList.add("RET");
        norwayIataList.add("SDN");
        norwayIataList.add("TRF");
        norwayIataList.add("SSJ");
        norwayIataList.add("SKE");
        norwayIataList.add("SOG");
        norwayIataList.add("SOJ");
        norwayIataList.add("SVG");
        norwayIataList.add("SKN");
        norwayIataList.add("SRP");
        norwayIataList.add("LYR");
        norwayIataList.add("SVJ");
        norwayIataList.add("TOS");
        norwayIataList.add("TRD");
        norwayIataList.add("VDS");
        norwayIataList.add("VRY");
        norwayIataList.add("VAW");

        if (norwayIataList.contains(iata1) && norwayIataList.contains(iata2)) {
            return false;
        }
        else {
            return true;
        }

    }
}
