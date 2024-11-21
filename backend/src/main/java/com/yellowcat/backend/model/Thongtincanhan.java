package com.yellowcat.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "thongtincanhan")
public class Thongtincanhan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thongtincanhan_id_gen")
    @SequenceGenerator(name = "thongtincanhan_id_gen", sequenceName = "thongtincanhan_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "hoten", nullable = false, length = 100)
    private String hoten;

    @Column(name = "tuoi")
    private Integer tuoi;

    @Size(max = 10)
    @Column(name = "gioitinh", length = 10)
    private String gioitinh;

    @Size(max = 255)
    @NotNull
    @Column(name = "idtaikhoan", nullable = false)
    private String idtaikhoan;

    @Size(max = 20)
    @Column(name = "sdt", length = 20)
    private String sdt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public Integer getTuoi() {
        return tuoi;
    }

    public void setTuoi(Integer tuoi) {
        this.tuoi = tuoi;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getIdtaikhoan() {
        return idtaikhoan;
    }

    public void setIdtaikhoan(String idtaikhoan) {
        this.idtaikhoan = idtaikhoan;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

}