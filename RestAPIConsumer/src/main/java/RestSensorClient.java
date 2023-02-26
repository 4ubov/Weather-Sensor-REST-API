import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestSensorClient {
    public static void main(String[] args) {

        //  Init fields
        RestTemplate restTemplate = new RestTemplate();

        //  Setup media type for sending content
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a new sensor (for more info go to realization of methods)
        RestSensorClient.createNewSensor(restTemplate);

        // Create a new Measurement (More info in method implementation)
        RestSensorClient.createInteractiveNewMeasurement(restTemplate);

        //  Create a new Measurement by N times, with random values
        //  Method parameters: String sensorName, int countOfMeasurements, RestTemplate restTemplate
        RestSensorClient.createRandomMeasurements("Sensor_v1", 10, restTemplate);

        //  Draw graph by all existing measurements
        RestSensorClient.drawMeasurementsGraph(restTemplate);

    }

    public static void createNewSensor(RestTemplate restTemplate) {
        //  About method: Create new Sensor by interactive input from console.
        //  If this sensor already exist program End, cause throws exception.

        // Init sensor object
        Map<String, String> sensor = new HashMap<String, String>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new SensorName");
        String sensorName = scanner.next();
        scanner.close();

        sensor.put("name", sensorName);

        //  Sending request and getting response from server
        String url = "http://localhost:8080/sensor/registration";
        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(sensor);
        String response = restTemplate.postForObject(url, request, String.class);

        System.out.println(response);
    }

    public static void createInteractiveNewMeasurement(RestTemplate restTemplate) {
        // About method: Create new Measurement by interactive input from console,
        // and generating request, that will send to server and getting response (print it in console).

        //  Init field for Measurement object
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter existing Sensor name: ");
        String SensorName = scanner.nextLine();

        System.out.println("Measurement setting.");
        System.out.println("Enter value: ");
        Double value = Math.round(scanner.nextDouble() * 100.0) / 100.0;

        System.out.println("Enter isRaining: ");
        Boolean raining = scanner.nextBoolean();

        scanner.close();

        //  Init Sensor and Measurement object
        Map<String, String> sensor = new HashMap<String, String>();
        sensor.put("name", SensorName);

        Map<String, Object> measurement = new HashMap<String, Object>();
        measurement.put("value", value);
        measurement.put("raining", raining);
        measurement.put("sensor", sensor);

        //  Sending request and getting response from server
        String urlToCreateMeasurement = "http://localhost:8080/measurements/add";
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(measurement);
        String response = restTemplate.postForObject(urlToCreateMeasurement, request, String.class);

        System.out.println(response);

        measurement.clear();
    }

    public static void createRandomMeasurements(String sensorName,
                                                int countOfMeasurements,
                                                RestTemplate restTemplate) {

        //  About method: create Measurement: countOfMeasurements-(N) times by random values
        String urlToCreateMeasurement = "http://localhost:8080/measurements/add";

        //  Init Sensor and Measurement object
        Map<String, String> sensor = new HashMap<String, String>();
        sensor.put("name", sensorName);

        Map<String, Object> measurement = new HashMap<String, Object>();

        // Fill Measurement object and send request & get response by countOfMeasurements times
        for (int i = 0; i < countOfMeasurements; i++) {
            Random random = new Random();

            Double value = ThreadLocalRandom.current().nextDouble(-100, 99 + 1);
            value = Math.round(value * 100.0) / 100.0;
            Boolean raining = random.nextBoolean();

            measurement.put("value", value);
            measurement.put("raining", raining);
            measurement.put("sensor", sensor);

            HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(measurement);
            String response = restTemplate.postForObject(urlToCreateMeasurement, request, String.class);

            System.out.println(response);
            measurement.clear();
        }
    }

    public static void drawMeasurementsGraph(RestTemplate restTemplate) {
        //  Url for Get request
        String url = "http://localhost:8080/measurements";

        //  Get all measurements from server
        String response = restTemplate.getForObject(url, String.class);

        //  Draw graph by xchart ver 3.8.3
        List<Double> temperatures = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d\\d?.\\d");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(response));
        int counter = 1;
        while (matcher.find()) {
            temperatures.add(Double.parseDouble(matcher.group()));
            indexes.add(counter++);
        }

        XYChart chart = QuickChart.getChart("График температур", "", "Температура", "Sensor",
                indexes, temperatures);

        SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);
        wrapper.displayChart();
    }
}
