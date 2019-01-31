package org.jcryptool.analysis.fleissner.logic;

import java.math.BigInteger;

//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.concurrent.ThreadLocalRandom;

//import java.util.ArrayList;

public class Test {

  /**
   * @param args
   */
  public static void main(String[] args) {
      // TODO Auto-generated method stub
      
//    for (int i=0;i<args.length;i++) {
//        
//        System.out.println(args[i]);
//    }
//    String blubbb = "12,hallo,156,witz";
//    String[] data = blubbb.split(",");
//    
//    for (int i=0;i<data.length;i++) {
//        System.out.println(data[i]);
//    }
//    
//    int templatelength = 0;
//    int holes = 100;
//    int isSquare=holes;
//    int j = 0;
//
//    do {
//        isSquare-= (2*j+1);
//        j++;
//    }
//    while(isSquare>0);
//    
//    if (isSquare==0) {
//        templatelength =(int) Math.sqrt(4*holes);
//    }
//    else {
//        templatelength = (int) Math.sqrt((4*holes)+1);
//    }
//    
//    System.out.println("templateLength: "+templatelength);
      
//    String bla = "hier nicht";
      
//    switch(bla) {
//    
//    case "hier": System.out.println("blabla"); break;
//    case "da": System.out.println("nicht hier"); break;
//    case "deshalb": System.out.println("du bist dumm"); break;
//    default: System.out.println("keine gültige eingabe"); break;
//    }
      
//    int templateLength = 4;
////      int holes = 1;
//    int[] grille = {1,2,2,2};
////      
//    CryptedText ct = new CryptedText();
//    String alphabet = "abcdefghijklmnopqrstuvwxyzäöüß";
//    String bigAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß";
//    String text = "DawkinskommtzumSchlussdassesunmöglichseidiebehaupteteExistenzeinesHöherenWesenszubeweisenoderzuwiderlegendaNichtexistenznichtbewiesenwerdenkönneHierführterdasGedankenexperimentvonBertrandRussellaneineTeekannediemöglicherweiseimAllkreiseaberprinzipiellauchmitdenbestenTeleskopennichtzuentdeckenseiMitGottseieswiemitihrzwarvorstellbaraberwedernachweisbarnochwiderlegbarIneinemsolchenFalllägedieBeweislastaberbeidenendiedieExistenzderTeekanneoderdieeinesGottesbehauptenwürdenMitRücksichtaufdielogischeSchwierigkeiteinesprinzipiellenBeweisesderNichtExistenzirgendeinesWesensvertrittDawkinsdaherdievorsichtigeThesedassGottmitziemlicherSicherheitnichtexistiertDawkinsattackiertdabeidieGotteshypothesemitgenaujenemTypvonArgumentdasvontheologischerSeitelangeZeitgegendieVorstellungunerschaffenerLebensformeninsFeldgeführtwurdeDieExistenzsolcherLebensformenmüsseerklärtwerdenihrebloßzufälligeExistenzseiunplausibelFürdenBereichderBiologiewurdedasArgumententkräftetunddieEntstehungvonLebewesenmitkomplexenhochfunktionellenStrukturenalsnatürlichesPhänomenimRahmenderEvolutionstheorieverstandenBeiderFragenachderExistenzGottesabergreiftdieserTypvonUnwahrscheinlichkeitsargumentsoDawkinsDieExistenzeinesGottesseimindestenssounwahrscheinlichwiedieExistenzeinerunerschaffenenreinzufälligentstandenenBoeing"; 
//    char[] c = text.toCharArray();
//    String newText = "";
//    String punctuation = ",.:;-_";
//    ct.load("HALLODUWURSTICHBINDUMM", true, 3, grille);
//    System.out.println("INFO(toString):\n"+ct.toString());
//    System.out.println("INFO(object):\n"+ct);
      
//    for (int k=0; k<text.length();k++) {
//        for (int n=0; n<punctuation.length();n++) {
//            if (text.charAt(k)!=punctuation.charAt(n)) {
//                newText += text.charAt(k);
//            }
//        }
//    }
//    text = newText;
//    newText = "";
//    
//    for (int i = 0; i<c.length;i++) {
//        for (int j=0; j<alphabet.length();j++) {
//            if (c[i]==alphabet.charAt(j)) {
//                c[i] = bigAlphabet.charAt(j);
//            }
//        }
//        newText += c[i];
//    }
//    System.out.println(newText);
//    
//    double k = Double.MAX_VALUE;
//    BigInteger i = new BigInteger("0");
//    BigInteger j = new BigInteger("0");
//    int k = i.compareTo(j);
//    System.out.println("i gleich j: "+k);
//    
//    for (int n=2;n<=12;n++) {
//        int h=(int) (Math.pow(n, 2))/4;
//        double omega = Math.pow(4, h);
//        System.out.println("n: "+n+", h: "+h+", |Ω|: "+omega );
//        
//    }

//    try {
//        Path sourceFile = Paths.get("C:/Users/Dinah/Documents/Uni/MeineVeranstaltungen/BA-Projektarbeit/Parameter.csv");    ///*newText*/ change this
//        BufferedReader brFile = Files.newBufferedReader(sourceFile, Charset.forName("Cp1252"));

//        BufferedReader reader=new BufferedReader(new FileReader("Parameter.csv"));
      
          //Skips to first real value, ignoring additional data in csv file
//        String line;
//        line = reader.readLine();
//        templateLength = Integer.parseInt(line);
//        line = reader.readLine();
//        
//        
//    
//        //reads whole csv file
//        do{
//            
//            line=reader.readLine();
//            System.out.println("TemplateLength: "+templateLength+", Line: "+line);
//        }while(Character.getNumericValue(line.charAt(0)) !=templateLength);
//        
//        if (templateLength%2==0) {
//            holes = (int) (Math.pow(templateLength, 2))/4;
//        }
//        else {
//            holes = (int) (Math.pow(templateLength, 2)-1)/4;
//        }
//        
//        grille = new int[2*holes];
//        String[] data=line.split(",");
//    
//        for(int i=1; i<data.length;i++){
//            
//            grille[i-1]=(Integer.parseInt(data[i]));
//        }
//        
//        for (int j=0;j<grille.length;j++){
//            System.out.println("grille("+j+"): "+String.valueOf(grille[j]));
//        }
//            
//        reader.close();
//    } catch (Exception e) {
//        System.out.println("An error occured during file handling: " + e);
//        e.printStackTrace();
//    }
      
      String abc = "SDAZWSHUMESCSHSKEUIILNSUKDNMISÖKSGDEOBEMHAAMLUICTNPTZEWEEIENESSITSEEEHEXÖKNNSOHZIEURDSERTZUEEBWEWNNIDIEINCHCTEHXWRETLRIEGSTDBEETWEEINNNKÖDNNZAEESENPHIAEEESGREDIARRTMFRNÜHKEAENNETRNVEDTRUESSXROENBDCLLAAHLNNEEDRILNKWEREINMMEEIIÖSEGELSTEAEBEIEIRMAKTPRUIENCHNMITTNNIEZCDIPEFHLETNSIBKEZEUELNTSLODPEAIECOKTSTTISEHITEERNLESESULZWBWAIIREAMRAIBEMTVRORGRWESDLEBAERNGOMESBROCNAHGLARCWICINDHHENWFAEEELINIILLIÄDESLEASNTXGIEESADIBOTNDEEIEREBNBZDEEREWDTIEESEEIKBREEEINHEDAEANNSNEGMMUPIOTOTETTDRÜECKERNSWÜDRICGHIPISGCHKERTIEANSUFCRZITIHEDWIIPIIEELLELNEESOZNBNEIWICRHTGEEWSEEEXISIONNDSSEETIEVSERDTRNENIESRTTTEDISRDGIEEVGAOTWTOKIRFTHEMSSNIECISTZDIEHADMASHSLIHCTSEIITNEIAHTRETCRSHUATDCTAIEWXKCIEHRTIEKDINRTABSEGYHYEPONTPIVADOHIEEVNUJASEGENMROGUTMEITENMTEETDOALGGIASCNHESNGVDEONRDIEZESETEIIVHORESTTOTEGELSLLAUFLFFOENREDNGMGERUNLYFENÜEIEBNEHRRTSWUNCSRFEHNDEZDSESOFLCOHEIRREKEEXRVLMEÄLNIEMBRSTWTEREEÜDSSNEENFINSÄLZLISGIHBEREEEBEWLIUFXNLIPSÜORDßENTZLBAUUSERLEAKOGRIEGWRIÄUCFUHDRHTMEEDNEETDTRUNBDDAIEINTOIEEONTONLKEBOECTHMSFWTEEJUPLNSEHEXNKUTINONMGEENHVCLLEEHINAELSSNMNRPSAATRTXHHÄMÜNUROLEKNDTERIUMEENREVOOLNERIBEVEENUAITCRIOSXHDEDTRNAFNESRETXIDHRSAGEETEENRHRGTREYIRZSPGCFOTTKHVOEDNTIUEIENLSICSANHWABEKETIEESOXDAIWSTGSSOKARIPTTETNNGSZDEUSSMEIIEEMINNIINNDCSWAHHRWSTEEISNCTEHKZEDEEINIENISNESRULOENXIUEERRSNBEITNZSUOCETHIFAFÄENANGLDFLEIEEVONCWGENNENN";
      System.out.println("Länge: "+abc.length());
  }
}

