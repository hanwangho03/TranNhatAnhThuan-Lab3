package Lab3.TranNhatAnhThuan_Lab3.services;


import entities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final List<Book> books;
    public List<Book> getAllBooks() {
        return books;
    }
    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public void updateBook(Book book) {
        var bookOptional = getBookById(book.getId());
        if (bookOptional.isPresent()) {
            Book bookUpdate = bookOptional.get();
            bookUpdate.setTitle(book.getTitle());
            bookUpdate.setAuthor(book.getAuthor());
            bookUpdate.setPrice(book.getPrice());
            bookUpdate.setCategory(book.getCategory());
        }
    }
    public void deleteBookById(Long id) {
        getBookById(id).ifPresent(books::remove);
    }
    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
}
