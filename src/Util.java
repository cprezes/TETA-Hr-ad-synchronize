
public class Util {

	private static String query = "SELECT LOWER(LOGIN_AD) AS LOGIN_AD, IMIE, NAZWISKO, CONCAT(CONCAT(IMIE , ' ' ), NAZWISKO) AS CN , STANOWISKO, JEDNOSTKA, STANOWISKO_OPIS, LOWER(LOGIN_AD_PRZEL ) AS LOGIN_AD_PRZEL, LOWER(E_MAIL) AS E_MAIL, FRIMA, TO_CHAR( (DATA_ROZW + interval '1' DAY) , 'mm/dd/yyyy') AS DATA_ROZW_EXP ,TO_CHAR( DATA_ROZW , 'dd-mm-yyyy') AS DATA_ROZW_OPIS FROM MOST_AD WHERE FRIMA <> 'blank' AND LENGTH(TRIM(TRANSLATE(LOGIN_AD, ' +-.0123456789',' '))) IS NOT NULL  ";

	public String getQuery() {
		return query;
	}

	public String addPsTag(String column, String value) {


		String out = "";
		
		
		if (empty( value )) {
			out = "";
		} else {

		out=checkWhatNeedDekorate(column,value);

		}
		
		return out + " ";
	}
	
	
	private String checkWhatNeedDekorate(String column,String value) {
		String out="";
		
		switch (column) {
		case "LOGIN_AD":				out = addPreambula(value);				break;
		case "IMIE":					out = name(value);						break;
		case "NAZWISKO":				out = surname(value);					break;
		case "STANOWISKO":				out = title(value);						break;
		case "JEDNOSTKA":				out = department(value);				break;
		case "STANOWISKO_OPIS":			out = streetAddress(value);				break;
		case "LOGIN_AD_PRZEL":			out = manager(value);					break;
		case "FRIMA":					out = company(value);					break;
		case "CN":						out = displayName(value);				break;
		case "DATA_ROZW_OPIS":			out = dateQuitDescryption(value);		break;
		case "DATA_ROZW_EXP":			out = dateQuitSet(value);				break;
		}
		return out;
	}
	

	private String addPreambula(String param) {
		return "get-aduser -filter {SamAccountName -like \"" + param + "\" } | Set-ADUser";
	}

	private String name(String param) {

		return "-GivenName \"" + CapitalizeFirst(param) + "\"";
	}

	private String surname(String param) {

		return "-Surname \"" + CapitalizeFirst(param) + "\"";
	}

	private String title(String param) {
		return "-Title \"" + CapitalizeFirst(param) + "\"";
	}

	private String department(String param) {
		return "-Department \"" + CapitalizeFirst(param) + "\"";
	}

	private String streetAddress(String param) {
		return "-StreetAddress \"" + CapitalizeFirst(param) + "\"";
	}

	private String manager(String param) {
		return "-Manager " + param;
	}

	private String displayName(String param) {
		return "-DisplayName  \"" + CapitalizeFirst(param) + "\"";
	}

	private String company(String param) {

		if (param.equals("WARSZAWA")) {
			param = "Head Office";
		} else {
			param = "Oddzial";
		}
		return "-Company \"" + param + "\"";
	}

	private String dateQuitDescryption(String param) {

		return "-Description \"Data zwolnienia " + param + "\"";

	}

	private String dateQuitSet(String param) {

		return "-AccountExpirationDate \"" + param + "\"";

	}

	private String CapitalizeFirst(String source) {
		int len = 0;
		int iter = 0;
		StringBuffer res = new StringBuffer();
		String[] strArr = source.split(" ");
		for (String str : strArr) {
			len = str.length();
			if (len <= 2) {
				str = str.toUpperCase();
			} else if (len > 2) {
				char[] stringArray = str.trim().toCharArray();
				iter = 0;
				for (Character c : stringArray) {
					if (iter == 0) { // Pierwszy znak wyrazu
						stringArray[0] = Character.toUpperCase(c);
					} else if (stringArray[iter - 1] == 45) { // Sprawdzenie podwójnego nazwiska
						stringArray[iter] = Character.toUpperCase(c);
					} else { // Kolejene znaki wyrazu
						stringArray[iter] = Character.toLowerCase(c);
					}
					iter++;
				}

				str = new String(stringArray);
			} else {
				str = "";
			}
			res.append(str).append(" ");

		}
		return res.toString().trim().replace("\"", "");
	}
	public static boolean empty( String s ) {
		 
		  return s == null || s.trim().isEmpty() || s.contains("null")||s.length() == 0;
		}
}
