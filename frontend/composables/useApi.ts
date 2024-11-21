import { useAuth } from "./useAuth";
import { useToast } from "vue-toastification";

export const useApi = () => {
  const { getAccessToken, refreshAccessToken } = useAuth();
  const toast = useToast();

  const makeRequest = async (url: string, options: any = {}) => {
    try {
      const response = await executeRequest(url, options);
      return response;
    } catch (error: any) {
      if (error.status === 401) {
        try {
          await refreshAccessToken();
          return await executeRequest(url, options);
        } catch (refreshError) {
          toast.error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
          throw refreshError;
        }
      }
      throw error;
    }
  };

  const executeRequest = async (url: string, options: any = {}) => {
    const token = getAccessToken();
    const response = await $fetch(url, {
      ...options,
      headers: {
        ...options.headers,
        Authorization: `Bearer ${token}`,
      },
    });
    return response;
  };

  return {
    makeRequest,
  };
};
