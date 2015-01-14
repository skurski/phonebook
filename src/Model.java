import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Model read data from the file
 * and stored it in linked list also
 * save data to file
 * @author Peter Skurski
 */
class Model {
    //path to file with contacts
    private static String path = "contacts.txt";
    private static Scanner file; //input object
    private static PrintWriter outFile; //output object
    private static List<Person> personList; //linked list with Persons

    /**
     * Non-argument constructor
     * Create linked list with Persons
     * Read from input file and fill
     * created earlier linked list
     */
    public Model() {}

    public static void createLinkedList() {
        personList = new LinkedList<Person>();

        try {
            file = new Scanner(Paths.get(path));
            while(file.hasNext()) {
                Person person = new Person(file.next(), file.next(), file.next(),
                        file.next());
                personList.add(person);
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            file.close();
        }
    }

    /**
     * Return linked list with Persons
     * @return linked list<Person>
     */
    public static List<Person> getPersonList() {
        return personList;
    }

    /**
     * Add new Person to linked list
     * @param firstName
     * @param lastName
     * @param phone
     * @param email
     */
    public static void addPerson(String firstName, String lastName,
                          String phone, String email) {
        personList.add(new Person(firstName, lastName,
                phone, email));
    }

    /**
     * Write linked list to output file
     */
    public static void writeListToFile() {
        try {
            outFile = new PrintWriter(path);
            for(Person per: personList)
                outFile.println(per.toString());

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            outFile.close();
        }
    }

    /**
     * return size of linked list
     * @return int
     */
    public static int personListSize() {
        return personList.size();
    }
}
