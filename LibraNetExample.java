import java.time.Duration;
import java.time.format.DateTimeParseException;

// Common interface for all library items
interface LibraryItem {
    void borrow(String duration) throws Exception;   // Borrow for a given duration string
    void returnItem() throws Exception;              // Return item
    boolean isAvailable();
    int getId();
    String getTitle();
}

// Abstract base class for common fields and methods
abstract class BaseItem implements LibraryItem {
    protected int id;
    protected String title;
    protected String author;
    protected boolean available = true;
    protected long borrowEndEpochMillis = 0;  // Store borrow end time in epoch millis
    protected int finePerDay;                  // in Rs per day

    public BaseItem(int id, String title, String author, int finePerDay) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.finePerDay = finePerDay;
    }

    @Override
    public void borrow(String durationStr) throws Exception {
        if (!available && !isAvailable()) {
            throw new Exception("Item not available for borrowing");
        }
        Duration duration;
        try {
            // Parse ISO-8601 duration format (e.g. PT72H for 72 hours)
            duration = Duration.parse(durationStr);
        } catch (DateTimeParseException e) {
            throw new Exception("Invalid borrow duration format. Use ISO-8601 format, e.g. PT72H", e);
        }
        long now = System.currentTimeMillis();
        borrowEndEpochMillis = now + duration.toMillis();
        available = false;
    }

    @Override
    public void returnItem() throws Exception {
        if (available) {
            throw new Exception("This item was not borrowed.");
        }
        available = true;
        borrowEndEpochMillis = 0;
    }

    @Override
    public boolean isAvailable() {
        if (available) {
            return true;
        }
        // If past borrow end time, item is now available
        if (System.currentTimeMillis() > borrowEndEpochMillis) {
            available = true;
            borrowEndEpochMillis = 0;
            return true;
        }
        return false;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}

// Book class with page count specialization
class Book extends BaseItem {
    private int pageCount;

    public Book(int id, String title, String author, int finePerDay, int pageCount) {
        super(id, title, author, finePerDay);
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }
}

// Playable interface for audiobooks
interface Playable {
    void play() throws Exception;
}

// AudioBook implements Playable
class AudioBook extends BaseItem implements Playable {
    private Duration playbackDuration;

    public AudioBook(int id, String title, String author, int finePerDay, Duration playbackDuration) {
        super(id, title, author, finePerDay);
        this.playbackDuration = playbackDuration;
    }

    @Override
    public void play() throws Exception {
        if (available) {
            throw new Exception("Audiobook is not borrowed.");
        }
        // Sample play simulation
        System.out.println("Playing audiobook: " + title + " by " + author);
    }

    public Duration getPlaybackDuration() {
        return playbackDuration;
    }
}

// EMagazine class with archive functionality
class EMagazine extends BaseItem {
    private int issueNumber;
    private boolean archived = false;

    public EMagazine(int id, String title, String author, int finePerDay, int issueNumber) {
        super(id, title, author, finePerDay);
        this.issueNumber = issueNumber;
    }

    public void archiveIssue() {
        archived = true;
        System.out.printf("Archived e-magazine issue %d: %s%n", issueNumber, title);
    }

    public boolean isArchived() {
        return archived;
    }

    public int getIssueNumber() {
        return issueNumber;
    }
}

// Utility to safely parse int ID from string
class Utils {
    public static int parseID(String idString) throws Exception {
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid ID format", e);
        }
    }
}

// Sample usage
public class LibraNetExample {
    public static void main(String[] args) {
        try {
            int bookId = Utils.parseID("101");
            Book book = new Book(bookId, "Effective Java", "Joshua Bloch", 10, 416);
            book.borrow("PT72H"); // 72 hours
            System.out.println("Borrowed book: " + book.getTitle() + ", pages: " + book.getPageCount());
            book.returnItem();
            System.out.println("Book returned, availability: " + book.isAvailable());

            AudioBook audioBook = new AudioBook(102, "Java Concurrency", "Brian Goetz", 15, Duration.ofHours(4));
            audioBook.borrow("PT48H");
            audioBook.play();

            EMagazine emag = new EMagazine(103, "Tech Today", "Editors", 5, 27);
            emag.archiveIssue();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
