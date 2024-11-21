interface Thucung {
    id: number;
    ten: string;
    cannang: number;
    tuoi: number;
    giong: string;
    idtaikhoan: string;
    image: string | null;
}

interface Dichvu {
    id: number;
    tendichvu: string;
    mota: string;
    anh: string | null;
    giatien: number;
    trangthai: boolean;
}

interface Idlichhen {
    id: number;
    idkhachhang: string;
    thucung: Thucung;
    dichvu: Dichvu;
    emailNguoiDat: string;
    date: string;
    trangthai: number;
    idcalichhen: {
        id: number;
        tenca: string;
        thoigianca: string;
        trangthai: boolean;
    };
    trangthaica: boolean;
    thoigianhuy: string | null;
    thoigianthaydoi: string | null;
    solanthaydoi: number;
}

interface Giamgia {
    id: number;
    phantramgiam: number;
    ngaybatdau: string;
    ngayketthuc: string;
    mota: string;
    trangthai: boolean;
}

interface Transaction {
    id: number;
    idlichhen: Idlichhen;
    date: string;
    sotien: number;
    ngaythanhtoan: string | null;
    phuongthucthanhtoan: string;
    trangthai: number;
    nguoithanhtoan: string | null;
    magiaodich: string;
    idgiamgia: Giamgia;
}

class TransactionEntity {
    id: number;
    idlichhen: Idlichhen;
    date: string;
    sotien: number;
    ngaythanhtoan: string | null;
    phuongthucthanhtoan: string;
    trangthai: number;
    nguoithanhtoan: string | null;
    magiaodich: string;
    idgiamgia: Giamgia;

    constructor(data: Transaction) {
        this.id = data.id;
        this.idlichhen = data.idlichhen;
        this.date = data.date;
        this.sotien = data.sotien;
        this.ngaythanhtoan = data.ngaythanhtoan;
        this.phuongthucthanhtoan = data.phuongthucthanhtoan;
        this.trangthai = data.trangthai;
        this.nguoithanhtoan = data.nguoithanhtoan;
        this.magiaodich = data.magiaodich;
        this.idgiamgia = data.idgiamgia;
    }

    toObject() {
        return {
            id: this.id,
            idlichhen: this.idlichhen,
            date: this.date,
            sotien: this.sotien,
            ngaythanhtoan: this.ngaythanhtoan,
            phuongthucthanhtoan: this.phuongthucthanhtoan,
            trangthai: this.trangthai,
            nguoithanhtoan: this.nguoithanhtoan,
            magiaodich: this.magiaodich,
            idgiamgia: this.idgiamgia,
        };
    }
}

export default TransactionEntity;