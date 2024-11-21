import  ThuCungKhachHang from "~/models/ThuCungKhachHang";
import  DichVu from "~/models/DichVu";
import  CaLichHenKhachDat from "~/models/CaLichHenKhachDat";

export default class DichVuKhachDat{
    idlichhen: number;
    idkhachhang: string ;
    thucung: ThuCungKhachHang;
    dichvu: DichVu;
    emailnguoidat: string;
    ngaydat : Date;
    trangthai: boolean;
    calichhen :CaLichHenKhachDat;

    constructor(idlichhen: number, idkhachhang: string, thucung: {
        cannang: number;
        giongcho: string;
        id: number;
        idtaikhoan: string;
        image: string;
        tenthuocung: string;
        tuoi: number
    }, dichvu: {
        anh: string | null;
        giatien: number;
        id: number;
        mota: string;
        tendichvu: string;
        trangthai: boolean
    }, emailnguoidat: string, ngaydat: any, trangthai: boolean, calichhen: {
        id: number;
        tenca: string;
        thoigianca: Date;
        trangthai: boolean
    }) {
        this.idlichhen = idlichhen;
        this.idkhachhang = idkhachhang;
        this.thucung = thucung;
        this.dichvu = dichvu;
        this.emailnguoidat = emailnguoidat;
        this.ngaydat = ngaydat;
        this.trangthai = trangthai;
        this.calichhen = calichhen;
    }
}