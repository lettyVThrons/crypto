package org.jcryptool.analysis.fleissner.logic;

import java.util.logging.Logger;

public class FleissnerGrilleSolver {
    
    private static final Logger log = Logger.getLogger( FleissnerGrilleSolver.class.getName() );
    
    public static void main(String[] args) {
        
      args = new String[6];
      args[0] = "-method";
      args[1] = "encrypt";
      args[2] = "-key";
      args[3] = "1,0,1,6,4,2,4,5,6,6,2,0,0,3,1,5,1,3,3,4,1,2,4,0";
      args[4] = "-dataCryptedText";
      args[5] = "example2Ger.txt";

//      assign users values to parameters
        try {
//          Configuration of given parameters and selecting and applying one of the three methods
            ParameterSettings ps = new ParameterSettings(args);
            MethodApplication ma = new MethodApplication(ps);
            String method = ps.getMethod();
            
            switch(method) {
                case "analyze": ma.analyze();
                                break;
                case "encrypt": ma.encrypt();
                                break;
                case "decrypt": ma.decrypt();
                                break;
                case "keyGenerator": ma.keyGenerator();
                                break;
            }
            
            log.info("To String method of Method Application in fgSolver: \n"+ma.toString());
        } catch (InvalidParameterCombinationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
