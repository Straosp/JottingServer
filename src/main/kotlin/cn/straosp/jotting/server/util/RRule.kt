package cn.straosp.jotting.server.util

import java.util.regex.Pattern

object RRule {

    // RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU;BYHOUR=8,9;BYMINUTE=30
    private const val RRULE_START = "RRULE:"
    private const val FREQ = "FREQ"
    private val FREQ_VALUE = arrayOf("SECONDLY","MINUTELY","HOURLY","DAILY","WEEKLY","MONTHLY","YEARLY")
    private const val BY_MONTH = "BYMONTH"
    private const val BY_WEEK_NO = "BYWEEKNO"
    private const val BY_YEAR_DAY = "BYYEARDAY"
    private const val BY_MONTH_DAY = "BYMONTHDAY"
    private const val BY_DAY = "BYDAY"
    private const val BY_HOUR = "BYHOUR"
    private const val BY_MINUTE = "BYMINUTE"
    private const val BY_SECOND = "BYSECOND"

    fun verifyRRule(rrule: String){
        val basicPattern = Pattern.compile(
            "^RRULE:(FREQ=(SECONDLY|MINUTELY|HOURLY|DAILY|WEEKLY|MONTHLY|YEARLY);)?" +
                    "(INTERVAL=(\\d+);)?" +
                    "(COUNT=(\\d+);)?" +
                    "(UNTIL=(\\d{8}T\\d{6}Z|\\d{8}T\\d{6}(\\+|-)\\d{2}\\d{2});)?" +
                    "(BYDAY=([MO|TU|WE|TH|FR|SA|SU](,[MO|TU|WE|TH|FR|SA|SU])*);)?" +
                    "(BYMONTH=(\\d+(,\\d+)*);)?" +
                    "(BYMONTHDAY=(\\d+(,\\d+)*);)?$"
        )
        val content = if (rrule.startsWith(RRULE_START)){
            rrule.split(":").last().split(";")
        }else{
            rrule.split(";")
        }



    }

}