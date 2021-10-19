package viewComposer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

public class TimeScheduleView {

    // 画面を作るためのメソッド
    public static LinkedList<String> makeTimeStack() {
        LinkedList<String> timeStack = new LinkedList<String>(); // 格納順を記憶する LinkedList順番を保持する
        double doubleNumber = 0.0;
        String zero = "";
        String half = "";
        for (doubleNumber = 0.0; doubleNumber < 24.0; doubleNumber = doubleNumber + 0.5) {

            //   System.out.println(doubleNumber);
            // BigDecimal クラスを使用すると、丸めの振る舞いを完全に制御できます。このクラスは算術、スケール操作、丸め、比較、ハッシュ、そしてフォーマット変換のための操作も提供します。
            // 浮動小数点数の整数部と小数部を取得するには
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(doubleNumber));
            //   System.out.println(bigDecimal.toPlainString());

            int intValue = bigDecimal.intValue();
            //    System.out.println("Integer Part: " + intValue);
            //   System.out.println("Decimal Part: " + bigDecimal.subtract(new BigDecimal(intValue)).toPlainString());
            String deci = bigDecimal.subtract(new BigDecimal(intValue)).toPlainString();
            if (deci.equals("0.0")) {
                // System.out.println("0.0です");
                zero = intValue + ":00";
                timeStack.push(zero);
            } else if (deci.equals("0.5")) {
                // System.out.println("0.5です");
                half = intValue + ":30";
                timeStack.push(half);
            }
        }

        // 末尾に空文字追加した
        timeStack.add("");
    //	System.out.println(stack.toString());
        Collections.reverse(timeStack);

      System.out.println(timeStack.toString());  // [0:00, 0:30, 1:00, 1:30, 2:00, 2:30, 3:00, 3:30, 4:00, 4:30, 5:00, 5:30, 6:00, 6:30, 7:00, 7:30, 8:00, 8:30, 9:00, 9:30, 10:00, 10:30, 11:00, 11:30, 12:00, 12:30, 13:00, 13:30, 14:00, 14:30, 15:00, 15:30, 16:00, 16:30, 17:00, 17:30, 18:00, 18:30, 19:00, 19:30, 20:00, 20:30, 21:00, 21:30, 22:00, 22:30, 23:00, 23:30]
        return timeStack;
    }

    //開始時間から終了時間までの、timeStackに含まれているリストを取得するメソッド
//    public static int rows(List<String> timeStack, String strStartTime, String strEndTime){
//
//        int startIndex = timeStack.indexOf(strStartTime);
//        int endIndex = timeStack.indexOf(strEndTime);
//       //  containList = timeStack.subList(startIndex, endIndex + 1);
//       return endIndex - startIndex;
//    }




}
