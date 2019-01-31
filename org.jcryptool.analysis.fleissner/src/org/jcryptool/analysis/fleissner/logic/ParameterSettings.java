package org.jcryptool.analysis.fleissner.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * @author Dinah
 *
 */
public class ParameterSettings {

    private static final Logger log = Logger.getLogger( FleissnerGrilleSolver.class.getName() );
    
    private String decryptedText;
    private String encryptedText = null;
    private String textInLine = null;
    private String method = null;
    private int templateLength=0;
    private boolean isPlaintext = false;
    private int holes = 0;
    private int[] grille = null;
    private BigInteger restart = new BigInteger("0");
    private String statistics = null;
    private String language = null;
    private int nGramSize = 0;
    
//  Sets all parameters and method to be applied. Checks, if combinations of given parameters are useful to start a method.
//  Sets default values to non given parameters, if useful and throws Exception else.
    public ParameterSettings(String[] args) throws InvalidParameterCombinationException{
        // TODO Auto-generated constructor stub
        
        try {
            for (int i=0;i<args.length;i++) {
                
                String arg = args[i];
                
                switch (arg) {
                
                case "-method":
                    if ((args[i+1].equals("encrypt"))||(args[i+1].equals("analyze"))||(args[i+1].equals("decrypt")) ||(args[i+1].equals("keyGenerator"))) {
                        method = args[i+1];
                        log.info("method: "+method);
                    }
                    else {
//                      there is no other method deposited
                        throw new InvalidParameterCombinationException("method has to be analyze, encrypt, decrypt or keyGenerator");
                    }
                    break;
                case "-plaintext":
//                  sets plaintext, if given
                    isPlaintext = true;
                    textInLine = args[i+1];
                    log.info("Text type: plaintext\nText: "+textInLine);
                    break;
                case "-dataPlaintext":
//                  sets plaintext from file, if given
                    isPlaintext = true;
                    try {
                        String source = args[i+1];
                        Path sourceFile = Paths.get(source);
                        BufferedReader brFile = Files.newBufferedReader(sourceFile, Charset.forName("Cp1252"));
                        textInLine=brFile.readLine();
                                
                        brFile.close();
                        log.info("Text type: plaintext from file\nText: "+textInLine);
                        
                        } catch (Exception e) {
                            log.severe("An error occured during file handling: " + e);
                            e.printStackTrace();
                        }
                    break;
                case "-cryptedText":
//                  sets crypted text, if given
                    isPlaintext = false;
                    textInLine = args[i+1];
                    log.info("Text type: crypted text\nText: "+textInLine);
                    break;
                case "-dataCryptedText":
//                  sets crypted text from file, if given
                    isPlaintext = false;
                    try {
                        Path sourceFile = Paths.get(args[i+1]);
                        BufferedReader brFile = Files.newBufferedReader(sourceFile, Charset.forName("Cp1252"));
                        textInLine=brFile.readLine();
                                
                        brFile.close();
                        log.info("Text type: crypted text from file\nText: "+textInLine);
                        
                        } catch (Exception e) {
//                          System.out.println("An error occured during file handling: " + e);
                            log.severe("An error occured during file handling: " + e);
                            e.printStackTrace();
                        }   
                    break;
                case "-restarts":
//                  sets restarts, if given
                    if (Integer.parseInt(args[i+1])>0) {
                        restart = BigInteger.valueOf(Integer.parseInt(args[i+1]));
                        log.info("restarts: "+String.valueOf(restart));
                    }
                    else {
//                      there has to be at least one restart
                        throw new InvalidParameterCombinationException("Restarts has to be an integer greater or equal to 1");
                    }
                    break;
                case "-keyLength":
//                  sets key length, if given
                    if (Integer.parseInt(args[i+1])<2) {
//                      there is no valid key with length less than 2
                        throw new InvalidParameterCombinationException("Length of grille has to be an integer greater or equal to 2");
                    }
                    else {
                        templateLength = Integer.parseInt(args[i+1]);
                        if (templateLength%2==0) {
                            holes = (int) (Math.pow(templateLength, 2))/4;
                        }
                        else {
                            holes = (int) (Math.pow(templateLength, 2)-1)/4;
                        }
                        log.info("keylength: "+templateLength+"\nholes: "+holes);
                    }
                    break;
                case "-key":
//                  sets key in the following shape (example for 3x3 grille): 0,0,2,1 
//                  for two holes at coordinates (0,0) and (2,1)
//                  and calulates and sets key length and holes
                    try {
                        String template = args[i+1];
                        String[] data;
    
                        data=template.split(",");
                        grille = new int[data.length];
                        if (grille.length%2!=0) {
                            throw new InvalidParameterCombinationException("Invalid key length");
                        }
                        holes = ((grille.length)/2);
                        int isSquare=holes;
                        int j = 0;

                        do {
                            isSquare-= (2*j+1);
                            j++;
                        }
                        while(isSquare>0);
                        
                        if (isSquare==0) {
                            templateLength =(int) Math.sqrt(4*holes);
                        }
                        else {
                            templateLength = (int) Math.sqrt((4*holes)+1);
                        }
                        
                        for (int k=0;k<data.length;k++) {
                            grille[k]= Integer.parseInt(data[k]);
                        }
                        log.info("key: "+template+"\nkeylength: "+String.valueOf(templateLength)+"\nholes: "+String.valueOf(holes));
                                                            
                    } catch (Exception e) {
                        log.severe("An error occured during parameter handling: " + e);
                        e.printStackTrace();
                    }   
                    break;
                case "-statistics":     
//                  sets statistics, if given
                    statistics = args[i+1];
                    break;
                case "-language":
//                  sets language, if given
                    if ((args[i+1].equals("german"))||(args[i+1].equals("english"))) {
                        language = args[i+1];
                        log.info("language: "+language);
                    }
                    else {
//                      currently there is no other alphabet deposited
                        throw new InvalidParameterCombinationException("language has to be english or german");
                    }
                    break;
                case "-nGramSize":
//                  there are just functioning tri- and quadgrams deposited
                    if ((Integer.parseInt(args[i+1])<3) || (Integer.parseInt(args[i+1])>4)) {
                        throw new InvalidParameterCombinationException("Please enter a tri- or quadgram");
                    }
                    else {
//                      sets ngram, if given
                        nGramSize = Integer.parseInt(args[i+1]);
                        log.info("Ngram size: "+nGramSize);
                    }
                    break;
                }
            }
        }catch (Exception e) {
            log.severe("An error occured during file handling: " + e);
            e.printStackTrace();
        }
//      sets default method as analyze as this is the main function of the program
        if (method == null) {
            method = "analyze";
            log.info("default method: "+method);
        }
//      there has to be a grille for mere encryption
        if ((method.equals("encrypt")) && (grille == null)) {
            throw new InvalidParameterCombinationException("Please choose a template for encyrption");
        }
//      there has to be a plaintext to encrypt
        else if ((method.equals("encrypt")) && (textInLine==null)) {
            throw new InvalidParameterCombinationException("Please enter a plaintext for encyrption");
        }
//      there has to be a grille for mere decryption
        if ((method.equals("decrypt")) && (grille == null)) {
            throw new InvalidParameterCombinationException("Please choose a template for decryption");
        }
//      there has to be a crypted text to decrypt
        else if ((method.equals("decrypt")) && (textInLine==null)) {
            throw new InvalidParameterCombinationException("Please enter a crypted text for decryption");
        }
        if (method.equals("analyze")) {
//          if a text statistic is given as parameter, the parameters language and nGram size are mandatory
            if ((statistics!=null) && ((language == null) || (nGramSize == 0))) {
                throw new InvalidParameterCombinationException("Please enter language and Ngram size for your statistic");
            }
//          sets default language as german
            if (language == null) {
                language = "german";
                log.info("default language: german");
            }
//          sets default nGram size to 4
            if (nGramSize == 0) {
                nGramSize = 4;
                log.info("default nGram size: "+nGramSize);
            }
//          sets deposited statistic for given nGram size and language
            if ((nGramSize!=0) && (statistics == null)) {
                if (language.equals("german")){
                    switch(nGramSize) {
                    case 3:
                        statistics = "de-3gram-nocs.bin";
                        break;
                    case 4:
                        statistics = "de-4gram-nocs.bin";
                        break;
                    }
                }
                else if (language.equals("english")) {
                    switch(nGramSize) {
                    case 3:
                        statistics = "en-3gram-nocs.bin";
                        break;
                    case 4:
                        statistics = "en-4gram-nocs.bin";
                        break;
                    }
                }
                log.info("Default Ngram: "+statistics+"  with N = "+nGramSize+" in language: "+language+" has been choosen.");
            }
//          sets default plaintext, if there is no given
            if (textInLine == null) {
                isPlaintext = true;
                try {
                    String sourceFile;
                    if (language.equals("german")) {
                        sourceFile = "exampleGer.txt";
                        log.info("Default german plaintext has been choosen");
                    }else {
                        sourceFile = "exampleEng.txt";
                        log.info("Default english plaintext has been choosen");
                    }
                    InputStreamReader res = new InputStreamReader(getClass().getResourceAsStream(sourceFile));
                    BufferedReader brFile = new BufferedReader(res);
                    textInLine=brFile.readLine();
                        
                    brFile.close();
                    log.info("Default plaintext loaded");
                
                } catch (Exception e) {
                    log.severe("An error occured during file handling: " + e);
                    e.printStackTrace();
                }
            }
        }

//      sets random key length and calculates holes
        if (templateLength==0) {
            
            templateLength = ThreadLocalRandom.current().nextInt(2, 21);
            
            if (templateLength%2==0) {
                holes = (int) (Math.pow(templateLength, 2))/4;
            }
            else {
                holes = (int) (Math.pow(templateLength, 2)-1)/4;
            }
            log.info("random keyLength: "+templateLength+" with "+holes+" holes");
        }
//      sets default restarts to 50, if hill climbing is the analyzing method
        if ((restart.equals(BigInteger.valueOf(0))) && (templateLength > 4) && (method.equals("analyze"))) {

            restart = BigInteger.valueOf(50);
            log.info("default restarts: "+String.valueOf(restart));
        }
    }

    /**
     * @return the decryptedText
     */
    public String getDecryptedText() {
        return decryptedText;
    }

    /**
     * @param decryptedText the decryptedText to set
     */
    public void setDecryptedText(String decryptedText) {
        this.decryptedText = decryptedText;
    }

    /**
     * @return the encryptedText
     */
    public String getEncryptedText() {
        return encryptedText;
    }

    /**
     * @param encryptedText the encryptedText to set
     */
    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }

    /**
     * @return the textInLine
     */
    public String getTextInLine() {
        return textInLine;
    }

    /**
     * @param textInLine the textInLine to set
     */
    public void setTextInLine(String textInLine) {
        this.textInLine = textInLine;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the templateLength
     */
    public int getTemplateLength() {
        return templateLength;
    }

    /**
     * @param templateLength the templateLength to set
     */
    public void setTemplateLength(int templateLength) {
        this.templateLength = templateLength;
    }

    /**
     * @return the isPlaintext
     */
    public boolean isPlaintext() {
        return isPlaintext;
    }

    /**
     * @param isPlaintext the isPlaintext to set
     */
    public void setPlaintext(boolean isPlaintext) {
        this.isPlaintext = isPlaintext;
    }

    /**
     * @return the holes
     */
    public int getHoles() {
        return holes;
    }

    /**
     * @param holes the holes to set
     */
    public void setHoles(int holes) {
        this.holes = holes;
    }

    /**
     * @return the grille
     */
    public int[] getGrille() {
        return grille;
    }

    /**
     * @param grille the grille to set
     */
    public void setGrille(int[] grille) {
        this.grille = grille;
    }

    /**
     * @return the restart
     */
    public BigInteger getRestart() {
        return restart;
    }

    /**
     * @param restart the restart to set
     */
    public void setRestart(BigInteger restart) {
        this.restart = restart;
    }

    /**
     * @return the statistics
     */
    public String getStatistics() {
        return statistics;
    }

    /**
     * @param statistics the statistics to set
     */
    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the nGramSize
     */
    public int getnGramSize() {
        return nGramSize;
    }

    /**
     * @param nGramSize the nGramSize to set
     */
    public void setnGramSize(int nGramSize) {
        this.nGramSize = nGramSize;
    }
}
