package com.yellowcat.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "dichvu")
public class Dichvu {
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dichvu_id_gen")
    @SequenceGenerator(name = "dichvu_id_gen", sequenceName = "dichvu_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonProperty
    @NotNull
    @Column(name = "tendichvu", nullable = false, length = 100)
    private String tendichvu;

    @JsonProperty
    @NotNull
    @Column(name = "mota", nullable = false, length = Integer.MAX_VALUE)
    private String mota;

    @JsonProperty
    @NotNull
    @Column(name = "anh", length = Integer.MAX_VALUE)
    private String anh;

    @JsonProperty
    @NotNull
    @Column(name = "giatien", nullable = false)
    private Float giatien;

    @JsonProperty
    @NotNull
    @Column(name = "trangthai", nullable = false)
    private Boolean trangthai = false;

    public Boolean getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(Boolean trangthai) {
        this.trangthai = trangthai;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTendichvu() {
        return tendichvu;
    }

    public void setTendichvu(String tendichvu) {
        this.tendichvu = tendichvu;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public Float getGiatien() {
        return giatien;
    }

    public void setGiatien(Float giatien) {
        this.giatien = giatien;
    }
}