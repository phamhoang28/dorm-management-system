package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.Billing;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.UserInfo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
    Optional<ResidentHistory> findTopByUserInfo_IdOrderByIdDesc(long id);

    Optional<ResidentHistory> findTopByUserInfo_IdAndSlot_Room_Dorm_Branch_IdOrderByIdDesc(long userId, long brandId);
    Optional<ResidentHistory> findByUserInfo_IdOrderByEndDate(long id);
    // all nhiều tháng
    @Query(value = "select reh.* from resident_history as reh inner join slot on reh.slot_id = slot.id " +
            "inner join room on slot.room_id = room.id inner join electric_water_usage as ewu on room.id = ewu.room_id " +
            "where  (ewu.created_date BETWEEN reh.start_date And reh.end_date) " +
            "And ewu.room_id=? and ewu.id=?",nativeQuery = true)
    List<ResidentHistory> findResidentsByRoomId(Long roomid, Long id);
    //all 1 tháng
    @Query(value = "select reh.* from resident_history as reh inner join slot on reh.slot_id = slot.id " +
            "inner join room on slot.room_id = room.id " +
            "where  room_id=?  and (? BETWEEN reh.start_date And reh.end_date)",nativeQuery = true)
    List<ResidentHistory> findResidentsByRoomIdInMonth(Long roomid , LocalDate month_pay );


;
    @Query(value = "select * from resident_history where :date between start_date and end_date", nativeQuery = true)
    List<ResidentHistory> findByDateBetweenStartDateAndEndDate(@Param("date")Date date);

    @Query(value = "select * from resident_history where slot_id = :slot_id and (:date between start_date and end_date)", nativeQuery = true)
    ResidentHistory findBySlotIdWithDateBetweenStartDateAndEndDate(@Param("slot_id")long id,@Param("date") Date date);

    List<ResidentHistory> findBySlot_IdAndCheckinDateIsNull(Long slotId);

    List<ResidentHistory> findBySlot_IdAndCheckoutDateIsNull(Long slotId);
    @Query(value = "select top 1 * from resident_history where resident_id = :resident_id  and (GETDATE() between start_date and end_date)", nativeQuery = true)
    Optional<ResidentHistory> findCurrentSlotOfResident(@Param("resident_id") long residentId);

}
