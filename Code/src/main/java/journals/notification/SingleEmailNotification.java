package journals.notification;

import org.springframework.stereotype.Service;

import journals.dto.SingleJournalEmailDTO;

@Service
public class SingleEmailNotification extends AbstractEmailNotification<SingleJournalEmailDTO> {
}
