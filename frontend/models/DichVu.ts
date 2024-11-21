export default class DichVu {
    id: number;
    tendichvu: string;
    mota: string;
    anh: string | null;
    giatien: number;
    trangthai: boolean;

    constructor(id: number, tendichvu: string, mota: string, anh: string | null, giatien: number, trangthai: boolean) {
        this.id = id;
        this.tendichvu = tendichvu;
        this.mota = mota;
        this.anh = anh;
        this.giatien = giatien;
        this.trangthai = trangthai;
    }
}
