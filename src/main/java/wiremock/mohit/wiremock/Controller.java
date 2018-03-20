package wiremock.mohit.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

    @Value("${base.url}")
    private String baseUrl;

    @Value("${base.mocked.url}")
    private String mockedUrl;

    private WireMockServer wireMockServer = new WireMockServer(8443);

    public Controller(){
        wireMockServer.start();
    }

    private static final String API="/country/get/iso2code/IN";
    private static final String APPLICATION_JSON="application/json";


    @GetMapping("get")
    public String greeting() {
        return "Hello World";
    }

    @GetMapping("consume")
    public String consumeAPI(){
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(baseUrl+API);
        String response = restTemplate.getForObject(baseUrl+API, String.class);
        return response;
    }

    @GetMapping("consume/wiremock")
    public String consumeWiremockAPI(){
        wireMockServer.stubFor(get(urlEqualTo(API))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("\"testing-library\": \"WireMock\"")));
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(mockedUrl+API);
        String response = restTemplate.getForObject(mockedUrl+API, String.class);
        return response;
    }


    @GetMapping("consume/wiremock/mappings")
    public String consumeWiremock1API(){
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:8082/__admin", String.class);
        return response;
    }

}
