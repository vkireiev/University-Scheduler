package ua.foxmided.foxstudent103852.universityscheduler.util;

import java.util.function.Supplier;

public class GenericCreator<E> {
    private Supplier<E> supplier;

    public GenericCreator(Supplier<E> supplier) {
        this.supplier = supplier;
    }

    public E getInstance() {
        return supplier.get();
    }

}
