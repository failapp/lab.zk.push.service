package cl.architeq.zkpush.repository;

import cl.architeq.zkpush.model.EventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventLogRepository extends CrudRepository<EventLog, Long>,
        PagingAndSortingRepository<EventLog, Long> {

    Page<EventLog> findBySync(boolean sync, Pageable pageable);

    Page<EventLog> findBySyncAndCustomerId(boolean sync, String customerId, Pageable pageable);

    //List<People> findDistinctByNameNotIn(List<String> names);
    //List<EventLog> findDistinctByCustomerId

    @Query("SELECT DISTINCT e.customerId FROM EventLog e WHERE e.sync = false")
    List<String> searchDistinctCustomerIdAndFlagSync();

    Page<EventLog> findByEventDate(String eventDate, Pageable pageable);

    Page<EventLog> findByEventDateBetween(String from, String to, Pageable pageable);

    Page<EventLog> findByEventDateTimeBetween(String from, String to, Pageable pageable);

    Optional<EventLog> findByEventDateTimeAndUserIdAndMacDevice(String eventDateTime, String userId, String macAddr);

    Page<EventLog> findByEventDateAndUserIdAndMacDevice(String eventDate, String userId, String macAddr, Pageable pageable);

    Page<EventLog> findByEventDateAndUserId(String eventDate, String userId, Pageable pageable);

    Page<EventLog> findByEventDateAndCustomerId(String eventDate, String userId, Pageable pageable);

}
