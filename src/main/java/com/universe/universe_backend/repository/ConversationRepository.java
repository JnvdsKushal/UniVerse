// repository/ConversationRepository.java
package com.universe.universe_backend.repository;
import com.universe.universe_backend.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("""
        SELECT c FROM Conversation c
        WHERE (c.userOne.id = :a AND c.userTwo.id = :b)
           OR (c.userOne.id = :b AND c.userTwo.id = :a)
    """)
    Optional<Conversation> findBetween(@Param("a") UUID a, @Param("b") UUID b);

    @Query("SELECT c FROM Conversation c WHERE c.userOne.id = :userId OR c.userTwo.id = :userId")
    List<Conversation> findAllForUser(@Param("userId") UUID userId);
}