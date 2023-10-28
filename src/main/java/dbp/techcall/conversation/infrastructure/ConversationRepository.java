package dbp.techcall.conversation.infrastructure;

import dbp.techcall.conversation.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}
