package ua.foxmided.foxstudent103852.universityscheduler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;

public final class ConstantsTestAuditorium {
    public static final Long AUDITORIUM_ID_VALID_1 = 3L;
    public static final Long AUDITORIUM_ID_VALID_2 = 4L;
    public static final Long AUDITORIUM_ID_FOR_UPDATE = 5L;
    public static final Long AUDITORIUM_ID_INVALID = 0L;
    public static final Long AUDITORIUM_ID_NOT_EXIST = 111L;

    public static final String AUDITORIUM_NUMBER_VALID = "B103-test";
    public static final String AUDITORIUM_NUMBER_FOR_UPDATE = "X101-Upd";
    public static final String AUDITORIUM_NUMBER_EMPTY = "";
    public static final String AUDITORIUM_NUMBER_INVALID_1 = "A";
    public static final String AUDITORIUM_NUMBER_INVALID_2 = "A101-102-103-104";

    public static final Short AUDITORIUM_CAPACITY_VALID = 75;
    public static final Short AUDITORIUM_CAPACITY_INVALID_1 = 1;
    public static final Short AUDITORIUM_CAPACITY_INVALID_2 = 301;
    public static final Short AUDITORIUM_CAPACITY_INVALID_3 = -301;
    public static final Short AUDITORIUM_CAPACITY_FOR_UPDATE = 23;

    public static final boolean AUDITORIUM_AVAILABLE_VALID = false;

    private ConstantsTestAuditorium() {
    }

    public static Auditorium getTestAuditorium(@NotNull Long id) {
        Map<Long, Long> idMap = new HashMap<>();
        Map<Long, String> numberMap = new HashMap<>();
        Map<Long, Short> capacityMap = new HashMap<>();
        Map<Long, Boolean> availableMap = new HashMap<>();
        // id = 1
        idMap.put(1L, 1L);
        numberMap.put(1L, "A101");
        capacityMap.put(1L, (short) 75);
        availableMap.put(1L, true);
        // id = 2
        idMap.put(2L, 2L);
        numberMap.put(2L, "A102");
        capacityMap.put(2L, (short) 50);
        availableMap.put(2L, true);
        // id = 3
        idMap.put(3L, 3L);
        numberMap.put(3L, "B103-test");
        capacityMap.put(3L, (short) 75);
        availableMap.put(3L, false);
        // id = 4
        idMap.put(4L, 4L);
        numberMap.put(4L, "B104");
        capacityMap.put(4L, (short) 50);
        availableMap.put(4L, true);
        // id = 5
        idMap.put(5L, 5L);
        numberMap.put(5L, "B105");
        capacityMap.put(5L, (short) 50);
        availableMap.put(5L, true);
        // id = 111
        idMap.put(111L, 111L);
        numberMap.put(111L, "NotExist");
        capacityMap.put(111L, (short) 111);
        availableMap.put(111L, false);

        Auditorium auditorium = new Auditorium();
        auditorium.setId(idMap.get(id));
        auditorium.setNumber(numberMap.get(id));
        auditorium.setCapacity(capacityMap.get(id));
        auditorium.setAvailable(availableMap.get(id));
        return auditorium;
    }

    public static List<Auditorium> getAllTestAuditoriums() {
        return new ArrayList<Auditorium>(Arrays.asList(
                getTestAuditorium(1L),
                getTestAuditorium(2L),
                getTestAuditorium(3L),
                getTestAuditorium(4L),
                getTestAuditorium(5L)));
    }

    public static Auditorium newValidAuditorium(Long id) {
        Auditorium auditorium = newValidAuditorium();
        auditorium.setId(id);
        return auditorium;
    }

    public static Auditorium newValidAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_VALID);
        auditorium.setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_VALID);
        auditorium.setAvailable(ConstantsTestAuditorium.AUDITORIUM_AVAILABLE_VALID);
        return auditorium;
    }

}
