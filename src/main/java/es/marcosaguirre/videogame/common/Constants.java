package es.marcosaguirre.videogame.common;

public class Constants {
	public static final int CODE_OK = 0;
	public static final int ERROR_CODE_ELEMENT_NOT_FOUND = 1; 
	public static final int ERROR_CODE_SERVER = 2;
	public static final int ERROR_CODE_DAO = 3;
	public static final int ERROR_CODE_GAME_NOT_AVAILABLE = 4;
	public static final int ERROR_CODE_MALFORMED_REQUEST = 5;
	public static final int ERROR_CODE_GAME_NOT_RENTED = 6;
	public static final String MSJ_RENTAL_INFO = "Rental detailed info"; 
	public static final String MSJ_RETURN_INFO = "Return detailed info"; 
	public static final String MSJ_RENTAL_CONFIRMATION = "Rental Confirmation detailed info";
	public static final String MSJ_RETURN_CONFIRMATION = "Return Confirmation detailed info"; 
	public static final String MSJ_REPEATED_GAME_IDS = "List of game ids contains repeated values"; 
	public static final String MSJ_SERVER_ERROR = "Error on server"; 
	public static final String MSJ_ENTRY_DOES_NOT_EXISTS = "Entry does not exists"; 
	public static final String MSJ_ENTRY_DELETED = "Entry has been deleted";
	public static final String MSJ_EMPTY_LIST = "The list is empty";
	public static final String MSJ_ENTRY_CREATED = "The entity has been created";
	public static final String MSJ_ENTRY_UPDATED = "The entity has been updated";
	public static final String MSJ_GAME_NOT_AVAILABLE = "The game is not available for rent";
	public static final String MSJ_GAME_NOT_RENTED= "The game is not rented by this customer in this moment";
	
	public static final String ONLY_CHARACTERS_REGEX_STRING = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$";
	public static final String CHARACTERS_31_STRING= "abcdefghijABCDEFGHIJabcdefghijA";
	public static final String CHARACTERS_61_STRING = "abcdefghijABCDEFGHIJabcdefghijabcdefghijABCDEFGHIJabcdefghijA";
	public static final String CHARACTERS_71_STRING = "abcdefghijABCDEFGHIJabcdefghijabcdefghijABCDEFGHIJabcdefghijABCDEFGHIJa";
	public static final String CHARACTERS_61_MAIL_STRING = "abcdefghijABCDEFGHIJabcdefghijabcdefg@hijABCDEFGHIJabcdefghijA.com";
	public static final String NOT_ONLY_CHARACTERS_STRING = "1dfjk&";
	public static final String BAD_EMAIL_FORMATTING_STRING = "email.email.com";
}
