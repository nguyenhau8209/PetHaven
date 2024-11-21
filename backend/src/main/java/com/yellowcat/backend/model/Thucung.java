package com.yellowcat.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "thucung")
public class Thucung {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thucung_id_gen")
    @SequenceGenerator(name = "thucung_id_gen", sequenceName = "thucung_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "ten", nullable = false, length = 100)
    private String ten;

    @Column(name = "cannang")
    private Double cannang;

    @Column(name = "tuoi")
    private Integer tuoi;

    @Size(max = 100)
    @Column(name = "giong", length = 100)
    private String giong;

    @Size(max = 255)
    @NotNull
    @Column(name = "idtaikhoan", nullable = false)
    private String idtaikhoan;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Double getCannang() {
        return cannang;
    }

    public void setCannang(Double cannang) {
        this.cannang = cannang;
    }

    public Integer getTuoi() {
        return tuoi;
    }

    public void setTuoi(Integer tuoi) {
        this.tuoi = tuoi;
    }

    public String getGiong() {
        return giong;
    }

    public void setGiong(String giong) {
        this.giong = giong;
    }

    public String getIdtaikhoan() {
        return idtaikhoan;
    }

    public void setIdtaikhoan(String idtaikhoan) {
        this.idtaikhoan = idtaikhoan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}