package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.Owner;
import bookcrossing.domain.Person;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.OwnerRepository;
import bookcrossing.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    public OwnerService(OwnerRepository ownerRepository, PersonRepository personRepository, BookRepository bookRepository) {
        this.ownerRepository = ownerRepository;
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Owner findById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner with ID " + id + " not found."));
    }

    @Transactional
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Transactional
    public List<Owner> getOwnerBooks(Long personId) {
        return ownerRepository.findByPersonId(personId);
    }

    @Transactional
    public void registerOwner(Long personId, Long bookId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Person with ID " + personId + " not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + bookId + " not found."));

        if (isPersonOwnsBook(person, book)) {
            throw new RuntimeException("You cannot register as an owner of your own book.");
        }

        Owner owner = new Owner();
        owner.setPerson(person);
        owner.setBook(book);

        ownerRepository.save(owner);
    }

    private boolean isPersonOwnsBook(Person person, Book book) {
        List<Book> ownerBooks = person.getOwnerBooks();
        return ownerBooks != null && ownerBooks.contains(book);
    }
}
