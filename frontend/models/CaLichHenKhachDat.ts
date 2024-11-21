export default class CaLichHenKhachDat {
    id: number;
    tenca: string;
    thoigianca: Date;
    trangthai: boolean;

    constructor(id: number, tenca: string, thoigianca: Date, trangthai: boolean) {
        this.id = id;
        this.tenca = tenca;
        this.thoigianca = thoigianca;
        this.trangthai = trangthai;
    }
}