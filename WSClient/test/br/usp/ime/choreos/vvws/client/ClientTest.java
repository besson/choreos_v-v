package br.usp.ime.choreos.vvws.client;

import static org.junit.Assert.*;

import org.junit.Test;


import br.usp.ime.choreos.vvws.ws.SimpleStoreWS;
import br.usp.ime.choreos.vvws.ws.SimpleStoreWSService;


public class ClientTest {
	
	@Test
	public void clientTest() {
		
		SimpleStoreWSService service = new SimpleStoreWSService();
		SimpleStoreWS store = service.getSimpleStoreWSPort();

		String rockCds = store.searchByGenre("Rock");
		assertEquals("The dark side of the moon;Led Zeppelin I;Have a nice day;Help!;", rockCds);

		String noRockCds = store.searchByGenre("rock"); // case sensitive
		assertTrue(noRockCds.isEmpty());

		String floydCds = store.searchByArtist("Pink");
		assertEquals("The dark side of the moon;", floydCds);		
		
		String emptyReturn = store.searchByArtist("This artist does not exist");
		assertTrue(emptyReturn.isEmpty());
		
		assertTrue(store.purchase(null, null));
		
	}

}