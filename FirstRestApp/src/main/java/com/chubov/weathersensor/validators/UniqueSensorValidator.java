package com.chubov.weathersensor.validators;

import com.chubov.weathersensor.models.Sensor;
import com.chubov.weathersensor.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@SuppressWarnings("NullableProblems")
@Component
public class UniqueSensorValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public UniqueSensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        Sensor sensor = (Sensor) object;
        if (sensorService.findOneByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "", "Такой сенсор уже зарегистрирован!");
        }
    }
}
