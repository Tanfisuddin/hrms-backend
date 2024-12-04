package com.imw.commonmodule.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imw.commonmodule.enums.attendance.AttendanceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        indexes = {
                @Index(name = "idx_user_id_check_in_date", columnList = "user_id, check_in_date")
        }
)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Check in location
    private String checkInLocation;

    private Double checkInLatitude;

    private Double checkInLongitude;

//    Check Out Location
    private String checkOutLocation;

    private Double checkOutLatitude;

    private Double checkOutLongitude;

    private Boolean isRegularized = true;

    @Temporal(TemporalType.DATE)
    private LocalDate checkInDate;

    @Temporal(TemporalType.TIME)
    private LocalTime checkInTime;

    @Temporal(TemporalType.TIME)
    private LocalTime checkOutTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

