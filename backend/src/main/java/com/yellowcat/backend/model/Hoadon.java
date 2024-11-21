package com.yellowcat.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "hoadon")
public class Hoadon {
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hoadon_id_gen")
    @SequenceGenerator(name = "hoadon_id_gen", sequenceName = "hoadon_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idlichhen")
    private Lichhen idlichhen;

    @JsonProperty
    @Column(name = "date")
    private LocalDateTime date;

    @JsonProperty
    @NotNull
    @Column(name = "sotien", nullable = false)
    private Double sotien;

    @JsonProperty
    @Column(name = "ngaythanhtoan")
    private LocalDateTime ngaythanhtoan;

    @JsonProperty
    @Size(max = 50)
    @Column(name = "phuongthucthanhtoan", length = 50)
    private String phuongthucthanhtoan;

    @JsonProperty
    @NotNull
    @Column(name = "trangthai", nullable = false)
    private Integer trangthai;

    @JsonProperty
    @Size(max = 255)
    @Column(name = "nguoithanhtoan")
    private String nguoithanhtoan;

    @JsonProperty
    @Size(max = 255)
    @NotNull
    @Column(name = "magiaodich", nullable = false)
    private String magiaodich;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idgiamgia")
    private Giamgia idgiamgia;

    public Giamgia getIdgiamgia() {
        return idgiamgia;
    }

    public void setIdgiamgia(Giamgia idgiamgia) {
        this.idgiamgia = idgiamgia;
    }

    public String getMagiaodich() {
        return magiaodich;
    }

    public void setMagiaodich(String magiaodich) {
        this.magiaodich = magiaodich;
    }

    public String getNguoithanhtoan() {
        return nguoithanhtoan;
    }

    public void setNguoithanhtoan(String nguoithanhtoan) {
        this.nguoithanhtoan = nguoithanhtoan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Lichhen getIdlichhen() {
        return idlichhen;
    }

    public void setIdlichhen(Lichhen idlichhen) {
        this.idlichhen = idlichhen;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getSotien() {
        return sotien;
    }

    public void setSotien(Double sotien) {
        this.sotien = sotien;
    }

    public LocalDateTime getNgaythanhtoan() {
        return ngaythanhtoan;
    }

    public void setNgaythanhtoan(LocalDateTime ngaythanhtoan) {
        this.ngaythanhtoan = ngaythanhtoan;
    }

    public String getPhuongthucthanhtoan() {
        return phuongthucthanhtoan;
    }

    public void setPhuongthucthanhtoan(String phuongthucthanhtoan) {
        this.phuongthucthanhtoan = phuongthucthanhtoan;
    }

    public Integer getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(Integer trangthai) {
        this.trangthai = trangthai;
    }


}