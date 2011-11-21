package com.libs.utils;

import java.util.Calendar;
import java.util.Date;

import com.libs.R;

import android.app.Activity;
import android.content.res.Resources;
import android.text.format.Time;

/***
 * Usage
 *   LunarCalendar lc = new LunarCalendar();
 *   lc.set(year, month, day);              //eg:2010-12-31
 *   String lunarDate = lc.getLunarMonthString() 
 *   			+ lc.getLunarDayString();   //eg:九月十六
 *   String festival = getLocalFestival();  //eg:国庆节
 *   String animal = lc.AnimalsYear(year)；  //eg:虎
 *   String t = lc.cyclical(year);          //eg:庚寅
 *   
 *
 * by zhenyu
 * version 1.0
 *
 ***/

public class LunarCalendar {
    public int lyear;

    public int lmonth;
    public String clmonth;
    public int lday;

    public boolean leap;
    public String chinesemonth;
    public String solarTerms;

    public int yearCyl, monCyl, dayCyl;

    public String qingming;
    public String chineseSolarFestival;
    public String chineseLunarFestival;
    public String twSolarFestival;
    public String twLunarFestival;
    public String chAndTwFestival;
    public String lunarFestival;
    public String twlunarFestival;
    public String usFestival;
    public String usFestival1;
    public String usFestival2;
    protected Resources mResources = null;
    Activity mActivity;
    private Calendar baseDate = Calendar.getInstance();

    public static Calendar offDate = Calendar.getInstance();

    final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260,
            0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250,
            0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
            0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,
            0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0,
            0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
            0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
            0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950,
            0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558,
            0x0b540, 0x0b5a0, 0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
            0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50,
            0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0, 0x0da50,
            0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4,
            0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
            0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, 0x05aa0,
            0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
            0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
            0x0ada0 };

    String[] Gan;

    String[] Zhi;

    String[] Animals;

    String[] SolarTerm;
    String[] chineseTen;

    final static long[] STermInfo = new long[] { 0, 21208, 42467, 63836, 85337, 107014, 128867,
            150921, 173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494,
            397447, 419210, 440795, 462224, 483532, 504758 };

    public static String chineseMonthNumber[];

    String[] sFtv;
    String[] twsFtv;

    String[] twlFtv;

    String[] lFtv;

    String[] wFtv;

    String[] usextra;
    String[] usFtv;
    String[] uswFtv;
    
    String[] solarFtvIW;  // For IW
    String[] lunarFtvIW;

    public LunarCalendar() {
        baseDate.setMinimalDaysInFirstWeek(7);//
    }

    public LunarCalendar(Activity activity) {
        this.mActivity = activity;
        baseDate.setMinimalDaysInFirstWeek(7);//
        init();
    }

    private void init() {

        Gan = mActivity.getResources().getStringArray(R.array.fih_calendar_Gan_txt);

        Zhi = mActivity.getResources().getStringArray(R.array.fih_calendar_Zhi_txt);

        Animals = mActivity.getResources().getStringArray(R.array.fih_calendar_Animals_txt);

        SolarTerm = mActivity.getResources().getStringArray(R.array.fih_calendar_SolarTerm_txt);

        chineseMonthNumber = mActivity.getResources().getStringArray(
                R.array.fih_calendar_chineseMonthNumber_txt);

        sFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_sFtv_txt);
        twsFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_twsFtv_txt);

        twlFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_twlFtv_txt);

        lFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_lFtv_txt);

        wFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_wFtv_txt);
        chineseTen = mActivity.getResources().getStringArray(R.array.fih_calendar_chineseTen_txt);

        usFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_usFtv_txt);
        uswFtv = mActivity.getResources().getStringArray(R.array.fih_calendar_uswFtv_txt);
        usextra = mActivity.getResources().getStringArray(R.array.fih_calendar_usextra_txt);
        
        solarFtvIW = mActivity.getResources().getStringArray(R.array.solar_festival_txt_for_IW);
        lunarFtvIW = mActivity.getResources().getStringArray(R.array.lunar_festival_txt_for_IW);
        
        
    }

    // The total days of the lunar year
    final private static int lYearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0) sum += 1;
        }
        return (sum + leapDays(y));
    }

    // The days of leap month
    final private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            if ((lunarInfo[y - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;

        } else
            return 0;
    }

    // The numbers of leap month in the year
    // Return 0, if there is no leap month in the year
    final private static int leapMonth(int y) {
        return (int) (lunarInfo[y - 1900] & 0xf);

    }

    // The days of the months of the year
    final public static int monthDays(int y, int m)// 
    {
        if ((lunarInfo[y - 1900] & (0x10000) >> m) == 0)
            return 29;
        else
            return 30;
    }

    // The animals of the year
    public String AnimalsYear(int y) {
        return Animals[(y - 4) % 12];
    }

    public String cyclical(int num) {
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    final private int sTerm(int y, int n)// 
    {
        offDate.set(1900, 0, 6, 2, 5, 0);
        long temp = offDate.getTime().getTime();
        offDate.setTime(new Date(
                (long) ((31556925974.7 * (y - 1900) + STermInfo[n] * 60000L) + temp)));
        return offDate.get(Calendar.DAY_OF_MONTH);
    }

    public void CalculateLunarCalendar(int y, int m, int d)// 
    {
        int leapMonth = 0;

        try {

            baseDate.set(1900, 0, 31);

        } catch (Exception e) {
            e.printStackTrace();
        }
        long base = baseDate.getTimeInMillis();

        try {

            baseDate.set(y, m - 1, d);

        } catch (Exception e) {
            e.printStackTrace();
        }
        long obj = baseDate.getTimeInMillis();

        int offset = (int) ((obj - base) / 86400000L); // The offset from 1900.1.31
        dayCyl = offset + 40;
        monCyl = 14;

        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = lYearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset <= 0) {
            offset += daysOfYear; // The offset of current year
            iYear--;
            monCyl -= 12;

        }

        lyear = iYear; // The lunar year number

        yearCyl = iYear - 1864;// 

        leapMonth = leapMonth(iYear); // 
        leap = false;

        // 
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            //
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(iYear);
            } else
                daysOfMonth = monthDays(iYear, iMonth);

            offset -= daysOfMonth;
            // 
            if (leap && iMonth == (leapMonth + 1)) leap = false;
            if (!leap) monCyl++;
        }
        // 
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        // 
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        lmonth = iMonth;
        if (lmonth == 13) {
            lmonth = 1;
            lyear++;
        }

        chinesemonth = chineseMonthNumber[lmonth - 1];
        lday = offset + 1;

        if (d == sTerm(y, (m - 1) * 2))
            solarTerms = SolarTerm[(m - 1) * 2];
        else if (d == sTerm(y, (m - 1) * 2 + 1))
            solarTerms = SolarTerm[(m - 1) * 2 + 1];
        else
            solarTerms = "";

        // chinese Festivals
        for (int i = 0; i < sFtv.length; i++) {
            if (Integer.parseInt(sFtv[i].substring(0, 2)) == m
                    && Integer.parseInt(sFtv[i].substring(2, 4)) == d) {
                chineseSolarFestival = sFtv[i].substring(5);

            }
        }

        if (d == sTerm(y, 6)) {
            qingming = mActivity.getResources().getString(R.string.fih_calendar_qingming_txt);
        } else {
            qingming = "";
        }

        // Taiwan festivals
        for (int i = 0; i < twsFtv.length; i++) {
            if (Integer.parseInt(twsFtv[i].substring(0, 2)) == m
                    && Integer.parseInt(twsFtv[i].substring(2, 4)) == d) {
                twSolarFestival = twsFtv[i].substring(5);
            }
        }

        // chinese
        for (int i = 0; i < lFtv.length; i++) {
            if (Integer.parseInt(lFtv[i].substring(0, 2)) == lmonth
                    && Integer.parseInt(lFtv[i].substring(2, 4)) == lday) {
                chineseLunarFestival = lFtv[i].substring(5);
            }
        }

        // Taiwan
        for (int i = 0; i < twlFtv.length; i++) {
            if (Integer.parseInt(twlFtv[i].substring(0, 2)) == lmonth
                    && Integer.parseInt(twlFtv[i].substring(2, 4)) == lday) {
                twLunarFestival = twlFtv[i].substring(5);
            }
        }

        // chinese and Taiwan
        for (int i = 0; i < wFtv.length; i++) {
            Time t = new Time();
            t.set(baseDate.getTimeInMillis());
            t.monthDay = 1;

            Calendar tt = Calendar.getInstance();
            tt.setTimeInMillis(t.normalize(true));
            if (tt.get(Calendar.DAY_OF_WEEK) <= baseDate.get(Calendar.DAY_OF_WEEK)) {

                if (Integer.parseInt(wFtv[i].substring(0, 2)) == m
                        && Integer.parseInt(wFtv[i].substring(2, 3)) == baseDate
                                .get(Calendar.WEEK_OF_MONTH)
                        && Integer.parseInt(wFtv[i].substring(3, 4)) == baseDate
                                .get(Calendar.DAY_OF_WEEK)) {
                    chAndTwFestival = wFtv[i].substring(5);
                }
            } else {
                if (Integer.parseInt(wFtv[i].substring(0, 2)) == m
                        && Integer.parseInt(wFtv[i].substring(2, 3)) == (baseDate
                                .get(Calendar.WEEK_OF_MONTH) - 1)
                        && Integer.parseInt(wFtv[i].substring(3, 4)) == baseDate
                                .get(Calendar.DAY_OF_WEEK)) {
                    chAndTwFestival = wFtv[i].substring(5);
                }
            }

        }

        // usFestival
        for (int i = 0; i < usFtv.length; i++) {
            if (Integer.parseInt(usFtv[i].substring(0, 2)) == m
                    && Integer.parseInt(usFtv[i].substring(2, 4)) == d) {
                usFestival = usFtv[i].substring(5);

            }
        }
        // usFestivals
        for (int i = 0; i < uswFtv.length; i++) {
            Time t = new Time();
            t.set(baseDate.getTimeInMillis());
            t.monthDay = 1;

            Calendar tt = Calendar.getInstance();
            tt.setTimeInMillis(t.normalize(true));
            if (tt.get(Calendar.DAY_OF_WEEK) <= baseDate.get(Calendar.DAY_OF_WEEK)) {

                if (Integer.parseInt(uswFtv[i].substring(0, 2)) == m
                        && Integer.parseInt(uswFtv[i].substring(2, 3)) == baseDate
                                .get(Calendar.WEEK_OF_MONTH)
                        && Integer.parseInt(uswFtv[i].substring(3, 4)) == baseDate
                                .get(Calendar.DAY_OF_WEEK)) {
                    usFestival1 = uswFtv[i].substring(5);
                }
            } else {
                if (Integer.parseInt(uswFtv[i].substring(0, 2)) == m
                        && Integer.parseInt(uswFtv[i].substring(2, 3)) == (baseDate
                                .get(Calendar.WEEK_OF_MONTH) - 1)
                        && Integer.parseInt(uswFtv[i].substring(3, 4)) == baseDate
                                .get(Calendar.DAY_OF_WEEK)) {
                    usFestival1 = uswFtv[i].substring(5);
                }
            }

        }

        // us Easter Sunday

        int baseY = baseDate.get(Calendar.YEAR);
        y = 2007;
        int n1 = baseY - 1900;
        int a1 = n1 % 19;
        int q1 = (int) Math.floor(n1 / 4);
        int b1 = (int) Math.floor((7 * a1 + 1) / 19);
        int m1 = (11 * a1 + 4 - b1) % 29;
        int w1 = (n1 + q1 + 31 - m1) % 7;
        int d1 = 25 - m1 - w1;
        Time easterSunday = new Time();
        easterSunday.set(baseDate.getTimeInMillis());

        if (d1 >= 0) {
            easterSunday.month = 4;
            easterSunday.monthDay = d1;

        } else {
            easterSunday.month = 3;
            easterSunday.monthDay = 31 - d1;

        }
        easterSunday.normalize(true);

        Time GoodFriday = easterSunday;
        GoodFriday.monthDay = GoodFriday.monthDay - 2;
        GoodFriday.normalize(true);

        if (baseDate.get(Calendar.DAY_OF_MONTH) == easterSunday.monthDay
                && baseDate.get(Calendar.MONTH) == easterSunday.month) {
            usFestival2 = usextra[1];
        }
        if (baseDate.get(Calendar.DAY_OF_MONTH) == GoodFriday.monthDay
                && baseDate.get(Calendar.MONTH) == GoodFriday.month) {
            usFestival2 = usextra[0];

        }

        // usThansGiving
        Time Thanksgiving = new Time();
        Thanksgiving.year = baseDate.get(Calendar.YEAR);
        Thanksgiving.month = 11;
        Thanksgiving.monthDay = 1;
        Thanksgiving.normalize(true);
        if (Thanksgiving.weekDay <= 4)
            Thanksgiving.monthDay = Thanksgiving.monthDay - 2 - Thanksgiving.weekDay - 1;
        else
            Thanksgiving.monthDay = Thanksgiving.monthDay - Thanksgiving.weekDay + 4;

        Thanksgiving.normalize(true);
        if (baseDate.get(Calendar.DAY_OF_MONTH) == Thanksgiving.monthDay
                && baseDate.get(Calendar.MONTH) == Thanksgiving.month) {
            usFestival2 = usextra[2];
        }

    }

    public void CalculateLunarCalendarIW(int y, int m, int d)// 
    {
        int leapMonth = 0;

        try {
            baseDate.set(1900, 0, 31);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long base = baseDate.getTimeInMillis();

        try {
            baseDate.set(y, m - 1, d);

        } catch (Exception e) {
            e.printStackTrace();
        }
        long obj = baseDate.getTimeInMillis();

        int offset = (int) ((obj - base) / 86400000L); // The offset from 1900.1.31
        dayCyl = offset + 40;
        monCyl = 14;

        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = lYearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset <= 0) {
            offset += daysOfYear; // The offset of current year
            iYear--;
            monCyl -= 12;

        }

        lyear = iYear; // The lunar year number

        yearCyl = iYear - 1864;// 

        leapMonth = leapMonth(iYear); // 
        leap = false;

        // 
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            //
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(iYear);
            } else
                daysOfMonth = monthDays(iYear, iMonth);

            offset -= daysOfMonth;
            // 
            if (leap && iMonth == (leapMonth + 1)) leap = false;
            if (!leap) monCyl++;
        }
        // 
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        // 
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        lmonth = iMonth;
        if (lmonth == 13) {
            lmonth = 1;
            lyear++;
        }

        chinesemonth = chineseMonthNumber[lmonth - 1];
        lday = offset + 1;
        
        solarTerms = "";
        chineseLunarFestival = "";
        chineseSolarFestival = "";
        
        
        // Check 24 solar terms
        if (d == sTerm(y, (m - 1) * 2))
            solarTerms = SolarTerm[(m - 1) * 2];
        else if (d == sTerm(y, (m - 1) * 2 + 1))
            solarTerms = SolarTerm[(m - 1) * 2 + 1];
        else
            solarTerms = "";
        
        if(!solarTerms.equals("")){
            return;    
        }
        
        // Check solar festivals
        for (int i = 0; i < solarFtvIW.length; i++) {
            if (Integer.parseInt(solarFtvIW[i].substring(0, 2)) == m
                    && Integer.parseInt(solarFtvIW[i].substring(2, 4)) == d) {
                chineseSolarFestival = solarFtvIW[i].substring(5);
            }
        }
        
        if(!chineseSolarFestival.equals("")){
            return;    
        }
        
        // Check lunar festivals
        for (int i = 0; i < lunarFtvIW.length; i++) {
            if (Integer.parseInt(lunarFtvIW[i].substring(0, 2)) == lmonth
                    && Integer.parseInt(lunarFtvIW[i].substring(2, 4)) == lday) {
                chineseLunarFestival = lunarFtvIW[i].substring(5);
            }
        }

    }

    public void set(int y, int m, int d) {
        // CalculateLunarCalendar(y, m, d);
        CalculateLunarCalendarIW(y, m, d);
    }

    public void set(Calendar cal) {
        //CalculateLunarCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal
        //        .get(Calendar.DAY_OF_MONTH));
        CalculateLunarCalendarIW(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal
                .get(Calendar.DAY_OF_MONTH));
    }

    public static LunarCalendar getInstance() {

        return new LunarCalendar();
    }

    public String getLunarDayString() {
        if (lday > 30 || lday < 1)
            return "";
        else
            return chineseTen[lday - 1];
    }

    public String getLunarMonthString() {

        if (lmonth > 12 || lmonth < 1)
            return "";
        else
            return chineseMonthNumber[lmonth - 1];
    }

    public String getLocalFestival() {
    	String showSolar = "";
        if (solarTerms != null && !solarTerms.equals("")) {
            showSolar = solarTerms;
        } else if(chineseSolarFestival != null && !chineseSolarFestival.equals("")) {
            showSolar = chineseSolarFestival;
        } else if(chineseLunarFestival != null && !chineseLunarFestival.equals("")) {
            showSolar = chineseLunarFestival;
        }
        return showSolar;
    }
}
