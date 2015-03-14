package phonebook.database;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
 * Model read data from the file
 * and stored it in linked list also
 * save data to file
 * @author Peter Skurski
 */
public class Model {
    //path to file with contacts
    private static final String PATH = "contacts.txt";
    private static Scanner __scanner; //input object
    private static PrintWriter __printer; //output object
    private static List<Person> __persons = new LinkedList<Person>(); //linked list with Persons

    public Model() {}

    /*
     * Read data from file and insert it into linked list
     */
    public static void read() {
        try {
        	__scanner = new Scanner(Paths.get(PATH));
            while(__scanner.hasNext()) {
                Person person = new Person(__scanner.next(), __scanner.next(), __scanner.next(),
                		__scanner.next());
                __persons.add(person);
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            __scanner.close();
        }
    }

    /*
     * Return linked list with Persons
     * @return List<Person>
     */
    public static List<Person> getPersons() {
        return __persons;
    }

    /*
     * Add new Person to linked list
     * @param firstName
     * @param lastName
     * @param phone
     * @param email
     */
    public static void addPerson(String firstName, String lastName,
                          String phone, String email) {
    	__persons.add(new Person(firstName, lastName,
                phone, email));
    }

    /*
     * Write linked list to output file
     */
    public static void write() {
        try {
            __printer = new PrintWriter(PATH);
            for(Person per: __persons)
                __printer.println(per.toFile());

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            __printer.close();
        }
    }

    /*
     * Return size of linked list
     * @return int
     */
    public static int getSize() {
        return __persons.size();
    }
}
