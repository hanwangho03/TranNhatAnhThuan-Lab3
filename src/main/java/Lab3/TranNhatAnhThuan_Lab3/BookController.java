package Lab3.TranNhatAnhThuan_Lab3;

import Lab3.TranNhatAnhThuan_Lab3.services.BookService;
import entities.Book;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public String showAllBooks(@RequestParam(value = "author", required = false) String author, @NotNull Model model) {
        List<Book> books;
        if (author != null && !author.isEmpty()) {
            books = bookService.findBooksByAuthor(author);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(@NotNull Model model) {
        model.addAttribute("book", new Book());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook( @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors() || book.getId() == null || book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty() ||
                book.getPrice() == null || book.getCategory() == null || book.getCategory().isEmpty()) {
            model.addAttribute("error", "All fields are required.");
            return "book/add";
        }
        if(bookService.getBookById(book.getId()).isEmpty())
            bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/index")
    public String showHomePage() {
        return "book/index"; // Đây là tên của file index.html trong thư mục templates/home
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id) {
        var book = bookService.getBookById(id).orElse(null);
        model.addAttribute("book", book != null ? book : new Book());
        return "book/edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors() || book.getId() == null || book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty() ||
                book.getPrice() == null || book.getPrice() <= 0 || book.getCategory() == null || book.getCategory().isEmpty()) {
            model.addAttribute("error", "All fields are required and must be valid.");
            return "book/edit";
        }
        bookService.updateBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        if (bookService.getBookById(id).isPresent())
            bookService.deleteBookById(id);
        return "redirect:/books";
    }
}
