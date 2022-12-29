package sevenislands.punctuation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PunctuationService {
    
    private PunctuationRepository punctuationRepository;

    @Autowired
    public PunctuationService(PunctuationRepository punctuationRepository) {
        this.punctuationRepository = punctuationRepository;
    }

    @Transactional
    public List<Object []> findPunctuations() {
        return punctuationRepository.findPunctuations();
    }
}
