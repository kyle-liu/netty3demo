package time;

import java.util.Date;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: PM1:45
 */
public class UnixTime {

    private final int value;

    public UnixTime(int value) {

        this.value =value;
    }

    public  int getValue() {
        return  value;
    }

    @Override
    public  String toString() {
        return "Unix Time : " +  new Date(value*1000L).toString();
    }
}
