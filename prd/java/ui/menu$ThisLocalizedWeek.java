package com.mccmr.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class menu$ThisLocalizedWeek {
   private static final ZoneId TZ = ZoneId.of("Pacific/Auckland");
   private final Locale locale;
   private final DayOfWeek firstDayOfWeek;
   private final DayOfWeek lastDayOfWeek;

   public menu$ThisLocalizedWeek(Locale var1) {
      this.locale = locale;
      this.firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
      this.lastDayOfWeek = DayOfWeek.of((this.firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length + 1);
   }

   public LocalDate getFirstDay() {
      return LocalDate.now(TZ).with(TemporalAdjusters.previousOrSame(this.firstDayOfWeek));
   }

   public LocalDate getLastDay() {
      return LocalDate.now(TZ).with(TemporalAdjusters.nextOrSame(this.lastDayOfWeek));
   }

   public String toString() {
      return String.format("The %s week starts on %s and ends on %s", this.locale.getDisplayName(), this.firstDayOfWeek, this.lastDayOfWeek);
   }
}
