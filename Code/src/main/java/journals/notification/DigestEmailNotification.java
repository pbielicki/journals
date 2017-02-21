package journals.notification;

import org.springframework.stereotype.Service;

import journals.dto.DigestEmailDTO;

@Service
public class DigestEmailNotification extends AbstractEmailNotification<DigestEmailDTO> {
}
