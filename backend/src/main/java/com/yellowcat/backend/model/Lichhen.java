package com.yellowcat.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "lichhen")
public class Lichhen {
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lichhen_id_gen")
    @SequenceGenerator(name = "lichhen_id_gen", sequenceName = "lichhen_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @JsonProperty
    @Column(name = "idkhachhang", nullable = false)
    private String idkhachhang;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "idthucung")
    private Thucung thucung;

    @JsonProperty
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iddichvu")
    private Dichvu dichvu;

    @JsonProperty
    @NotNull
    @Column(name = "emailnguoidat", nullable = false)  // ánh xạ chính xác tên cột
    private String emailNguoiDat;

    @NotNull
    @JsonProperty
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @JsonProperty
    @Column(name = "trangthai", nullable = false)
    private Integer trangthai ;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "idcalichhen")
    private Calichhen idcalichhen;

    @Column(name = "trangthaica")
    private Boolean trangthaica;

    @Column(name = "thoigianhuy")
    private LocalDateTime thoigianhuy;

    @Column(name = "thoigianthaydoi")
    private LocalDateTime thoigianthaydoi;

    @ColumnDefault("0")
    @Column(name = "solanthaydoi")
    private Integer solanthaydoi;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "solannhacnho", nullable = false)
    private Integer solannhacnho;

    public Integer getSolannhacnho() {
        return solannhacnho;
    }

    public void setSolannhacnho(Integer solannhacnho) {
        this.solannhacnho = solannhacnho;
    }

    public Integer getSolanthaydoi() {
        return solanthaydoi;
    }

    public void setSolanthaydoi(Integer solanthaydoi) {
        this.solanthaydoi = solanthaydoi;
    }

    public LocalDateTime getThoigianthaydoi() {
        return thoigianthaydoi;
    }

    public void setThoigianthaydoi(LocalDateTime thoigianthaydoi) {
        this.thoigianthaydoi = thoigianthaydoi;
    }

    public LocalDateTime getThoigianhuy() {
        return thoigianhuy;
    }

    public void setThoigianhuy(LocalDateTime thoigianhuy) {
        this.thoigianhuy = thoigianhuy;
    }

    public Boolean getTrangthaica() {
        return trangthaica;
    }

    public void setTrangthaica(Boolean trangthaica) {
        this.trangthaica = trangthaica;
    }

    public Calichhen getIdcalichhen() {
        return idcalichhen;
    }

    public void setIdcalichhen(Calichhen idcalichhen) {
        this.idcalichhen = idcalichhen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @Size(max = 255) @NotNull String getIdkhachhang() {
        return idkhachhang;
    }

    public void setIdkhachhang(@Size(max = 255) @NotNull String idkhachhang) {
        this.idkhachhang = idkhachhang;
    }

    public Thucung getThucung() {
        return thucung;
    }

    public void setThucung(Thucung thucung) {
        this.thucung = thucung;
    }

    public Dichvu getDichvu() {
        return dichvu;
    }

    public void setDichvu(Dichvu dichvu) {
        this.dichvu = dichvu;
    }

    public String getEmailNguoiDat() {
        return emailNguoiDat;
    }

    public void setEmailNguoiDat(String emailNguoiDat) {
        this.emailNguoiDat = emailNguoiDat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public @NotNull Integer getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(@NotNull Integer trangthai) {
        this.trangthai = trangthai;
    }
}