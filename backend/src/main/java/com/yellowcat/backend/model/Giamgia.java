package com.yellowcat.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "giamgia")
public class Giamgia {
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "giamgia_id_gen")
    @SequenceGenerator(name = "giamgia_id_gen", sequenceName = "giamgia_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @JsonProperty
    @Column(name = "phantramgiam")
    private Integer phantramgiam;

    @NotNull
    @JsonProperty
    @Column(name = "ngaybatdau")
    private LocalDate ngaybatdau;

    @NotNull
    @JsonProperty
    @Column(name = "ngayketthuc")
    private LocalDate ngayketthuc;

    @NotNull
    @JsonProperty
    @Column(name = "mota", length = Integer.MAX_VALUE)
    private String mota;

    @NotNull
    @JsonProperty
    @Column(name = "trangthai")
    private Boolean trangthai;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPhantramgiam() {
        return phantramgiam;
    }

    public void setPhantramgiam(Integer phantramgiam) {
        this.phantramgiam = phantramgiam;
    }

    public LocalDate getNgaybatdau() {
        return ngaybatdau;
    }

    public void setNgaybatdau(LocalDate ngaybatdau) {
        this.ngaybatdau = ngaybatdau;
    }

    public LocalDate getNgayketthuc() {
        return ngayketthuc;
    }

    public void setNgayketthuc(LocalDate ngayketthuc) {
        this.ngayketthuc = ngayketthuc;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public Boolean getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(Boolean trangthai) {
        this.trangthai = trangthai;
    }
}