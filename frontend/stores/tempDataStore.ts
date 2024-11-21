import type DichVuKhachDat from "~/models/DichVuKhachDat";

export const useTempDataStore = defineStore('tempData', {
  state: () => ({
    dichVuKhachDat: null as DichVuKhachDat | null,
  }),

  actions: {
    setDichVuKhachDat(data: DichVuKhachDat) {
      this.dichVuKhachDat = data;
    },

    clearDichVuKhachDat() {
      this.dichVuKhachDat = null;
    },
  },

  getters: {
    getDichVuKhachDat(): DichVuKhachDat | null {
      return this.dichVuKhachDat;
    },
  },
});