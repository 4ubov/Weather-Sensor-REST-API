package com.chubov.weathersensor.dto;

import com.chubov.weathersensor.models.Sensor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class MeasurementDTO {
    //  Fields
    @NotNull(message = "Значение не может быть пустым")
    @Min(value = -100, message = "Значение не может быть больше, чем -100")
    @Max(value = 100, message = "Значение не может быть больше, чем 100")
    private double value;

    @NotNull(message = "Значение должно быть true или false")
    private Boolean raining;

    @NotNull(message = "Должен быть указан сенсор!")
    private SensorDTO sensor;

    //  Getter and Setter


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
