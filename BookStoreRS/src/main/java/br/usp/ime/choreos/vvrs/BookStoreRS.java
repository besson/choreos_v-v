package br.usp.ime.choreos.vvrs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import br.usp.ime.choreos.vvrs.model.Book;
import br.usp.ime.choreos.vvrs.model.MockBooks;

@Path("/bookstore")
public class BookStoreRS {

	private static List<Book> books = MockBooks.bookList; 
	
	public static int addBook(Book book) {
	        
		books.add(book);
		return books.size()-1;
        }

	@Path("/book/{id}")
	public static String getBookById(@PathParam("id")  int id) {
		
		Book book = BookStore.getBookById(id);
		
		if (book != null)
			return book.toString();
		else
			return "Not found!";
        }

	public static void updateBook(int id, Book book) {

		books.set(id, book);
        }

	public static void removeBook(int id) {

		books.remove(id);
        }

	public static void clear() {

		books.clear();
        }

	public static List<Book> getBookByTitle(String title) {

		List<Book> found = new ArrayList<Book>();
		
		for (Book b: books) {
			if (b.getTitle().toUpperCase().contains(title.toUpperCase()))
				found.add(b);
		}
		
		return found;
        }

	@GET
	@Path("/books")
	public static String getAllBooks() {

		return BookStore.getAllBooks().toString();
        }
}

