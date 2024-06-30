package br.com.alura.conversordemoedas;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static ExchangeRateAPI exchangeRate;
    private static ExchangeRateJsonConversion exchangeRateJsonConversion;

    private static void menu() {
        System.out.print("""
                  * CONVERSOR DE MOEDAS *
                
                1 - Converter Valor
                2 - Verificar Moedas Suportadas
                
                0 - Sair
                
                Selecione a opção desejada :\s""");
    }

    private static String receiveStringInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static int receiveDigit() {
        String input = receiveStringInput();

        while(!input.matches("^[0-9]$")) {
            System.out.print("Numero inválido, tente novamente : ");
            input = receiveStringInput();
        }

        return Integer.parseInt(input);
    }

    private static int receiveNumber() {
        String input = receiveStringInput();

        while(!input.matches("^[0-9]+$") && input.length() < 10) {
            System.out.print("Número inválido ou muito longo, tente novamente : ");
            input = receiveStringInput();
        }

        return Integer.parseInt(input);
    }

    private static String receiveValidToken() {
        String input = "";

        while (input.length() != 3) {
            input = receiveStringInput();

            if (input.length() != 3) System.out.println("Token da moeda precisa ter exatamente 3 letras! Tente novamente.");
        }

        return input;
    }

    private static double receiveDouble() {
        String input = receiveStringInput();

        while(!input.matches("^-?[0-9]+([.][0-9]+)?$") || input.length() > 25) {
            if (input.length() > 25) System.out.println("Número muito alto, tente novamente : ");
            else System.out.println("Número inválido, tente novamente : ");
            input = receiveStringInput();
        }

        return Double.parseDouble(input);
    }

    private static int verifyTokenOrDigit(String input) {
        // if number with length 1, return 0
        // if token (length 3), return 1
        // if none of the above, return -1

        if (input.matches("^[0-9]$")) {
            return 0; // is a number with length 1
        }

        if (input.length() == 3 && input.matches("^[a-zA-Z]+$")) {
            return 1; // is a token with length 3
        }

        return -1; // none of the above
    }

    public static void convertValueBetweenCurrencies() {
        /*
        Digite o primeiro
        Pegar se o informado é token ou um número
        Digite o segundo (Token ou número)
        ExchangeRateAPI -> requisição
        gson lib, pegar dados de json (classe não implementada)
        */
        String input1;
        String input2;
        double value;

        System.out.println("A busca pode ser feita a partir de Tokens ou numeros.");
        System.out.println(exchangeRate.returnSupportedCurrencies());
        System.out.print("Digite um Token ou um numero: ");

        input1 = receiveStringInput();
        switch (verifyTokenOrDigit(input1)) {
            case 0:
                int int1 = Integer.parseInt(input1);
                System.out.print("digite o segundo número : ");
                int int2 = receiveDigit();
                System.out.print("informe a quantidade da primeira moeda : ");
                value = receiveDouble();
                exchangeRateJsonConversion.setData(exchangeRate.pairRequest(int1, int2, value), value);
                System.out.println(exchangeRateJsonConversion.getData());
                break;
            case 1:
                System.out.print("digite o segundo token : ");
                input2 = receiveValidToken();
                System.out.print("informe a quantidade da primeira moeda : ");
                value = receiveDouble();
                exchangeRateJsonConversion.setData(exchangeRate.pairRequest(input1.toUpperCase(), input2.toUpperCase(), value), value);
                System.out.println(exchangeRateJsonConversion.getData());
                break;
            case -1:
                System.out.println("invalid input");
                break;
        }
    }

    public static void main(String[] args) {
        exchangeRateJsonConversion = new ExchangeRateJsonConversion();

        try {
            exchangeRate = new ExchangeRateAPI();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            menu();
            int option = receiveDigit();

            switch (option) {
                case 0:
                    return;
                case 1:
                    convertValueBetweenCurrencies();
                    break;
                case 2:
                    System.out.print(exchangeRate.returnSupportedCurrencies());
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
