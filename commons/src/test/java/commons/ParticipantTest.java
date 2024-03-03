package commons;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
public class ParticipantTest {
    @Test
    public void testConstructorAndGetters() {
        String firstName = "John";
        String lastName = "Doe";
        String userName = "JD";

        Participant participant = new Participant(firstName, lastName, "x", "s", userName);


        assertEquals(firstName, participant.getFirstName());
        assertEquals(lastName, participant.getLastName());
        assertEquals(userName, participant.getUserName());
    }

    @Test
    public void testSetters() {

        Participant participant = new Participant("John", "Doe", "x", "f", "JD");

        participant.setFirstName("Jane");
        participant.setLastName("Smith");
        participant.setUserName("JS");

        assertEquals("Jane", participant.getFirstName());
        assertEquals("Smith", participant.getLastName());
        assertEquals("JS", participant.getUserName());
    }

    @Test
    public void testGetId() {
        Participant participant = new Participant("John", "Doe", "x", "f", "JD");

        assertEquals(0, participant.getId());
    }
    @Test
    public void checkGetSetId(){
        Participant p = new Participant("John", "Doe", "x", "f", "JD");
        p.setId(2020200202);
        assertEquals(2020200202, p.getId());
    }
    @Test
    public void checkGetSetFullName(){
        Participant p = new Participant("John", "Doe", "x", "f", "JD");
        assertEquals("John",p.getFirstName());
        p.setFirstName("x");
        assertEquals("x",p.getFirstName());
    }
    @Test
    public void checkGetSetLastName(){
        Participant p = new Participant("John", "Doe", "x", "f", "JD");
        assertEquals("Doe",p.getLastName());
        p.setLastName("x");
        assertEquals("x",p.getLastName());
    }


    @Test
    public void testEquals() {

        Participant p2 = new Participant("John", "Doe", "x", "f", "JD");
        Participant p1 = new Participant("John", "Doe", "x", "f", "JD");
        assertEquals(p1, p2);
    }

    @Test
	public void fullName() {
		Participant person = new Participant("John", "Hopkins", "x", "f", "JH", "male");
		var notFormal = person.getFullName();
        person.setFormal(true);
		var formal = person.getFullName();
		assertEquals("John Hopkins, known as JH", notFormal);
		assertEquals("Mr. John Hopkins, known as JH", formal);
	}

    @Test
    public void testGroups() {

        Participant p1 = new Participant("John", "Hopkins", "x", "f", "JH");
        Group group1 = new Group("Dummy group");
        Group group2 = new Group ("Dummy group 2");
        p1.enterGroup(group1);
        p1.enterGroup(group2);
        assertEquals(2,p1.getGroups().size());
        p1.leaveGroup(group1);
        assertEquals(group2,p1.getGroups().get(0));
    }



}
