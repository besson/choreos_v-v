package br.usp.ime.choreos.vvws.client;

import br.usp.ime.choreos.vvws.ws.SimpleStoreWS;
import br.usp.ime.choreos.vvws.ws.SimpleStoreWSService;

public class Client {

	public static void main(String[] args) {

		SimpleStoreWSService service = new SimpleStoreWSService();
		SimpleStoreWS store = service.getSimpleStoreWSPort();
		
		String cds = store.searchByGenre("Rock");
		System.out.println(cds);
	}

}
