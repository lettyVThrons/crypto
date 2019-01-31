package org.jcryptool.analysis.fleissner.logic;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.math.BigInteger;


/**
 * @author Dinah
 *
 */
public class MethodApplication{

    private static final Logger log = Logger.getLogger( FleissnerGrilleSolver.class.getName() );

//  parameters given by user or default
    private String method, decryptedText, encryptedText, textInLine, language, statistics;
    private int templateLength, holes, nGramSize;
    private int[] grille;
    private BigInteger restart;
    private boolean isPlaintext;
    private CryptedText ct;
    private FleissnerGrille fg;
    private TextValuator tv;
    private long start, end;
    
//  parameters for analysis
    private double value = Double.MAX_VALUE, oldValue = Double.MAX_VALUE, alltimeLow=Double.MAX_VALUE, min = Double.MAX_VALUE;
    private int changes=0, iAll=0, grilleNumber=0, improvement = 0, minX=0, minY=0, minMove=0; 
    private int x,y,move;
    private int[] bestTemplate=null;
    private String lastImprovement = null, bestDecryptedText = "", procedure = "";
    
    public MethodApplication(ParameterSettings ps) {
        // applies parameter settings from ParameterSettings and sets and executes method
        this.method = ps.getMethod();
        this.encryptedText = ps.getEncryptedText();
        this.textInLine = ps.getTextInLine();
        this.templateLength = ps.getTemplateLength();
        this.isPlaintext = ps.isPlaintext();
        this.holes = ps.getHoles();
        this.grille = ps.getGrille();
        this.restart = ps.getRestart(); 
        this.language = ps.getLanguage();
        this.statistics = ps.getStatistics();
        this.nGramSize = ps.getnGramSize();
        this.ct = new CryptedText();
        this.fg = new FleissnerGrille(templateLength);
        if (method.equals("analyze")) {
            this.tv = new TextValuator(statistics, language, nGramSize);
            if (this.grille == null) {
                // Build a random grille
                log.info("Grillvalue: "+String.valueOf(grille)+", \nrandom grille will be created");
                this.grille = new int[2*holes]; 
                fg.clearGrille();
                String key = "";
                
                for(int i=0; i<holes; i++)
                {
                    do
                    {
                        x = ThreadLocalRandom.current().nextInt(0, templateLength);
                        y = ThreadLocalRandom.current().nextInt(0, templateLength);
                    }
                    while (!fg.isPossible(x, y));

                fg.setState(x, y, true);
                grille[2*i] = x;
                key += x+",";
                grille[(2*i)+1] = y;
                key += y+",";
                }
                log.info("random grille: "+key+" created with\nTextInLine:\n"+textInLine+"\nisPlaintext: "+isPlaintext+"\nkeyLength: "+templateLength);
            }
            ct.load(textInLine, isPlaintext, templateLength, grille, fg);
        }
    }       
    
    public void analyze () {
        
//      Executes one of the methods Brute-Force or Hill-Climbing dependent on templateLength
        log.info("INFO:\n"+ct.toString());
        start = System.currentTimeMillis();
        log.info("Start analysis ");
        
        if (templateLength<5){
//          Brute Force for up to 4 x 4 grilles. Creates all possibles grilles and evaluates each text decrypted by them
            procedure = "Brute-Force";
            log.info("Start "+procedure);
            this.bruteForce();
            log.info("\nFinished "+procedure);
        }
        else {
//          Hill-Cimbing for templates from 5 x 5. creates random grilles and evaluates those and slightly variations of them before
//          trying new random grilles.
            procedure = "Hill-Climbing";
            log.info("Start "+procedure);
            this.hillClimbing();
            log.info("\nFinished "+procedure);
        }
        end = System.currentTimeMillis() - start; 
    }
    
    public void bruteForce() {

//      clears grilles in FleissnerGrille from potential earlier encryptions before building new ones
        fg.clearGrille();
        ArrayList<int[]> templateList = fg.bruteForce(templateLength, holes);
        if (templateList.isEmpty()) {
            log.info("templateList is empty");
            return;
        }
        log.info("sift through all templates");
        // using every possible template / Grille
        for (int[] template : templateList) {
                
            iAll++;
            fg.useTemplate(template, templateLength);
            decryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(decryptedText);
            if (value < alltimeLow)
            {
//              better grille found, saves new template
                alltimeLow = value;
                bestTemplate = template;
                grilleNumber = iAll;
            }   
            log.info("\n\nGrille: "+iAll+fg);
            log.info("Accurateness: " + value + " (best: "+alltimeLow+")");
            log.info("Decrypted text:\n ==> "+decryptedText+"\n");
        }
        fg.useTemplate(bestTemplate, templateLength);   
    }
    
    public void hillClimbing() {

        do {
//          clears grilles in FleissnerGrille from potential earlier encryptions before building a random new one
            fg.clearGrille();
            for(int i=0; i<holes; i++)
            {
                do
                {
                    x = ThreadLocalRandom.current().nextInt(0, templateLength);
                    y = ThreadLocalRandom.current().nextInt(0, templateLength);
                }
                while (!fg.isPossible(x, y));

            fg.setState(x, y, true);
            }
            log.info("Restart: "+restart+", fleissnergrille to string (random): "+fg+"");
            decryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(decryptedText);
            log.info("Decrypted Text: "+decryptedText+"\nwith value: "+String.valueOf(value));
            
            do{
                iAll++;
                // calculate the best possible solution if only changing one of the cells
                for (x=0; x<templateLength; x++)
                {
                    for (y=0; y<templateLength; y++)
                    {
                        if (fg.isFilled(x, y))
                        {
                            for (move=0; move<=3; move++)
                            {
                                fg.change(x, y, move);
                                decryptedText = fg.decryptText(ct.getText());
                                value = tv.evaluate(decryptedText);
                                fg.undoChange(x, y, move);
                                
                                if (value < min)
                                {
                                    min = value;
                                    minX = x;
                                    minY = y;
                                    minMove = move;
                                }   
                            }
                        }
                    }
                }

                if (min < oldValue)
                {
                    // we found a better solution by changing one of the cells
                    // go on with this new solution
                    fg.change(minX, minY, minMove);
                    oldValue = min;
                    decryptedText = fg.decryptText(ct.getText());
                    improvement++;
                    if (oldValue<alltimeLow) 
                    {
                        alltimeLow=oldValue;
                        grilleNumber=iAll;
                        bestDecryptedText = fg.decryptText(ct.getText());
                        bestTemplate = fg.saveTemplate(holes);
                        lastImprovement = String.valueOf(restart);
                        changes++;
                        log.info("best grille yet with "+changes+" changes, where value is " +alltimeLow+"\n"+fg);
                    }
                    log.info("try: " + iAll + ", changes: "+changes + " (last at: " + grilleNumber + "in restart: "+restart+"), accurateness: " + min + " (best: "+oldValue+", alltime: "+alltimeLow+")");
                    log.info("==> "+decryptedText+"\n Grille: \n"+fg);
                }
            } while (Math.abs(iAll-improvement)<1);
            restart = restart.subtract(BigInteger.valueOf(1));
            improvement = 0;
            iAll = 0;
            
        }while(restart.compareTo(BigInteger.valueOf(0))==1);
        
        fg.useTemplate(bestTemplate, templateLength);
        minMove = 0;
//      checks all 4 rotation positions of the found grille
        for (move=1; move<=4; move++)
        {
            fg.rotate();
            decryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(decryptedText);
                        
            if (value < alltimeLow)
            {
                alltimeLow = value;
                minMove = move;
                improvement++;
            }                           
        }
        if (improvement != 0) {
            
            log.info("Improvement by "+minMove+" rotation(s).");        
            for (int moves = 1 ; moves <= minMove; moves++) {
                fg.rotate();
            }
            bestTemplate = fg.saveTemplate(holes);
        }
    }
    
//  encrypts given plaintext with given key directly through load method of class CryptedText
    public void encrypt(){

        ct.load(textInLine, isPlaintext, templateLength, grille, fg);
        encryptedText = "";
        for(char[][]textPart:ct.getText()) {
            for (int y = 0; y < templateLength; y++) {
                for (int x = 0; x < templateLength; x++) {
                    encryptedText += textPart[x][y];
                }
            }
        }
    }
    
//  decrypts given crypted text with given key by decryption method of class FleissnerGrille
    public void decrypt(){

        ct.load(textInLine, isPlaintext, templateLength, grille, fg);
        fg.useTemplate(grille, templateLength);
        decryptedText = fg.decryptText(ct.getText());
        log.info("Crypted text succesfully decrypted");
    }
    
    public void keyGenerator() {
        this.grille = new int[2*holes]; 
        fg.clearGrille();
        
        for(int i=0; i<holes; i++)
        {
            do
            {
                x = ThreadLocalRandom.current().nextInt(0, templateLength);
                y = ThreadLocalRandom.current().nextInt(0, templateLength);
            }
            while (!fg.isPossible(x, y));

            fg.setState(x, y, true);
            grille[2*i] = x;
            grille[(2*i)+1] = y;
        }
    }
    
    /**
     * @return the fg
     */
    public FleissnerGrille getFg() {
        return fg;
    }

    /**
     * @param fg the fg to set
     */
    public void setFg(FleissnerGrille fg) {
        this.fg = fg;
    }

    public String toString() {
        
        String output= null;
        String bestTemplateCoordinates = "";
        
        switch(this.method) {
        
        case "analyze":
            bestDecryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(bestDecryptedText);
            for (int i = 0; i<bestTemplate.length;i++) {
                bestTemplateCoordinates+=bestTemplate[i];
            }
            output = "\nBest grille: "+bestTemplateCoordinates+" with length: "+templateLength+" found at try: "+grilleNumber+", in Restart: "+lastImprovement+fg;
            output += "\nDecrpyted text:\n\n"+bestDecryptedText+"\n\n";
            output += "Procedure: "+procedure+"\n";
            output += "Accurateness: " + value+" (where alltimelow is "+alltimeLow+ ")\n";
            output += "Finished analysis in "+end+" miliseconds";
            break;
        case "encrypt":
            output = "\nEncrypted text:\n\n"+encryptedText+",\nencrypted with key:"+fg;
            for (int i = 0; i<grille.length;i++) {
                bestTemplateCoordinates+=grille[i];
            }
            output += "\nKey coordinates: "+bestTemplateCoordinates;
            break;
        case "decrypt":
            output = "\nDecrypted text:\n\n"+decryptedText+", decryption with key:"+fg;
            for (int i = 0; i<grille.length;i++) {
                bestTemplateCoordinates+=grille[i];
            }
            output += "\nKey coordinates: "+bestTemplateCoordinates;
            break;
        case "keyGenerator":
            output = "Key: ";
            for (int h=0;h<grille.length;h++) {
                output += grille[h]+",";
            }
            output += "\nwith length: "+templateLength;
            fg.clearGrille();
        }
        return output;
    }
}
