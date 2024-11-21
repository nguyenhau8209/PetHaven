import { useUserStore } from "~/stores/user";

export const useAuth = () => {
  const userStore = useUserStore();

  const getAccessToken = () => {
    if (process.client) {
      return localStorage.getItem("access_token");
    }
    return null;
  };

  const refreshAccessToken = async () => {
    try {
      const refreshToken = localStorage.getItem("refresh_token");
      if (!refreshToken) {
        throw new Error("No refresh token available");
      }

      const url =
        "http://localhost:9082/realms/spring/protocol/openid-connect/token";
      const params = new URLSearchParams({
        grant_type: "refresh_token",
        refresh_token: refreshToken,
        client_id: "PetHaven",
        client_secret: "GuFIaAADNfBUpuahqxLvMPWzqt6g8fRL",
      });

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: params.toString(),
      });

      if (!response.ok) {
        throw new Error("Failed to refresh token");
      }

      const data = await response.json();
      localStorage.setItem("access_token", data.access_token);
      localStorage.setItem("refresh_token", data.refresh_token);

      return data.access_token;
    } catch (error) {
      console.error("Error refreshing token:", error);
      localStorage.removeItem("access_token");
      localStorage.removeItem("refresh_token");
      localStorage.removeItem("viewRole");
      userStore.$reset();
      navigateTo("/");
      throw error;
    }
  };

  return {
    getAccessToken,
    refreshAccessToken,
  };
};
