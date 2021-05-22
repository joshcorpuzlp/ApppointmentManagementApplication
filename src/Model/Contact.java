package Model;

public class Contact {
    private int contactId;
    private String contactName;

    /**
     * Method that returns the contact Id
     * @return An integer value.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * A method that sets the Contact id
     * @param contactId Passes an integer value
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * A method that returns the Contact anme
     * @return a String object
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Method that sets the Contact name
     * @param contactName Passes a String object
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Constructor method for the Contact class
     * @param contactId Passes an integer value
     * @param contactName Passes a String object
     */
    public Contact(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }
}
