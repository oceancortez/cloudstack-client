package br.com.abril.api.cloudstack;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import br.com.abril.api.cloudstack.utils.CloudStackHelper;
import br.com.abril.api.cloudstack.utils.PropertiesManager;
import br.com.abril.api.cloudstack.utils.Utils;

/**
 * Cliente para acesso da API do CloudStack!
 * 
 */
public class App {
	private static final Logger log = Logger.getLogger(App.class);
	private static String host = null;
	private static String apiKey = null;
	private static String secretKey = null;
	private static String userId = null;
	private static PropertiesManager propertiesManager;

	static {
		try {
			propertiesManager = PropertiesManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DateFormat.getDateTimeInstance().format(new Date())+ " - Inicio do processo.");
		log.info(DateFormat.getDateTimeInstance().format(new Date())+ " - Inicio do processo.");
		String apiUrl = null;

		try {
			startPropertiesValidation();
			argumentValidation(args);

			String idServico = args[0];

			if (idServico.equals("1")) {
				System.out.println(DateFormat.getDateTimeInstance().format(new Date()) + " - Executando serciço rebootVirtualMachine.");
				apiUrl = CloudStackHelper.rebootVirtualMachine(args);
			} else if (idServico.equals("2")) {
				System.out.println(DateFormat.getDateTimeInstance().format(new Date()) + " - Executando serciço startVirtualMachine.");
				apiUrl = CloudStackHelper.startVirtualMachine(args);
			} else if (idServico.equals("3")) {
				System.out.println(DateFormat.getDateTimeInstance().format(new Date()) + " - Executando serciço stopVirtualMachine.");
				apiUrl = CloudStackHelper.stopVirtualMachine(args);
			} else {
				throw new IllegalArgumentException("Opção de serviço invalido.");
			}

			List<String> sortedParams = encodingParameters(apiUrl);
			String sortedUrl = generateUrlQueryParam(sortedParams);
			String encodedSignature = Utils.signRequest(sortedUrl, secretKey);

			String finalUrl = host + "/client/api" + "?" + apiUrl + "&apiKey="
					+ apiKey + "&signature=" + encodedSignature;
			log.info("final URL : " + finalUrl);

			Utils.clientHttpGet(host, finalUrl, userId, apiKey);

		}  catch (Throwable t) {
			log.warn(t);
		}
		log.info(DateFormat.getDateTimeInstance().format(new Date()) + " - fim do processo.");
		System.out.println(DateFormat.getDateTimeInstance().format(new Date()) + " - fim de processo.");
	}

	private static void argumentValidation(String[] args) {
		
		if (args == null || args.length == 0) {
			System.out.println("###########################################################################");
			System.out.println("# ");
			System.out.println("# Argumentos (Tipo de serviço) e (parametros de serviço) são obrigatorios!");
			System.out.println("# Exemplo: ");
			System.out.println("# java -jar cloudstack-client.jar ${1} ${2} ... ${n}");
			System.out.println("#  ${1} - 1,2,3 ... n ");
			System.out.println("#  ${2 ... n} - parametros esperados pelo serviço escolhido.");
			System.out.println("# ");
			System.out.println("###########################################################################");
			throw new IllegalArgumentException("Argumentos tipo de serviço e parametros de serviço são obrigatorios.");
		}
		
		if (!args[0].matches("-?\\d+(\\.\\d+)?")) {
			System.out.println("O primeiro argumento deve ser um opção de serviço.");
			throw new IllegalArgumentException(
					"Opção de serviço é obrigatorio.");
		}
		if (args.length < 2 || args[1] == null || args[1].equals("")) {
			System.out.println("Argumento de input de serviço são obrigatorios.");
			throw new IllegalArgumentException(
					"Parametros de serviço são obrigatorios.");
		}
	}

	private static void startPropertiesValidation() {
		host = propertiesManager.getProperty("host");
		if (host == null) {
			log.warn("Favor especificar host no formato http(s)://<DOMINIO>:<PORTA> no arquivo cloudstack-client.properties.");
			throw new IllegalArgumentException("Formato de host vazio.");
		}
		userId = propertiesManager.getProperty("userId");
		if (userId == null) {
			log.warn("Favor especificar userId no arquivo cloudstack-client.properties.");
			throw new IllegalArgumentException("Formato de host vazio.");
		}

		apiKey = propertiesManager.getProperty("apiKey");
		if (apiKey == null) {
			log.warn("Favor especificar API Key para seus acesso na CloudStack no arquivo cloudstack-client.properties.");
			throw new IllegalArgumentException("Formato de API Key vazio.");
		}

		secretKey = propertiesManager.getProperty("secretKey");
		if (secretKey == null) {
			log.warn("Favor especificar secret Key para seus acesso na CloudStack no arquivo cloudstack-client.properties.");
			throw new IllegalArgumentException("Formato de secret Key vazio.");
		}
		log.debug("Construção da API com parametros de entrada, host = '"
				+ host + "' usando apiKey = '" + apiKey + "' e secretKey = '"
				+ secretKey + "'");
	}

	private static String generateUrlQueryParam(List<String> sortedParams) {
		String sortedUrl = null;
		boolean first = true;
		for (String param : sortedParams) {
			if (first) {
				sortedUrl = param;
				first = false;
			} else {
				sortedUrl = sortedUrl + "&" + param;
			}
		}
		log.info("QueryParam : " + sortedUrl);
		return sortedUrl;
	}

	private static List<String> encodingParameters(String apiUrl)
			throws UnsupportedEncodingException {

		String encodedApiKey = URLEncoder.encode(apiKey.toLowerCase(), "UTF-8");

		List<String> sortedParams = new ArrayList<String>();
		sortedParams.add("apikey=" + encodedApiKey);

		StringTokenizer st = new StringTokenizer(apiUrl, "&");
		while (st.hasMoreTokens()) {
			String paramValue = st.nextToken().toLowerCase();
			String param = paramValue.substring(0, paramValue.indexOf("="));
			String value = URLEncoder.encode(paramValue.substring(
					paramValue.indexOf("=") + 1, paramValue.length()), "UTF-8");
			sortedParams.add(param + "=" + value);
		}
		Collections.sort(sortedParams);
		log.info("Parametros : " + sortedParams);
		return sortedParams;
	}


}
