package phonebook.database;
/*
 * Every instance of class Person
 * represents one contact in phone book
 * @author Peter Skurski
 */
public class Person implements Comparable<Person> {
    private String _firstName;
    private String _lastName;
    private String _phone;
    private String _email;

    public Person(String firstName, String lastName, String phone, String email) {
    	_firstName = firstName.trim();
    	_lastName = lastName.trim();
    	_phone = phone.trim();
    	_email = email.trim();
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public String getPhone() {
        return _phone;
    }

    public String getEmail() {
        return _email;
    }
    
    public String toFile() {
    	return String.format(_firstName+" "+_lastName+" "+
    				_phone+" "+_email);
    }

    public String toString() {
        return String.format(_firstName + " " + _lastName + " " +
                _phone + " " + _email);
    }
    
    public int compareTo(Person per) {
    	if(per == null) throw new NullPointerException();
        int ret = _lastName.compareTo(per._lastName);
        if (ret == 0) ret = _firstName.compareTo(per._firstName);
        if (ret == 0) ret = _email.compareTo(per._email);       
        return ret;
    }
}

