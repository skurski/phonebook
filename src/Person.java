/**
 * Every instance of class Person
 * represents one contact in phone book
 * @author Peter Skurski
 */
class Person {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public Person(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.phone = phone.trim();
        this.email = email.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return String.format(firstName + " " + lastName + " " +
                phone + " " + email);
    }
}

