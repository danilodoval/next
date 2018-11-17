package com.challenge.next.core.utils;

import com.challenge.next.core.driver.Driver;
import com.challenge.next.core.driver.Driver.Status;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides utilities related to {@link Driver}
 */
public class Drivers {

    /**
     * Do not have the purpose of creating objects.
     */
    private Drivers() {
        throw new AssertionError();
    }

    public static <C extends Driver> Set<C> filter(final Collection<C> drivers, final Status status) {
        return drivers.stream().filter(c -> c.getStatus() == status).collect(Collectors.toSet());
    }
}
