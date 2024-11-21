package com.yellowcat.backend.DTO;

import com.yellowcat.backend.model.Thucung;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DatLichDTO {
    @NotNull
    private Thucung idThuCung;
    @NotNull
    private String date;
    @NotNull
    private Integer idcalichhen;
    @NotNull
    private Integer idDichVu;



    public DatLichDTO() {
    }

    public Thucung getIdThuCung() {
        return idThuCung;
    }

    public void setIdThuCung(Thucung idThuCung) {
        this.idThuCung = idThuCung;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getIdcalichhen() {
        return idcalichhen;
    }

    public void setIdcalichhen(Integer idcalichhen) {
        this.idcalichhen = idcalichhen;
    }


    public Integer getIdDichVu() {
        return idDichVu;
    }

    public void setIdDichVu(Integer idDichVu) {
        this.idDichVu = idDichVu;
    }
}
