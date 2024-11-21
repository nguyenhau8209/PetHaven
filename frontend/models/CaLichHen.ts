export class Calichhen {
    id: number;
    tenca: string;
    thoigianca: string; // LocalTime trong Java là string trong TypeScript
    trangthai: boolean;

    constructor(id: number, tenca: string, thoigianca: string, trangthai: boolean) {
        this.id = id;
        this.tenca = tenca;
        this.thoigianca = thoigianca;
        this.trangthai = trangthai;
    }
}
