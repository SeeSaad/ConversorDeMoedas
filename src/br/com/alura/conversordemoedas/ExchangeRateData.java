package br.com.alura.conversordemoedas;

public record ExchangeRateData(String result,
                               String base_code,
                               String target_code,
                               double conversion_rate,
                               double conversion_result) {}