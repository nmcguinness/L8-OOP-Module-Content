package t09_interface.challenges.ce02;

import java.util.Objects;

/**
 * Simple contact model.
 * Equality and hashing are based on normalised (trimmed, lower-case) email.
 */
public class Contact {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String country;
    private String company;
    private boolean followUp;
    private int turnoverThousands;

    public Contact(
        String id,
        String name,
        String email,
        String phone,
        String department,
        String country,
        String company,
        boolean followUp,
        int turnoverThousands) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        this.id = id;
        this.name = name;
        this.email = email.trim();
        this.phone = phone;
        this.department = department;
        this.country = country;
        this.company = company;
        this.followUp = followUp;
        this.turnoverThousands = turnoverThousands;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getCountry() {
        return country;
    }

    public String getCompany() {
        return company;
    }

    public boolean isFollowUp() {
        return followUp;
    }

    public int getTurnoverThousands() {
        return turnoverThousands;
    }

    public String getNormalisedEmail() {
        return email.trim().toLowerCase();
    }

    public String getEmailDomain() {
        int atIndex = email.indexOf('@');
        if (atIndex < 0 || atIndex == email.length() - 1) {
            return "";
        }
        return email.substring(atIndex + 1).toLowerCase();
    }

    // "with" methods for formatters (name + email only are needed here)

    public Contact withName(String name) {
        return new Contact(
            id,
            name,
            email,
            phone,
            department,
            country,
            company,
            followUp,
            turnoverThousands
        );
    }

    public Contact withEmail(String email) {
        return new Contact(
            id,
            name,
            email,
            phone,
            department,
            country,
            company,
            followUp,
            turnoverThousands
        );
    }

    @Override
    public String toString() {
        return id + " - " + name + " (" + email + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        Contact contact = (Contact) o;
        return getNormalisedEmail().equals(contact.getNormalisedEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNormalisedEmail());
    }
}
