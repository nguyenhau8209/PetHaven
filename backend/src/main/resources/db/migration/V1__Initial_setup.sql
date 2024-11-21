CREATE TABLE TaiKhoan
(
    ID        SERIAL PRIMARY KEY,
    Username  VARCHAR(100) NOT NULL,
    Pass      VARCHAR(255) NOT NULL,
    TrangThai BOOLEAN      NOT NULL,
    Gmail     VARCHAR(255),
    Image     TEXT,
    Role      VARCHAR(50)  NOT NULL
);

CREATE TABLE GiamGia
(
    ID           SERIAL PRIMARY KEY,
    PhanTramGiam INT not null ,
    NgayBatDau   TIMESTAMP not null ,
    NgayKetThuc  TIMESTAMP not null ,
    MoTa         TEXT,
    TrangThai    BOOLEAN
);

CREATE TABLE ThongTinCaNhan
(
    ID         SERIAL PRIMARY KEY,
    HoTen      VARCHAR(100),
    Tuoi       INT,
    GioiTinh   VARCHAR(10),
    idTaiKhoan varchar(255) not null ,
    SDT        VARCHAR(20),
    Image      VARCHAR(100)
);

CREATE TABLE DichVu
(
    ID        SERIAL PRIMARY KEY,
    TenDichVu VARCHAR(100) NOT NULL,
    MoTa      TEXT         NOT NULL,
    Anh       TEXT,
    GiaTien   INT          NOT NULL,
    TrangThai   BOOLEAN NOT NULL
);

CREATE TABLE ThuCung
(
    ID         SERIAL PRIMARY KEY,
    Ten        VARCHAR(100) NOT NULL,
    CanNang    FLOAT,
    Tuoi       INT,
    Giong      VARCHAR(100),
    IDTaiKhoan VARCHAR(255) not null ,
    Image      TEXT
);

CREATE TABLE CaLichHen
(
    ID          SERIAL PRIMARY KEY,
    TenCa varchar(20),
    ThoiGianCa TIME NOT NULL,
    TrangThai   BOOLEAN
);

CREATE TABLE LichHen
(
    ID          SERIAL PRIMARY KEY,
    idKhachHang VARCHAR(255) NOT NULL ,
    idThuCung   INT REFERENCES ThuCung (ID),
    idDichVu    INT REFERENCES DichVu (ID),
    Date        DATE NOT NULL,
    TrangThai   INT   NOT NULL,
    EmailNguoiDat VARCHAR(255) NOT NULL default '123@gmail.com',
    idCaLichHen INT REFERENCES CaLichHen(ID),
    TrangThaiCa BOOLEAN,
    ThoiGianHuy TIMESTAMP,
    ThoiGianThayDoi TIMESTAMP,
    SoLanThayDoi INT not null default 0,
    SoLanNhacNho int not null default 0
);
-- quy ước trang thai:
-- 0 : thành công
-- 1 : thất bại
-- 2 : đã hủy
-- 3 : chờ thanh toán
-- 4 : chờ xác nhận
-- 5 : Rỗng
-- 6 : Thanh toán thành công


CREATE TABLE NhacLichHen
(
    ID          SERIAL PRIMARY KEY,
    TenThongBao VARCHAR(100) NOT NULL,
    MoTa        TEXT         NOT NULL,
    idTaiKhoan  VARCHAR(255) NOT NULL ,
    ISRead      BOOLEAN,
    TrangThai   BOOLEAN
);

CREATE TABLE HoaDon
(
    ID                  SERIAL PRIMARY KEY,
    MaGiaoDich          VARCHAR(255) Not null UNIQUE ,
    idLichHen           INT REFERENCES LichHen (ID),
    Date                TIMESTAMP,
    SoTien              DECIMAL(10, 2) NOT NULL,
    NgayThanhToan       TIMESTAMP,
    PhuongThucThanhToan VARCHAR(50),
    TrangThai           INT not null,
    NguoiThanhToan      VARCHAR(255),
    idGiamGia   INT REFERENCES GiamGia (ID)
);
-- Quy ước:
-- 1: Chờ thanh toán
-- 2: Thành công
-- 3: Thất bại


CREATE TABLE DanhGia
(
    ID         SERIAL PRIMARY KEY,
    idTaiKhoan VARCHAR(255) not null ,
    SoSao      INT,
    MoTa       TEXT,
    Date       TIMESTAMP,
    TrangThai  BOOLEAN,
    idHoaDon INT REFERENCES HoaDon(ID)
);



-- Thêm dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan (Username, Pass, TrangThai, Gmail, Image, Role)
VALUES ('user1', 'pass1', TRUE, 'user1@gmail.com', NULL, 'USER'),
       ('user2', 'pass2', FALSE, 'user2@gmail.com', NULL, 'ADMIN'),
       ('user3', 'pass3', TRUE, 'user3@gmail.com', NULL, 'USER');

-- Thêm dữ liệu vào bảng ThongTinCaNhan
INSERT INTO ThongTinCaNhan (HoTen, Tuoi, GioiTinh, idTaiKhoan, SDT)
VALUES ('Nguyen Van A', 25, 'Nam', 1, '0901234567'),
       ('Tran Thi B', 30, 'Nu', 2, '0907654321'),
       ('Le Van C', 22, 'Nam', 3, '0901122334');

-- Thêm dữ liệu vào bảng GiamGia
INSERT INTO GiamGia (PhanTramGiam, NgayBatDau, NgayKetThuc, MoTa, TrangThai)
VALUES
       (20, '2024-11-01 00:00:00', '2024-11-30 23:59:59', 'Giảm giá tháng 11', TRUE),
       (15, '2024-12-01 00:00:00', '2024-12-31 23:59:59', 'Giảm giá tháng 12', TRUE);

-- Thêm dữ liệu vào bảng DichVu
INSERT INTO DichVu (TenDichVu, MoTa, Anh, GiaTien,trangthai)
VALUES ('Dịch vụ 1', 'Mô tả dịch vụ 1', NULL, 100000,true),
       ('Dịch vụ 2', 'Mô tả dịch vụ 2', NULL, 150000,true),
       ('Dịch vụ 3', 'Mô tả dịch vụ 3', NULL, 200000,true);

-- Thêm dữ liệu vào bảng ThuCung
INSERT INTO ThuCung (Ten, CanNang, Tuoi, Giong, IDTaiKhoan, Image)
VALUES ('Dog 1', 10.5, 2, 'Golden', 1, NULL),
       ('Dog 2', 8.3, 3, 'Bulldog', 2, NULL),
       ('Dog 3', 12.7, 4, 'Labrador', 3, NULL);

INSERT INTO CaLichHen (TenCa, ThoiGianCa, TrangThai)
VALUES
    ('Ca 1', '07:00:00', TRUE),
    ('Ca 2', '08:00:00', TRUE),
    ('Ca 3', '09:00:00', TRUE),
    ('Ca 4', '10:00:00', TRUE),
    ('Ca 5', '11:00:00', TRUE),
    ('Ca 6', '12:00:00', TRUE),
    ('Ca 7', '13:00:00', TRUE),
    ('Ca 8', '14:00:00', TRUE);

-- Thêm dữ liệu vào bảng LichHen
INSERT INTO LichHen (idKhachHang, idThuCung, idDichVu, Date, TrangThai , idCaLichHen,TrangThaiCa)
VALUES (1, 1, 1, '2024-10-02 ', 1 , 1,TRUE),
       (2, 2, 2, '2024-10-03 ', 2, 2,TRUE),
       (3, 3, 3, '2024-10-04 ', 3 , 3,TRUE);

-- Thêm dữ liệu vào bảng NhacLichHen
INSERT INTO NhacLichHen (TenThongBao, MoTa, idTaiKhoan, ISRead, TrangThai)
VALUES ('Nhắc lịch 1', 'Thông báo lịch hẹn 1', 1, FALSE, TRUE),
       ('Nhắc lịch 2', 'Thông báo lịch hẹn 2', 2, TRUE, FALSE),
       ('Nhắc lịch 3', 'Thông báo lịch hẹn 3', 3, FALSE, TRUE);

-- Thêm dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (idLichHen, Date, SoTien, NgayThanhToan, PhuongThucThanhToan, TrangThai,MaGiaoDich,idGiamGia)
VALUES (1, '2024-10-02 10:00:00', 90000, '2024-10-02 11:00:00', 'Offline', 1,'1',2),
       (2, '2024-10-03 14:00:00', 120000, '2024-10-03 15:00:00', 'Online', 2,'2',2),
       (3, '2024-10-04 09:00:00', 170000, '2024-10-04 10:00:00', 'Online', 3,'3',2);

-- Thêm dữ liệu vào bảng DanhGia
INSERT INTO DanhGia (idTaiKhoan, SoSao, MoTa, Date, TrangThai,idHoaDon)
VALUES (1, 5, 'Rất tốt', '2024-10-01 12:00:00', TRUE,1),
       (2, 4, 'Tốt', '2024-09-30 11:00:00', TRUE,1),
       (3, 3, 'Bình thường', '2024-09-29 10:00:00', FALSE,1);