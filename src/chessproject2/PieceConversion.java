
package chessproject2;

/**
 * Just used as a way to convert array 00 to chess notation a1
 * 
 */
public class PieceConversion {

    //Converts chess notation to index
   public static int[] chessNotationToIndex(String pos) {
    if (pos == null || pos.length() != 2) return null;

    pos = pos.toLowerCase();
    char file = pos.charAt(0);      // a-h
    int rank = Character.getNumericValue(pos.charAt(1)); // 1-8

    if (file < 'a' || file > 'h') return null;
    if (rank < 1 || rank > 8) return null;

    int col = file - 'a';           // a->0, b->1, ..., h->7
    int row = rank - 1;             // 1->0, 2->1, ..., 8->7

    return new int[]{row, col};
}
   
   //Convert index back to chess notation
   public static String indexToChessNotation(int[] index)
   {
       if (index == null || index.length != 2) return null;
       
       int row = index[0];
       int col = index[1];
       
       if(row < 0 || row > 7 || col < 0 || col > 7) return null;
       
       char file = (char) ('a' + col);
       int rank = row + 1;
       
       return "" + file + rank;
   }
}