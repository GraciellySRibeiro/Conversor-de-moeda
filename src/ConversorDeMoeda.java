import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorDeMoeda {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/eeea9a34cf947bbc08a699e0/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("*********************************************");
            System.out.println(" Seja bem-vindo/a ao Conversor de Moeda =]");
            System.out.println("*********************************************");
            System.out.println("1) Dólar >> Peso argentino");
            System.out.println("2) Peso argentino >> Dólar");
            System.out.println("3) Dólar >> Real brasileiro");
            System.out.println("4) Real brasileiro >> Dólar");
            System.out.println("5) Dólar >> Peso colombiano");
            System.out.println("6) Peso colombiano >> Dólar");
            System.out.println("7) Sair");
            System.out.print("Escolha uma opção válida: ");
            int opcao = scanner.nextInt();

            if (opcao == 7) {
                System.out.println("Saindo do programa...");
                break;
            }

            System.out.print("Digite o valor que deseja converter: ");
            double valor = scanner.nextDouble();

            switch (opcao) {
                case 1:
                    converterMoeda("USD", "ARS", valor);
                    break;
                case 2:
                    converterMoeda("ARS", "USD", valor);
                    break;
                case 3:
                    converterMoeda("USD", "BRL", valor);
                    break;
                case 4:
                    converterMoeda("BRL", "USD", valor);
                    break;
                case 5:
                    converterMoeda("USD", "COP", valor);
                    break;
                case 6:
                    converterMoeda("COP", "USD", valor);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    private static void converterMoeda(String from, String to, double valor) {
        try {
            // Monta a URL da API com a moeda base
            String urlStr = API_URL + from;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Lê a resposta da API
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse da resposta JSON usando Gson
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");

            // Obtém a taxa de câmbio e realiza a conversão
            double taxaCambio = conversionRates.get(to).getAsDouble();
            double valorConvertido = valor * taxaCambio;

            System.out.printf("Valor %.2f [%s] corresponde ao valor final de >>> %.2f [%s]%n",
                    valor, from, valorConvertido, to);

        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
        }
    }
}
