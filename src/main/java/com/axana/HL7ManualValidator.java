package com.axana;
import java.util.ArrayList;
import java.util.List;

import com.axana.classes.SegmentValidation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.parser.PipeParser;

public class HL7ManualValidator {

    public static void main(String[] args) {
        String hl7Message = "MSH|^~\\&|SendingApp||ReceivingApp|ReceivingFacility||12312001141506|ADT^A01|123456|P|2.3\r" +
                            "PID|1||12345^^^Hospital^MR||Doe^John^A||19800101|M|||123 Main St^^Metropolis^NY^12345||(555)555-1234|||M|||12345^^^Hospital^MR\r" +
                            "PV1|1|I|W^101^1^A|";

        try {
            // Parse the message
            PipeParser parser = new PipeParser();
            Message parsedMessage = parser.parse(hl7Message);

            // Validate the message
            List<String> validationErrors = validateHL7Message(parsedMessage);

            // Output validation results
            if (validationErrors.isEmpty()) {
                System.out.println("Message is valid.");
            } else {
                System.out.println("Message has validation errors:");
                for (String error : validationErrors) {
                    System.out.println(" - " + error);
                }
            }
        } catch (HL7Exception e) {
            System.err.println("Error parsing HL7 message: " + e.getMessage());
        }
    }

    public static List<String> validateHL7Message(Message message) throws HL7Exception {
        List<String> errors = new ArrayList<>();
        String msgType="";
        String msgEvent="";
            MSH msh = (MSH) message.get("MSH");
            if(msh==null)
            {
                errors.add("MSH Segment is null.");

            }
            else
            {
                msgType = msh.getMessageType().getCm_msg1_MessageType().getValue();
                msgEvent = msh.getMessageType().getCm_msg2_TriggerEvent().getValue();
    
            }

        try {
            if(msgType.equals("ADT"))
            {
                if(msgEvent.equals("A01"))
                {
                    errors = SegmentValidation.MSH(message,msgEvent);
                    errors = SegmentValidation.PID(message,msgEvent);
                    errors = SegmentValidation.PV1(message,msgEvent);


                }

                if(msgEvent.equals("A02"))
                {
                    errors = SegmentValidation.MSH(message,msgEvent);
                    errors = SegmentValidation.PID(message,msgEvent);
                    errors = SegmentValidation.PV1(message,msgEvent);


                }

                if(msgEvent.equals("A03"))
                {
                    errors = SegmentValidation.MSH(message,msgEvent);
                    errors = SegmentValidation.PID(message,msgEvent);
                    errors = SegmentValidation.PV1(message,msgEvent);


                }

                if(msgEvent.equals("A04"))
                {
                    errors = SegmentValidation.MSH(message,msgEvent);
                    errors = SegmentValidation.PID(message,msgEvent);

                }




            }

            //SIU
            if(msgType.equals("SIU"))
            {
                if(msgEvent.equals("S12"))
                {
            /*****
             *  MSH	Message header	Required
                SCH	Schedule activity information	Required
                PID	Patient identification	Required
                RGS	Resource group	Required
                AIL	Appointment location	Required
            */

                    errors = SegmentValidation.MSH(message,msgEvent);
                    errors = SegmentValidation.SCH(message,msgEvent);
                    errors = SegmentValidation.PID(message,msgEvent);
                    errors = SegmentValidation.RGS(message,msgEvent);
                    errors = SegmentValidation.AIL(message,msgEvent);

                }
            }



            //ORU
            if(msgType.equals("ORU"))
            {
                if(msgEvent.equals("R01"))
                {
                    /****
                     *  MSH	Message header	Required
                        PID	Patient identification	Required
                        ORC	Common order	Required
                        OBR	Observation request	Required
                        OBX	Observation/result	Required
                    ***/
                    errors = SegmentValidation.MSH(message,msgEvent);
                    errors = SegmentValidation.PID(message,msgEvent);
                    errors = SegmentValidation.ORC(message,msgEvent);
                    errors = SegmentValidation.OBR(message,msgEvent);
                    errors = SegmentValidation.OBX(message,msgEvent);


                }
            }
           //ORM
           if(msgType.equals("ORM"))
           {
               if(msgEvent.equals("O01"))
               {
                   /****
                    *   MSH	Message header	Required
                        PID	Patient identification	Required
                        ORC	Common order	Required
                   ***/
                   errors = SegmentValidation.MSH(message,msgEvent);
                   errors = SegmentValidation.PID(message,msgEvent);
                   errors = SegmentValidation.ORC(message,msgEvent);


               }
           }

        }


catch (HL7Exception e) {
            errors.add("Error extracting or validating segments: " + e.getMessage());
        }

        return errors;
    }

}
