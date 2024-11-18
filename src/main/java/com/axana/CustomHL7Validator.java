package com.axana;
import java.util.ArrayList;
import java.util.List;

public class CustomHL7Validator {

    public static void main(String[] args) {
        String hl7Message = "MSH|^~\\&|SendingApp|SendingFacility|ReceivingApp|ReceivingFacility|202311141530||ADT^A01|123456|P|2.4\r" +
                            "PID|1||12345^^^Hospital^MR||^John^A||19800101|M|||123 Main St^^Metropolis^NY^12345||(555)555-1234|||M|||12345^^^Hospital^MR\r" +
                            "PV1|1|I|W^101^1^A|";

        List<String> validationErrors = validateHL7Message(hl7Message);

        if (validationErrors.isEmpty()) {
            System.out.println("Message is valid.");
        } else {
            System.out.println("Message has validation errors:");
            for (String error : validationErrors) {
                System.out.println(" - " + error);
            }
        }
    }

    public static List<String> validateHL7Message(String message) {
        List<String> errors = new ArrayList<>();

        // Split message into segments
        String[] segments = message.split("\r");

        // Check MSH segment
        if (segments.length == 0 || !segments[0].startsWith("MSH")) {
            errors.add("Missing or invalid MSH segment.");
            return errors; // Stop further validation if MSH is missing
        }

        // Validate MSH fields
        String[] mshFields = segments[0].split("\\|");
        if (mshFields.length < 12) {
            errors.add("MSH segment has fewer than 12 fields.");
        }
        if (mshFields.length > 9 && mshFields[9].isEmpty()) {
            errors.add("MSH-10 (Message Control ID) is missing.");
        }

        // Check for PID segment
        boolean pidFound = false;
        for (String segment : segments) {
            if (segment.startsWith("PID")) {
                pidFound = true;

                // Validate PID fields
                String[] pidFields = segment.split("\\|");
                if (pidFields.length < 3) {
                    errors.add("PID segment has fewer than 3 fields.");
                }
                if (pidFields.length > 5 && pidFields[5].isEmpty()) {
                    errors.add("PID-5 (Patient Name) is missing.");
                }
                break;
            }
        }
        if (!pidFound) {
            errors.add("Missing PID segment.");
        }

        // Additional checks can be added for other segments like PV1, etc.

        return errors;
    }
}
