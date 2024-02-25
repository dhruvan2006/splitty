package commons;+


import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private List<Participant> participants;
    private boolean pinned;
    private Participant groupLeader;

    public Group(String groupName) {
        this.groupName = groupName;
        this.participants = new ArrayList<>();
        this.pinned = false; // Set a pin so that a group is displayed first
        this.groupLeader = null; // Initially, no group leader
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

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public Participant getGroupLeader() {
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
}
