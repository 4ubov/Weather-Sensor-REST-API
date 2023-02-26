package com.chubov.weathersensor.controllers;

import com.chubov.weathersensor.dto.SensorDTO;
import com.chubov.weathersensor.models.Sensor;
import com.chubov.weathersensor.services.SensorService;
import com.chubov.weathersensor.util.ErrorResponse;
import com.chubov.weathersensor.util.SensorNotCreatedException;
import com.chubov.weathersensor.validators.UniqueSensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {
    private final ModelMapper modelMapper;
    private final SensorService sensorService;
    private final UniqueSensorValidator validator;

    @Autowired
    public SensorController(ModelMapper modelMapper, SensorService sensorService, UniqueSensorValidator validator) {
        this.modelMapper = modelMapper;
        this.sensorService = sensorService;
        this.validator = validator;
    }

    //  Methods
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO,
                                             BindingResult bindingResult) {
        Sensor sensor = convertToSensor(sensorDTO);
        validator.validate(sensor, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new SensorNotCreatedException(errorMessage.toString());
        }
        sensorService.save(sensor);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotCreatedException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //  ModelMapper methods. Converters.
    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
