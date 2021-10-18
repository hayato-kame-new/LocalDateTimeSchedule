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
    //	System.out.println(stack.toString());
        Collections.reverse(timeStack);
    //	System.out.println(stack.toString());
        return timeStack;
    }

}
