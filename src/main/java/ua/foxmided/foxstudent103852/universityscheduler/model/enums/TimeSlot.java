package ua.foxmided.foxstudent103852.universityscheduler.model.enums;

import java.time.LocalTime;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TimeSlot {

    LECTURE0(LocalTime.of(7, 0), LocalTime.of(8, 20)),
    LECTURE1(LocalTime.of(8, 30), LocalTime.of(9, 50)),
    LECTURE2(LocalTime.of(10, 0), LocalTime.of(11, 20)),
    LECTURE3(LocalTime.of(12, 0), LocalTime.of(13, 20)),
    LECTURE4(LocalTime.of(13, 30), LocalTime.of(14, 50)),
    LECTURE5(LocalTime.of(15, 0), LocalTime.of(16, 20)),
    LECTURE6(LocalTime.of(16, 30), LocalTime.of(17, 50));

    public final LocalTime timeBegin;
    public final LocalTime timeEnd;

}
