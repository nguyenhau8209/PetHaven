import { defineStore } from 'pinia'
import ThuCungKhachHang from "~/models/ThuCungKhachHang";

interface UserInfo {
  name: string
  roles: string[]
  listThuCung : ThuCungKhachHang[]
}

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null as UserInfo | null,
    isLoggedIn: false,
  }),
  actions: {
    setUserInfo(userInfo: UserInfo) {
      this.userInfo = userInfo
      this.isLoggedIn = true
    },
    clearUserInfo() {
      this.userInfo = null
      this.isLoggedIn = false
    },
  },
})