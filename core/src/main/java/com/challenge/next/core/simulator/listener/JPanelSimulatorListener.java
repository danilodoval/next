package com.challenge.next.core.simulator.listener;

import com.challenge.next.core.simulator.SimulatorListener;
import com.challenge.next.core.common.Position2D;
import com.challenge.next.core.common.SimulatorMap2D;
import com.challenge.next.core.driver.Driver;
import com.challenge.next.core.passenger.Passenger;

import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Configuration
public class JPanelSimulatorListener extends JPanel implements SimulatorListener {

    private static final long serialVersionUID = -2602902438192375955L;
    private final SimulatorMap2D simulatorMap2D;
    private Set<Driver> drivers;
    private Set<Passenger> passengers;

    public JPanelSimulatorListener(final SimulatorMap2D simulatorMap2D) {
        this.simulatorMap2D = Objects.requireNonNull(simulatorMap2D);
    }

    @Override
    public void paint(final Graphics g) {
        final Graphics2D graphics2d = (Graphics2D) g;

        graphics2d.setColor(Color.WHITE);
        final Position2D limit = simulatorMap2D.getLimit();
        g.fillRect(0, 0, limit.getX() + 50, limit.getY() + 50);


        drivers.forEach(driver -> {
            switch (driver.getStatus()) {
                case EMPTY:
                    graphics2d.setColor(Color.GRAY);
                    break;
                case ON_THE_WAY:
                    graphics2d.setColor(Color.DARK_GRAY);
                    break;
                case BUSY:
                    graphics2d.setColor(Color.BLACK);
            }
            final Position2D p = (Position2D) driver.getPosition();
            graphics2d.drawString(driver.getIdentifier(), p.getX(), p.getY());
        });

        passengers.forEach(passenger -> {
            switch (passenger.getStatus()) {
                case NO_DRIVER:
                    graphics2d.setColor(Color.BLUE);
                    break;
                case WAITING_DRIVER:
                    graphics2d.setColor(Color.GREEN);
                    break;
                case WITH_DRIVER:
                    graphics2d.setColor(Color.BLACK);
                    break;
                case ARRIVE_DESTINATION:
                    graphics2d.setColor(Color.RED);
            }
            final Position2D origin = (Position2D) passenger.getPosition();
            graphics2d.fillRect(origin.getX(), origin.getY(), 2, 2);
            final Position2D destination = (Position2D) passenger.getDestination();
            graphics2d.setColor(Color.LIGHT_GRAY);
            graphics2d.drawLine(origin.getX(), origin.getY(), destination.getX(), destination.getY());
        });

        graphics2d.setColor(Color.BLACK);
        simulatorMap2D.getBloquedPositions().stream().forEach(p -> graphics2d.fillRect(p.getX(), p.getY(), 1, 1));
    }

    @Override
    public void turnCompleted(final Set<Driver> drivers, final Set<Passenger> passengers) {
        this.drivers = new HashSet<>(drivers);
        this.passengers = new HashSet<>(passengers);
        repaint();
    }
}
