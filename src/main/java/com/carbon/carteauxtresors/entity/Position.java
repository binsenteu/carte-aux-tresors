package com.carbon.carteauxtresors.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Position {
    Integer x;
    Integer y;

    public Position(String x, String y) throws NumberFormatException {
        try {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
        } catch (Exception e) {
            throw new NumberFormatException(String.format("Unable to create position with data x = %s and y = %s", x, y));
        }
    }

    public boolean isPositionOutOfBounds(Position mapPosition) {
        return this.getX() < 0 || this.getX() >= mapPosition.getX()
                || this.getY() < 0 || this.getY() >= mapPosition.getY();
    }

    @Override
    public String toString() {
        return "X = " + this.getX() + " ; Y = " + this.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (!Objects.equals(x, position.x)) return false;
        return Objects.equals(y, position.y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}
