/**
 * This interface is used to define all Constants in Class SPAView.java.
 *
 * @author Biqiang Jiang
 *
 * @version 1.0, 01/09/09
 *
 * @since JDK1.5.7
 */

package org.jcryptool.visual.sidechannelattack.spa.views;

import org.jcryptool.visual.sidechannelattack.SPAPlugIn;

public interface Constants {
    static String pluginRootDirectory = SPAPlugIn.getDefault().getBundle().getLocation().substring(16);

	String INFORMATION = Messages.Constants_0;
	String RESULT = Messages.Constants_1;
	String MODE = Messages.Constants_10;
	String INPUT = Messages.Constants_100;

    String IMGADDRESSE_SQ_ENG = "icons/sam_Sq.jpg";
    String IMGADDRESSE_SQMUL_0_ENG = "icons/sam_SqMul_0.jpg";
    String IMGADRESSE_Y_ACHSE_ENG = "icons/achse_y.jpg";
    String IMGADDRESSE_X_ACHSE_ENG = "icons/achse_x.jpg";
    String IMGADDRESSE_SQMUL_ENG = "icons/sam_SqMul.jpg";

    String NOTE_RSA_VULNERABILITY = Messages.Constants_101;
            
    String DEC_ENG = Messages.Constants_102;
    String HIGHEST_BIT_ENG = Messages.Constants_104;
    String FINAL_RESULT_ENG = Messages.Constants_105;
    String INPUT_BASIS_ENG = Messages.Constants_106;
    String INPUT_RES_1_ENG = Messages.Constants_107;
    String RES_AFTER_SQUARE_ENG = Messages.Constants_108;
    String RES_AFTER_MUL_ENG = Messages.Constants_109;
    String EXP_ENG = Messages.Constants_11;
    String MODUL_ENG = Messages.Constants_110;
    String RES_EQUAL_ENG = Messages.Constants_111;
    String HOCH_2_MOD_ENG = Messages.Constants_112;
    String RSA_PROCESS_TEXT = Messages.Constants_113;
    String MAIN_GROUP_TITLE = Messages.Constants_114;
    String BASIS_LABEL = Messages.Constants_115;
    String EXPONENT_LABEL = Messages.Constants_116;
    String CHOOSE_Q_LABEL = Messages.Constants_117;
    String CHOOSE_P_LABEL = Messages.Constants_118;
    String FIRST_COLUMN_IN_TABLE = Messages.Constants_119;
    String SECOND_COLUMN_IN_TABLE = Messages.Constants_12;
    String THIRD_COLUMN_IN_TABLE = Messages.Constants_120;

    String INITIAL_ITEM_TEXT_1_IN_TABLE = Messages.Constants_121;
    String INITIAL_ITEM_TEXT_2_IN_TABLE = Messages.Constants_122;
    String INITIAL_ITEM_TEXT_3_IN_TABLE = Messages.Constants_123;
    String OUTPUT_TABLE_ITEM_TEXT = Messages.Constants_124;
    String INDICATION_OF_VULNERABILITY_TEXT = Messages.Constants_125;
    String EXECUTION_BUTTON_TEXT = Messages.Constants_126;
    String CLEAR_BUTTON_TEXT = Messages.Constants_127;
    String MODULE_LABEL_TEXT = Messages.Constants_128;

    String INITIAL_TABLE_ITEM_SQUARE = Messages.Constants_129;
    String INITIAL_TABLE_ITEM_MULTIPLY = Messages.Constants_13;
    String RES_SQUARE_MULTI_ALWAYS_SQUARE = Messages.Constants_130;
    String RES_SQUARE_MULTI_ALWAYS_MULTI = Messages.Constants_131;
    String INDICATION_SQUARE_MULTI_ALWAYS = Messages.Constants_132;
    String SQUARE_MULTI_ALWAYS_ALG_TEXT = Messages.Constants_133;

    String INFORMATION_SAM_TEXT = RSA_PROCESS_TEXT + "\n" + INDICATION_OF_VULNERABILITY_TEXT  + "\n" +  NOTE_RSA_VULNERABILITY;
    String INFORMATION_SAMA_TEXT = RSA_PROCESS_TEXT  + "\n" +  INDICATION_SQUARE_MULTI_ALWAYS  + "\n" +  NOTE_RSA_VULNERABILITY;
    String TOOL_TIP_TEXT_EXPONENT = Messages.Constants_134;
    String TOOL_TIP_TEXT_BASIS = Messages.Constants_135;
    String TOOL_TIP_TEXT_CLEARBUTTON = Messages.Constants_136;
    String TOOL_TIP_TEXT_RESULT_LABEL = Messages.Constants_137;
    String TOOL_TIP_TEXT_RESULT = Messages.Constants_138;
    String TOOL_TIP_TEXT_Q_SELECTION = Messages.Constants_139;
    String TOOL_TIP_TEXT_P_SELECTION = Messages.Constants_14;
    String TOOL_TIP_TEXT_MODULE_N = Messages.Constants_140;
    
    String SAM_MODE = Messages.Constants_141;
    String SAMA_MODE = Messages.Constants_142;

    String PARAMETER = Messages.Constants_143;

}
