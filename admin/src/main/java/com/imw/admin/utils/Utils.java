package com.imw.admin.utils;

import com.imw.commonmodule.enums.subscription.DurationType;

import java.time.LocalDate;

public class Utils {

    public static LocalDate getEndDateForPurchase(Integer duration, DurationType durationType, LocalDate startDate){

        if(durationType == DurationType.DAY){
            return startDate.plusDays(duration);
        }
        else if (durationType == DurationType.WEEK) {
            return startDate.plusWeeks(duration);
        }
        else if (durationType == DurationType.MONTH) {
            return startDate.plusMonths(duration);
        }
        else if (durationType == DurationType.YEAR) {
            return startDate.plusYears(duration);
        }
        else{
            return startDate.plusDays(duration);
        }

    }
}
