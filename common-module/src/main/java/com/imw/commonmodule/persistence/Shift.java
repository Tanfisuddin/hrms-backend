package com.imw.commonmodule.persistence;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String shiftName;

    @NotNull
    private Boolean isGeoFencingEnabled = Boolean.FALSE;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private Integer radius;

    @NotNull
    @Temporal(TemporalType.TIME)
    private LocalTime shiftStartTime;

    @NotNull
    @Temporal(TemporalType.TIME)
    private LocalTime shiftEndTime;

    @NotNull
    @Temporal(TemporalType.TIME)
    private LocalTime bufferTimeTo;

    @NotNull
    private Double workingHour;

    @JsonIgnore
    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY)
    private List<User> users;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
