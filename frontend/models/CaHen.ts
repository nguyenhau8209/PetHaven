export default class CaLichHen{
    id: number;
    tenca: String;
    thoigianca: Date;
    trangthai: boolean;

    constructor(id: number, tenca: String, thoigianca: Date, trangthai: boolean) {
        this.id = id;
        this.tenca = tenca;
        this.thoigianca = thoigianca;
        this.trangthai = trangthai;
    }
}