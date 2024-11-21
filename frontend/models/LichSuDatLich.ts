export class Lichhen {
    id: number;
    idkhachhang: string;
    thucung: Thucung;
    dichvu: Dichvu;
    emailNguoiDat: string;
    date: string; // LocalDate trong Java là string trong TypeScript
    trangthai: number;
    idcalichhen: Calichhen;

    constructor(
        id: number,
        idkhachhang: string,
        thucung: Thucung,
        dichvu: Dichvu,
        emailNguoiDat: string,
        date: string,
        trangthai: number,
        idcalichhen: Calichhen
    ) {
        this.id = id;
        this.idkhachhang = idkhachhang;
        this.thucung = thucung;
        this.dichvu = dichvu;
        this.emailNguoiDat = emailNguoiDat;
        this.date = date;
        this.trangthai = trangthai;
        this.idcalichhen = idcalichhen;
    }
}

export class Thucung {
    // Định nghĩa các thuộc tính cần thiết cho Thucung
}

export class Dichvu {
    // Định nghĩa các thuộc tính cần thiết cho Dichvu
}

export class Calichhen {
    // Định nghĩa các thuộc tính cần thiết cho Calichhen
}
