package viewComposer;

public class TimeScheduleView {

    // 画面を作るためのメソッド

     // 50 のセルがある 未定のセルが2つ  24時間を半分にした 30分刻みのセルが 48つある
// インスタンス化しなくても呼び出せるように static 静的メソッドクラスメソッドとして定義した
    public static  String[] createHalfTime() {
        String[] timeArray = new String[50];
        String strTime = "";
        for(int i = -2; i < 48; i++) {  // iが負の時は、時間未定のセルを作る
            if(i == -2 || i == -1) {
                strTime = "時間未定";
            } else if (i >= 0 && i < 48) {
                if(i % 2 == 0) {
                    strTime = Integer.toString(i) + ":00";
                } else {
                    strTime = Integer.toString(i) + ":30";
                }
            }
            timeArray[i + 2] = strTime;
        }
        return timeArray;
    }



}
