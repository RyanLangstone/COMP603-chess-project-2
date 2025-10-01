
package chessproject2;


import static chessproject2.Game.chars;

/**
 * Creates an array of the border to be displayed on the output
 * 
 */
public class GetBorder {
     public static String[] getborder() {
        String[] BorderLeftFrame = {chars.get("BL"), chars.get("LM"), chars.get("LM"), chars.get("LM"), chars.get("LM"), chars.get("LM"), chars.get("LM"), chars.get("LM"), chars.get("TL")};
        String[] borderMiddleFrame = {chars.get("BM"), chars.get("MM"), chars.get("MM"), chars.get("MM"), chars.get("MM"), chars.get("MM"), chars.get("MM"), chars.get("MM"), chars.get("TM")};
        String[] borderRightFrame = {chars.get("BR"), chars.get("RM"), chars.get("RM"), chars.get("RM"), chars.get("RM"), chars.get("RM"), chars.get("RM"), chars.get("RM"), chars.get("TR")};

        String[] borderString = new String[9];

        for (int row = 0; row < 9; row++) {
            borderString[row] = BorderLeftFrame[row];
            for (int column = 1; column <= 7; column++) {
                borderString[row] += chars.get("H");
                borderString[row] += chars.get("H");
                borderString[row] += chars.get("H");
                borderString[row] += borderMiddleFrame[row];
            }
            borderString[row] += chars.get("H");
            borderString[row] += chars.get("H");
            borderString[row] += chars.get("H");
            borderString[row] += borderRightFrame[row];
        }
        return borderString;
    }
}
