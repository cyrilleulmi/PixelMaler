package ch.appquest.pixelmaler;

import android.animation.ArgbEvaluator;
import android.graphics.Paint;

/**
 * Created by cyrilleulmi on 12/9/2014.
 */
public class PaintMapToJsonConverter {
    public static String ParseMapToJson(Paint[][] colorMap){
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for(int y = 0; y < colorMap.length; y++){
            for (int x = 0; x < colorMap[0].length; x++){
                String colorString = colorMap[x][y] != null ? Integer.toHexString(colorMap[x][y].getColor()): "FFFFFFFF";
                String formattedColorString = colorString.toLowerCase().replaceFirst("ff", "") + "FF";

                sb.append("{\"y\":\"" + y + "\",\"x\":\"" + x + "\",color\":\"" + formattedColorString + "\"}");

                if (y != colorMap.length - 1 || x != colorMap[0].length - 1){
                    sb.append(",\n");
                }
                else{
                    sb.append("\n");
                }
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
