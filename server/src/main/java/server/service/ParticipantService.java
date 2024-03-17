package server.service;

import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> getAllParticipants(int page, int size, String sortBy, String sortOrder) {
        // Implement pagination and sorting logic if needed
        return participantRepository.findAll();
    }

    public Optional<Participant> getParticipantById(Long id) {
        return participantRepository.findById(id);
    }

    public Participant addParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    public List<Participant> findParticipantByName(String name) {
        return participantRepository.findByUserName(name);
    }

    public List<Participant> findParticipantByPartialName(String substring) {
        return participantRepository.findByPartialName(substring);
    }
}
