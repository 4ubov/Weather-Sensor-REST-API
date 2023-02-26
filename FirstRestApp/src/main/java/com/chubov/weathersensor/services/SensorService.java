package com.chubov.weathersensor.services;

import com.chubov.weathersensor.models.Sensor;
import com.chubov.weathersensor.repositories.SensorRepository;
import com.chubov.weathersensor.util.SensorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    //  Methods
    @Transactional
    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }

    public Optional<Sensor> findOneByName(String name) {
        return sensorRepository.findByName(name);
    }

    public Sensor findOneByNameOrElseThrowException(String name) {
        return sensorRepository.findByName(name).orElseThrow(SensorNotFoundException::new);
    }
}
