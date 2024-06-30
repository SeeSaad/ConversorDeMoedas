package br.com.alura.conversordemoedas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ExchangeRateAPI {
    private final String key;
    private final String url = "https://v6.exchangerate-api.com/v6/";

    private final List<String> currenciesCode = new LinkedList<>();
//    private final String[] currenciesCode = {"ARS", "BRL", "EUR", "JPY", "MXN", "USD"};

    public ExchangeRateAPI() throws FileNotFoundException {
        File file = new File("key/ExchangeRateApiKey.txt");
        Scanner scanner;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("failed to retreive API key, file not found");
            throw new FileNotFoundException();
        }

        this.key = scanner.nextLine();
        this.currenciesCode.add("ARS");
        this.currenciesCode.add("BRL");
        this.currenciesCode.add("EUR");
        this.currenciesCode.add("JPY");
        this.currenciesCode.add("MXN");
        this.currenciesCode.add("USD");
    }

    public String returnSupportedCurrencies() {
        return """
                1 - ARS - Peso Argentino
                2 - BRL - Real Brasileiro
                3 - EUR - Euro
                4 - JPY - Yen Japonês
                5 - MXN - Peso Mexicano
                6 - USD - Dolar Estado Unidense
                """;
    }

    public String pairRequest(String option1, String option2, double value) {
        int num1 = currenciesCode.indexOf(option1);
        int num2 = currenciesCode.indexOf(option2);
        if (num1 == -1 || num2 == -1) throw new IllegalArgumentException();

        try {
            return safePairRequest(num1, num2, value);

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao fazer a requisição");
            return """
                    {
                        "result": "error",
                        "error-type": "unknown-code"
                    }
                    """;
        }
    }

    public String pairRequest(int option1, int option2, double value) {
        int num1 = option1 - 1;
        int num2 = option2 - 1;

        if (num1 < 0 || num2 < 0 || num1 >= currenciesCode.size() || num2 >= currenciesCode.size()) throw new IllegalArgumentException();

        try {
            return safePairRequest(num1, num2, value);

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao fazer a requisição");
            return """
                    {
                        "result": "error",
                        "error-type": "unknown-code"
                    }
                    """;
        }
    }

    private String safePairRequest(int option1, int option2, double value) throws IOException, InterruptedException {
        String code1 = currenciesCode.get(option1);
        String code2 = currenciesCode.get(option2);

        System.out.println(url + "[API-KEY]" + "/pair/" + code1 + "/" + code2 + "/" + value);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url + key + "/pair/" + code1 + "/" + code2 + "/" + value))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}