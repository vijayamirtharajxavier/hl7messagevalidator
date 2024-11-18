package com.axana.classes;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.segment.AIL;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.OBR;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.model.v23.segment.ORC;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v23.segment.PV1;
import ca.uhn.hl7v2.model.v23.segment.RGS;
import ca.uhn.hl7v2.model.v23.segment.SCH;





public class SegmentValidation {



    public static List<String> MSH(Message message,String msgEvent) throws HL7Exception  {
                List<String> errors = new ArrayList<>();

/***
    MSH-1: Field Separator (ST)
    MSH-2: Encoding Characters (ST)
    MSH-3: Sending Application (HD) optional
    MSH-4: Sending Facility (HD) optional
    MSH-5: Receiving Application (HD) optional
    MSH-6: Receiving Facility (HD) optional
    MSH-7: Date / Time of Message (TS) optional
    MSH-8: Security (ST) optional
    MSH-9: Message Type (CM_MSG)
    MSH-10: Message Control ID (ST)
    MSH-11: Processing ID (PT)
    MSH-12: Version ID (ID)
    MSH-13: Sequence Number (NM) optional
    MSH-14: Continuation Pointer (ST) optional
    MSH-15: Accept Acknowledgement Type (ID) optional
    MSH-16: Application Acknowledgement Type (ID) optional
    MSH-17: Country Code (ID) optional
    MSH-18: Character Set (ID) optional
    MSH-19: Principal Language of Message (CE) optional
 ***/

       try {

           // Extract MSH segment
            MSH msh = (MSH) message.get("MSH");
            String sendingApp = msh.getSendingApplication().getNamespaceID().getValue();
            String receivingApp = msh.getReceivingApplication().getNamespaceID().getValue();
            String msgtimestamp = msh.getDateTimeOfMessage().getTs1_TimeOfAnEvent().getValue();
            String msgType = msh.getMessageType().getCm_msg1_MessageType().getValue();
            String msgTriggerEvent = msh.getMessageType().getCm_msg2_TriggerEvent().getValue();
            String msgControlId = msh.getMessageControlID().getValue();
            String msgVersionId = msh.getVersionID().getValue();
            String msgProcessingId = msh.getProcessingID().encode();

            // Validate MSH fields
            if (isEmpty(sendingApp)) {
                errors.add("MSH-3 (Sending Application) is missing.");
            }
            if (isEmpty(receivingApp)) {
                errors.add("MSH-5 (Receiving Application) is missing.");
            }
            if (isEmpty(msgtimestamp)) {
                errors.add("MSH-7 (Date/Time of Message) is missing.");
            }
            if (isEmpty(msgType)) {
                errors.add("MSH-9-1 (Message Type) is missing.");
            }
            if (isEmpty(msgTriggerEvent)) {
                errors.add("MSH-9-2 (Message Event Trigger) is missing.");
            }
            if (isEmpty(msgControlId)) {
                errors.add("MSH-10 (Message Control Id) is missing.");
            }
            if (isEmpty(msgProcessingId)) {
                errors.add("MSH-11 (Message Processing Id) is missing.");
            }
            if (isEmpty(msgVersionId)) {
                errors.add("MSH-12 (Message Version Id) is missing.");
            }
        }
        catch (HL7Exception e)
        {
            errors.add("Error extracting or validating segments: " + e.getMessage());

        }
            return errors;
    }


    public static List<String> PID(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
    /**
     *  PID-1: Set ID - Patient ID (SI) optional
        PID-2: Patient ID (External ID) (CX) optional
        PID-3: Patient ID (Internal ID) (CX) repeating
        PID-4: Alternate Patient ID (CX) optional
        PID-5: Patient Name (XPN) repeating
        PID-6: Mother's Maiden Name (XPN) optional
        PID-7: Date of Birth (TS) optional
        PID-8: Sex (IS) optional
        PID-9: Patient Alias (XPN) optional repeating
        PID-10: Race (IS) optional
        PID-11: Patient Address (XAD) optional repeating
        PID-12: County Code (IS) optional
        PID-13: Phone Number - Home (XTN) optional repeating
        PID-14: Phone Number - Business (XTN) optional repeating
        PID-15: Primary Language (CE) optional
        PID-16: Marital Status (IS) optional repeating
        PID-17: Religion (IS) optional
        PID-18: Patient Account Number (CX) optional
        PID-19: SSN Number - Patient (ST) optional
        PID-20: Driver's License Number (DLN) optional
        PID-21: Mother's Identifier (CX) optional
        PID-22: Ethnic Group (IS) optional
        PID-23: Birth Place (ST) optional
        PID-24: Multiple Birth Indicator (ID) optional
        PID-25: Birth Order (NM) optional
        PID-26: Citizenship (IS) optional
        PID-27: Veterans Military Status (CE) optional
        PID-28: Nationality Code (CE) optional
        PID-29: Patient Death Date and Time (TS) optional
        PID-30: Patient Death Indicator (ID) optional
 **/
    
    
        PID pid = (PID) message.get("PID");
    
        if (message.get("PID") != null) {
                

            // Validate PID fields
            String patientID = pid.getPatientIDInternalID(0).getID().getValue(); // PID-3.1
            String patientFName = pid.getPatientName(0).getFamilyName().getValue(); // PID-3.1

            if (isEmpty(patientID))
            {
                errors.add("PID-3 (Patient ID) is missing.");
            }

            if (isEmpty(patientFName)) {
                errors.add("PID-5.1 (Patient Family Name) is missing.");
            }
            if (isEmpty(pid.getPatientName(0).getGivenName().getValue())) {
                errors.add("PID-5.2 (Patient Given Name) is missing.");
            }
            if (isEmpty(pid.getSex().getValue())) {
                errors.add("PID-8 (Administrative Sex) is missing.");
            }
        } else {
            errors.add("Missing PID segment.");
        }

        return errors;
    }

    public static List<String> PV1(Message message,String msgEvent) throws HL7Exception  {
        /***
         *  PV1-1: Set ID - Patient Visit (SI) optional
            PV1-2: Patient Class (ID)
            PV1-3: Assigned Patient Location (PL) optional
            PV1-4: Admission Type (ID) optional
            PV1-5: Preadmit Number (CX) optional
            PV1-6: Prior Patient Location (PL) optional
            PV1-7: Attending Doctor (XCN) optional repeating
            PV1-8: Referring Doctor (XCN) optional repeating
            PV1-9: Consulting Doctor (XCN) optional repeating
            PV1-10: Hospital Service (ID) optional
            PV1-11: Temporary Location (PL) optional
            PV1-12: Preadmit Test Indicator (ID) optional
            PV1-13: Readmission Indicator (ID) optional
            PV1-14: Admit Source (ID) optional
            PV1-15: Ambulatory Status (IS) optional repeating
            PV1-16: VIP Indicator (ID) optional
            PV1-17: Admitting Doctor (XCN) optional repeating
            PV1-18: Patient Type (ID) optional
            PV1-19: Visit Number (CX) optional
            PV1-20: Financial Class (FC) optional repeating
            PV1-21: Charge Price Indicator (ID) optional
            PV1-22: Courtesy Code (ID) optional
            PV1-23: Credit Rating (ID) optional
            PV1-24: Contract Code (ID) optional repeating
            PV1-25: Contract Effective Date (DT) optional repeating
            PV1-26: Contract Amount (NM) optional repeating
            PV1-27: Contract Period (NM) optional repeating
            PV1-28: Interest Code (ID) optional
            PV1-29: Transfer to Bad Debt Code (ID) optional
            PV1-30: Transfer to Bad Debt Date (DT) optional
            PV1-31: Bad Debt Agency Code (ID) optional
            PV1-32: Bad Debt Transfer Amount (NM) optional
            PV1-33: Bad Debt Recovery Amount (NM) optional
            PV1-34: Delete Account Indicator (ID) optional
            PV1-35: Delete Account Date (DT) optional
            PV1-36: Discharge Disposition (ID) optional
            PV1-37: Discharged to Location (CM_DLD) optional
            PV1-38: Diet Type (ID) optional
            PV1-39: Servicing Facility (ID) optional
            PV1-40: Bed Status (IS) optional
            PV1-41: Account Status (ID) optional
            PV1-42: Pending Location (PL) optional
            PV1-43: Prior Temporary Location (PL) optional
            PV1-44: Admit Date/Time (TS) optional
            PV1-45: Discharge Date/Time (TS) optional
            PV1-46: Current Patient Balance (NM) optional
            PV1-47: Total Charges (NM) optional
            PV1-48: Total Adjustments (NM) optional
            PV1-49: Total Payments (NM) optional
            PV1-50: Alternate Visit ID (CX) optional
            PV1-51: Visit Indicator (IS) optional
            PV1-52: Other Healthcare Provider (XCN) optional repeating
****/
        
        List<String> errors = new ArrayList<>();
        if (message.get("PV1") != null) {
            PV1 pv1 = (PV1) message.get("PV1");
            String patientClass  = pv1.getPatientClass().getValue();
            String visitNumber = pv1.getVisitNumber().getName();
            
            if (isEmpty(patientClass)) {
                errors.add("PV1-2 (Patient Class) is missing.");
            }
            if (isEmpty(visitNumber)) {
                errors.add("PV1-19 (Visit Number) is missing.");
            }


        }

    
    
        return errors;
    }

    public static List<String> SCH(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
/******
 *  SCH-1: Placer Appointment ID (EI)
    SCH-2: Filler Appointment ID (EI) optional
    SCH-3: Occurrence Number (NM) optional
    SCH-4: Placer Group Number (EI) optional
    SCH-5: Schedule ID (CE) optional
    SCH-6: Event Reason (CE)
    SCH-7: Appointment Reason (CE) optional
    SCH-8: Appointment Type (CE) optional
    SCH-9: Appointment Duration (NM) optional
    SCH-10: Appointment Duration Units (CE) optional
    SCH-11: Appointment Timing Quantity (TQ) repeating
    SCH-12: Placer Contact Person (XCN) optional
    SCH-13: Placer Contact Phone Number (XTN) optional
    SCH-14: Placer Contact Address (XAD) optional
    SCH-15: Placer Contact Location (PL) optional
    SCH-16: Filler Contact Person (XCN)
    SCH-17: Filler Contact Phone Number (XTN) optional
    SCH-18: Filler Contact Address (XAD) optional
    SCH-19: Filler Contact Location (PL) optional
    SCH-20: Entered By Person (XCN)
    SCH-21: Entered By Phone Number (XTN) optional repeating
    SCH-22: Entered By Location (PL) optional
    SCH-23: Parent Placer Appointment ID (EI) optional
    SCH-24: Parent Filler Appointment ID (EI) optional
    SCH-25: Filler Status Code (CE) optional
 */
        if (message.get("SCH") != null) {
            SCH sch = (SCH) message.get("SCH");

            String placerAppointmentId  = sch.getPlacerAppointmentID().getUniversalID().getValue();
            
            
            if (isEmpty(placerAppointmentId)) {
                errors.add("SHI-1 (Placer Appointment Id) is missing.");
            }
        }

        return errors;
    }

    //RGS
    public static List<String> RGS(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("RGS") != null) {
            RGS rgs = (RGS) message.get("RGS");
            
        }

        return errors;
    }
//AIL

public static List<String> AIL(Message message,String msgEvent) throws HL7Exception  {
    List<String> errors = new ArrayList<>();
    if (message.get("AIL") != null) {
        AIL ail = (AIL) message.get("AIL");
        
    }

    return errors;
}


    public static List<String> OBR(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("OBR") != null) {
            OBR obr = (OBR) message.get("OBR");
            
        }

        return errors;
    }
    public static List<String> OBX(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("OBX") != null) {
            OBX obx = (OBX) message.get("OBX");
            
        }

        return errors;
    }

    public static List<String> MFE(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("MFE") != null) {
            ORC obr = (ORC) message.get("ORC");        
        }

        return errors;
    }
    public static List<String> ORC(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("ORC") != null) {
            ORC orc = (ORC) message.get("ORC");
            }

            return errors;
        }


    public static List<String> DFT(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("DFT") != null) {
        }

        return errors;
    }

    public static List<String> RDE(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("RDE") != null) {
        }

        return errors;
    }

    public static List<String> MFN(Message message,String msgEvent) throws HL7Exception  {
        List<String> errors = new ArrayList<>();
        if (message.get("MFN") != null) {
        }

        return errors;
    }



    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

}
