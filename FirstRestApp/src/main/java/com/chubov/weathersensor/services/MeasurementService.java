package com.chubov.weathersensor.services;

import com.chubov.weathersensor.models.Measurement;
import com.chubov.weathersensor.models.Sensor;
import com.chubov.weathersensor.repositories.MeasurementRepository;
import com.chubov.weathersensor.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    //  Methods
    public Long findAllRainyDays(){
        return measurementRepository.findAll().stream().filter(Measurement::isRaining).count();
    }
    public List<Measurement> findAllMeasurements() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void save(Measurement measurement) {
        measurementRepository.save(measurement);
    }

}

