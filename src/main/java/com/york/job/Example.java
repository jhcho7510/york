package com.york.job;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Example {
    public static void main(String[] args) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String caseStartDate = dateFormat.format(LocalDateTime.now());
        System.out.println(caseStartDate);
        LocalDateTime localdatetime = LocalDateTime.parse(caseStartDate, dateFormat);
        System.out.println(localdatetime);
    }


}
