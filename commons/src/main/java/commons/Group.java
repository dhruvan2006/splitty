package commons;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String groupName;
    @OneToMany
    private List<Participant> participants;
    private boolean pinned;
    @ManyToOne
    private Participant groupLeader;


    public Group(String groupName) {
        this.groupName = groupName;
        this.participants = new ArrayList<>();
        this.pinned = false; // Set a pin so that a group is displayed first
        this.groupLeader = null; // Initially, no group leader
    }

    public Group() {

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    /** 
    if we plan to create a group from scratch
    */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public boolean isPinned() {
        return pinned;
    }

    public long getId() {
            return id;
    }

    public void setId(long id) {
            this.id = id;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public Participant getGroupLeader() {
        if (groupLeader==null) System.out.println("There is no group leader assigned");
        return groupLeader;
    }

    public void setGroupLeader(Participant groupLeader) {
        if (participants.contains(groupLeader)) this.groupLeader = groupLeader;
        else System.out.println("Member is not part of the group");
    }

    /** 
    * if we plan to add a person to an existing group
    */
    public void addToGroup(Participant participant) {
        if (participant!=null) participants.add(participant);
        else System.out.println("Could not find the participant you wish to add.");
    }

    public void removeFromGroup(Participant requester, Participant participantToRemove) {
        if (requester.equals(groupLeader) && participants.contains(participantToRemove)) {
            participants.remove(participantToRemove);
        } else if (participants.contains(participantToRemove)){
            System.out.println("Only the group leader can remove members from the group.");
        }
        else {
            System.out.println("Invalid participant.");
        }
    }

    @Override
    public String toString(){
        String result = groupName + " ,ID: " + id + " ,with the following participants:\n";
        for (Participant p:participants){
            result+= p.getFullName() + "\n";
        }
        return result;    
    }
}
