package br.com.alura.conversordemoedas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExchangeRateJsonConversion {
    private final Gson gson;
    private ExchangeRateData data;
    private double value;

    public ExchangeRateJsonConversion() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void setData(String json, double value) {
        this.data = gson.fromJson(json, ExchangeRateData.class);
        this.value = value;
    }

    public String getData() {
        if (data == null || data.result().compareToIgnoreCase("error") == 0) return "Erro na obtenção dos dados";

        return value + " " + data.base_code() + " ------->> " + data.conversion_result() + " " + data.target_code();
    }
}
