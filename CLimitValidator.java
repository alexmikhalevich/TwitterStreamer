package ru.fizteh.fivt.students.maked0n.moduletests.library;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class CLimitValidator implements IParameterValidator {
    private static final int MAX_TWEETS = 100;
    public final void validate(String name, String value)
        throws ParameterException {
        int n = Integer.parseInt(value);
        if (n <= 0) {
            throw new ParameterException("Параметр " + name
                    + " должен быть положительным (найдено " + value + ") ");
        } else if (n > MAX_TWEETS) {
            throw new ParameterException("Параметр " + name
                    + " должен быть меньше, чем 100 (найдено " + value + ") ");
        }
    }
}
