package com.dab.doctorappointment.scheduler;

import com.dab.doctorappointment.entity.DoctorSlot;
import com.dab.doctorappointment.entity.SlotStatus;
import com.dab.doctorappointment.repository.DoctorSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SlotReleaseScheduler {

    private final DoctorSlotRepository doctorSlotRepository;

    private static final int LOCK_TIMEOUT_MINUTES = 5;

    // Every 1 minute (60000 ms) run aagும்
    @Scheduled(fixedRate = 60000)
    public void releaseExpiredLocks() {

        LocalDateTime expiryThreshold = LocalDateTime.now().minusMinutes(LOCK_TIMEOUT_MINUTES);

        List<DoctorSlot> expiredSlots = doctorSlotRepository.findByStatusAndLockedAtBefore(
                SlotStatus.LOCKED, expiryThreshold
        );

        for (DoctorSlot slot : expiredSlots) {
            slot.setStatus(SlotStatus.AVAILABLE);
            slot.setLockedAt(null);
            doctorSlotRepository.save(slot);
            System.out.println("Released expired lock for slot ID: " + slot.getId());
        }
    }
}
